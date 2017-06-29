package com.grst.hotelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.grst.hotelapp.R;
import com.grst.hotelapp.logic.ChangeViewListener;
import com.grst.hotelapp.util.ConstantValue;
import com.grst.hotelapp.util.PropertiesUtil;
import com.grst.hotelapp.util.Tools;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainTabActivity extends FragmentActivity implements ChangeViewListener {

    private String TAG = MainTabActivity.class.getCanonicalName();
    // 定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    // 定义一个布局
    private LayoutInflater layoutInflater;

    @InjectView(R.id.home_draw_layout)
    DrawerLayout drawerLayout;

    private Class fragmentArray[] = {HomeFragmentPage.class,
            AboutMeFragmentPage.class};

    private int mImageViewArray[] = {R.drawable.home_icon_click, R.drawable.owen_icon_click};

    private String mTextviewArray[] = {ConstantValue.HOME_PAGE_TAB, ConstantValue.ME_TAB};

    /**
     * 要跳转的导航
     */
    private String jumpTab = ConstantValue.HOME_PAGE_TAB;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_tab_layout);
        ButterKnife.inject(this);


        jumpTab = getIntent().getStringExtra("jumpTab");

        initView();

        Tools.checkNet(this);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象

        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i)
//                    .setBackgroundResource(R.color.tab_background_color);
            mTabHost.setCurrentTabByTag(jumpTab);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tag) {
                    mTabHost.setCurrentTabByTag(tag);
                }
            });
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }




    long time = 0l;
    @Override
    public void onBackPressed() {

        long timeNow = System.currentTimeMillis();
        if (timeNow - time < 1500) {
            super.onBackPressed();
            String isLogin = PropertiesUtil.getProperties("isLogin");

            Log.i(TAG, "isLogin=====================" + isLogin);
            //如果没有选择记住密码，则删除用户保存的信息
            if (null == isLogin || isLogin.equals("false")) {
                PropertiesUtil.clearProperties(0);
            }
            Intent exit = new Intent(Intent.ACTION_MAIN);
            exit.addCategory(Intent.CATEGORY_HOME);
            exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exit);
            System.exit(0);
        } else {
            time = timeNow;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onChangeView(int viewFlag) {

        switch (viewFlag) {
            case ConstantValue.GET_MAIN_TAB:
                changeTabView(ConstantValue.HOME_PAGE_TAB);
                break;

            case ConstantValue.GET_MY_HOME_TAB:
                Toast.makeText(this, "GET_MY_HOME_TAB", Toast.LENGTH_SHORT).show();
                changeTabView(ConstantValue.ME_TAB);
                break;

        }

    }

    private void changeTabView(String tabName) {

        mTabHost.setCurrentTabByTag(tabName);

        jumpTab = tabName;
    }

    private void showLifeView() {
        mTabHost.setCurrentTabByTag("首页");
    }
}
