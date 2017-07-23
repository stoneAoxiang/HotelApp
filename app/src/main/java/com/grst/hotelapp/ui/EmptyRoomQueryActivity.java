package com.grst.hotelapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.grst.hotelapp.R;
import com.grst.hotelapp.module.CheckIn;
import com.grst.hotelapp.module.RoomInfo;
import com.grst.hotelapp.ui.custom.HomeGridView;
import com.grst.hotelapp.ui.custom.PullToRefreshView;
import com.grst.hotelapp.util.Tools;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class EmptyRoomQueryActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private String TAG = EmptyRoomQueryActivity.class.getCanonicalName();

    @InjectView(R.id.activity_name)
    TextView activityName;

    //选择按钮
    @InjectView(R.id.select_button)
    RadioGroup selectButton;

    @InjectView(R.id.empty_room)
    RadioButton emptyRoomButton;

    @InjectView(R.id.no_empty_room)
    RadioButton noEmptyRoomButton;

    private QueryType selectTab = QueryType.emptyRoomButton;

    private enum QueryType {

        emptyRoomButton, noEmptyRoomButton
    }

    @InjectView(R.id.return_back)
    ImageView returnBack;

    @InjectView(R.id.content_gv)
    HomeGridView activeList;

    @InjectView(R.id.process_bar)
    ProgressBar processBar;

    @InjectView(R.id.no_data)
    TextView noData;

    @InjectView(R.id.refreshable_view)
    PullToRefreshView refreshableView;

    // 入住信息数据
    private EmptyRoomListAdapter emptyRoomListAdapter;

    // 数据列表滑动到下方时，加载一个的布局，用于提示用户数据正在加载
    private boolean firstLoad = true; // 是否第一次读取数据

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.emptyroom_query_view);

        ButterKnife.inject(this);

        initWidget();
    }

    @Override
    protected void onStart() {

        super.onStart();

        emptyRoomListAdapter = new EmptyRoomListAdapter();

        initPage();
        getCheckInListInfo(selectTab);

    }

    private void initWidget() {
        activityName.setText(R.string.emptyeRoomQuery);

        refreshableView.setOnHeaderRefreshListener(this);
        refreshableView.setOnFooterRefreshListener(this);

        selectButton.setOnCheckedChangeListener(listener);

        returnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (checkedId == emptyRoomButton.getId()) {

                selectTab = QueryType.emptyRoomButton;
                initPage();
                getCheckInListInfo(selectTab);

            } else if (checkedId == noEmptyRoomButton.getId()) {

                selectTab = QueryType.noEmptyRoomButton;
                initPage();
                getCheckInListInfo(selectTab);
            }
        }
    };

    /**
     * 获取入住列表
     */
    private void getCheckInListInfo(QueryType selectTab) {

        BmobQuery<RoomInfo> query = new BmobQuery<>();

        if(selectTab.equals(QueryType.emptyRoomButton)){
            query.addWhereEqualTo("isEmpty", true);

            Log.i(TAG, "空房==============: ");

        }else{
            query.addWhereEqualTo("isEmpty", false);

            Log.i(TAG, "客房==============: ");
        }

        // 跳过之前页数并去掉重复数据
        query.setSkip(countPage * nowPage);

        // 设置每页数据个数
        query.setLimit(countPage);
        // 查找数据
        query.findObjects(new FindListener<RoomInfo>() {

            @Override
            public void done(List<RoomInfo> objects, BmobException e) {
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

                        updateEmptyRoomListInfo(objects);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                        processBar.setVisibility(View.GONE);
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

    private void setCheckOut(String id) {
        CheckIn checkIn = new CheckIn();
        checkIn.setCheckOutTime(Tools.getSystemTime());
        checkIn.update(id, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i(TAG,"更新成功");
                    initPage();
                    getCheckInListInfo(QueryType.emptyRoomButton);
                }else{
                    Log.i(TAG,"更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 更新房屋租凭列表UI
     */
    private void updateEmptyRoomListInfo(List<RoomInfo> list) {

        nowPage++;

        if (firstLoad) {
            refreshableView.completeNow();
            emptyRoomListAdapter.clear();
            emptyRoomListAdapter.addAll(list);
            activeList.setAdapter(emptyRoomListAdapter);
        } else {
            emptyRoomListAdapter.addAll(list);
            emptyRoomListAdapter.notifyDataSetChanged();
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

        getCheckInListInfo(selectTab);
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
        getCheckInListInfo(selectTab);
    }

    public class EmptyRoomListAdapter extends QuickAdapter<RoomInfo> {

        public EmptyRoomListAdapter() {
            super(EmptyRoomQueryActivity.this, R.layout.emptyroom_query_list);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, final RoomInfo item) {




            if(selectTab.equals(QueryType.emptyRoomButton)){
                helper.setVisible(R.id.no_empty_room, false);
                helper.setVisible(R.id.empty_room, true);
                helper.setText(R.id.empty_room, "房间号:" + item.getName());

            }else{
                helper.setVisible(R.id.no_empty_room, true);
                helper.setVisible(R.id.empty_room, false);
                helper.setText(R.id.no_empty_room, "房间号:" + item.getName());
            }




        }

    }
}
