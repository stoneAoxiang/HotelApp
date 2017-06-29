package com.grst.hotelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grst.hotelapp.R;
import com.grst.hotelapp.ui.PullToRefreshView.OnFooterRefreshListener;
import com.grst.hotelapp.ui.PullToRefreshView.OnHeaderRefreshListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class HomeFragmentPage extends Fragment implements
         OnHeaderRefreshListener, OnFooterRefreshListener{
    private String TAG = HomeFragmentPage.class.getCanonicalName();

    private View view;


    /*@InjectView(R.id.onshore_tv)
    TextView onshoreTV;

    @InjectView(R.id.onshore_tv)
    TextView offshoreTV;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment_view, null);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        initWidget();

    }

    private void initWidget() {


       /* onshoreTV = (TextView) view.findViewById(R.id.onshore_tv);
        onshoreTV.setOnClickListener(this);

        offshoreTV = (TextView) view.findViewById(R.id.offshore_tv);
        offshoreTV.setOnClickListener(this);*/

    }

  /*  @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.onshore_tv:

            intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
            break;

            case R.id.offshore_tv:

            intent = new Intent(getActivity(), CRUDActivity.class);
            startActivity(intent);
            break;


        }
    }*/


    @OnClick(R.id.onshore_tv)
    public void onShoreClick() {
        startActivity(new Intent(getActivity(), OnShoreActivity.class));
    }

    @OnClick(R.id.offshore_tv)
    public void offShoreClick() {
        startActivity(new Intent(getActivity(), CRUDActivity.class));
    }

    //下拉刷新
    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
    }

    //上拉加载
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
    }



}
