package com.grst.hotelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grst.hotelapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragmentPage extends Fragment{
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

    @OnClick(R.id.checkInQuery)
    public void checkInClick() {
        startActivity(new Intent(getActivity(), CheckInQueryActivity.class));
    }

    @OnClick(R.id.checkOut)
    public void checkOutClick() {
        startActivity(new Intent(getActivity(), CheckOutActivity.class));
    }

    @OnClick(R.id.emptyeRoomQuery)
    public void emptyeRoomQueryClick() {
       // startActivity(new Intent(getActivity(), emptyeRoomQueryActivity.class));
    }


}
