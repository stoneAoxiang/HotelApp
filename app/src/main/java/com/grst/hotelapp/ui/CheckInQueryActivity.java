package com.grst.hotelapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grst.hotelapp.R;
import com.grst.hotelapp.module.CheckIn;
import com.grst.hotelapp.ui.custom.PullToRefreshView;
import com.grst.hotelapp.util.ConstantValue;
import com.grst.hotelapp.util.Tools;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class CheckInQueryActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private String TAG = CheckInQueryActivity.class.getCanonicalName();

    @InjectView(R.id.activity_name)
    TextView activityName;

    @InjectView(R.id.return_back)
    ImageView returnBack;

    @InjectView(R.id.content_gv)
    ListView activeList;

    @InjectView(R.id.process_bar)
    ProgressBar processBar;

    @InjectView(R.id.no_data)
    TextView noData;

    @InjectView(R.id.refreshable_view)
    PullToRefreshView refreshableView;

    // 入住信息数据
    private CheckInListAdapter checkInListAdapter;

    // 数据列表滑动到下方时，加载一个的布局，用于提示用户数据正在加载
    private boolean firstLoad = true; // 是否第一次读取数据

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.checkin_query_view);

        ButterKnife.inject(this);

        initWidget();
    }

    @Override
    protected void onStart() {

        super.onStart();

        checkInListAdapter = new CheckInListAdapter();

        initPage();
        getCheckInListInfo();

    }

    private void initWidget() {
        activityName.setText(R.string.checkInQuery);

        refreshableView.setOnHeaderRefreshListener(this);
        refreshableView.setOnFooterRefreshListener(this);

        returnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

    }

    /**
     * 获取入住列表
     */
    private void getCheckInListInfo() {


        BmobQuery<CheckIn> query = new BmobQuery<>();

        query.addWhereDoesNotExists("checkOutTime");

        // 按时间降序查询
        query.order("-checkInTime");

        // 跳过之前页数并去掉重复数据
        query.setSkip(countPage * nowPage);

        // 设置每页数据个数
        query.setLimit(countPage);
        // 查找数据
        query.findObjects(new FindListener<CheckIn>() {

            @Override
            public void done(List<CheckIn> objects, BmobException e) {
                if (e == null) {
                    if (objects != null && objects.size() > 0) {
//                        toast("查询成功：" + objects.size() + "个");

                        if(objects.size() < countPage){
                            refreshableView.setAllowToLoadMore(false);
                        }

                        activeList.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        processBar.setVisibility(View.GONE);

                        refreshableView.setAllowToLoadMore(true);

                        updateRentalListInfo(objects);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                        refreshableView.setAllowToLoadMore(false);
                        refreshableView.onFooterRefreshComplete();
                        refreshableView.onHeaderRefreshComplete();
                    }
                } else {
                    loge(e);

                }
            }

        });


    }

    /**
     * 更新房屋租凭列表UI
     */
    private void updateRentalListInfo(List<CheckIn> list) {

        nowPage++;

        if (firstLoad) {
            refreshableView.completeNow();
            checkInListAdapter.clear();
            checkInListAdapter.addAll(list);
            activeList.setAdapter(checkInListAdapter);
        } else {
            checkInListAdapter.addAll(list);
            checkInListAdapter.notifyDataSetChanged();
            refreshableView.onFooterRefreshComplete();
        }
    }

    /**
     * 初始化翻页值
     */
    private void initPage() {

        nowPage = 0;
        refreshableView.setAllowToLoadMore(true);
        firstLoad = true;
        processBar.setVisibility(View.VISIBLE);
        activeList.setVisibility(View.GONE);
    }


    /**
     * 翻页加载数据
     */
    private void reloadData() {
        firstLoad = false;

        getCheckInListInfo();
    }

    //上拉加载更多
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        reloadData();
    }

    //下拉刷新
    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        initPage();
        getCheckInListInfo();
    }

    public class CheckInListAdapter extends QuickAdapter<CheckIn> {

        public CheckInListAdapter() {
            super(CheckInQueryActivity.this, R.layout.checkin_query_list);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, CheckIn item) {

            if(null == item.getCredentialPic()){
                helper.setBackgroundRes(R.id.card_img, R.mipmap.default_picture);
                Log.i(TAG, "name null ");
            }else{
                String name  = item.getCredentialPic().getFileUrl();
                Log.i(TAG, "name: " + name);


                String imgUrl = item.getCredentialPic().getFileUrl();
                Picasso.with(context).load(imgUrl)
                        .placeholder(R.mipmap.default_picture)
                        .error(R.mipmap.default_picture)
                        .into((ImageView) helper.getView(R.id.card_img));
            }

            helper.setText(R.id.roomNo, "房间号:" + item.getRoomNo());
            helper.setText(R.id.checkInTime, "入住时间:" + item.getCheckInTime());

            helper.setText(R.id.name, "姓名:" + item.getName());
            helper.setText(R.id.firstName, "英文姓:" + item.getFirstName() == null ? "" : item.getFirstName());
            helper.setText(R.id.lastName, "英文名:" + item.getLastName() == null ? "" : item.getLastName());
            helper.setText(R.id.sex, "性别:" + item.getSex());
            helper.setText(R.id.country, "国籍:" + item.getCountry());
            helper.setText(R.id.credentialType, "证件类型:" + item.getCredentialType());
            helper.setText(R.id.idCard, "证件号码:" + item.getIdCard());


        }

    }
}
