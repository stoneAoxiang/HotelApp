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

    }



    @OnClick(R.id.onshore_tv)
    public void onShoreClick() {
        startActivity(new Intent(getActivity(), OnShoreActivity.class));
    }

    @OnClick(R.id.outside_tv)
    public void outsideClick() {
        startActivity(new Intent(getActivity(), OutsideActivity.class));
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
        startActivity(new Intent(getActivity(), EmptyRoomQueryActivity.class));
    }


}
