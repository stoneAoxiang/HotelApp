package com.grst.hotelapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.grst.hotelapp.R;

public class AboutMeFragmentPage extends Fragment implements OnClickListener {

    private String TAG = AboutMeFragmentPage.class.getCanonicalName();

    private View view;

    public void onAttach(Activity activity) {
        super.onAttach(activity);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.about_me_fragment_view, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {



        }

    }


}