package com.grst.hotelapp.ui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


public class ExtendViewPage  extends ViewPager {
	
	private String TAG = ExtendViewPage.class.getCanonicalName();

	public ExtendViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ExtendViewPage(Context context, AttributeSet attrs) {
		super(context, attrs); 
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "ExtendViewPage分发-dispatchTouchEvent-ACTION_DOWN...");
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ExtendViewPage分发-dispatchTouchEvent-ACTION_UP...");
			break;
		default:break;
		}
		
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		int y = (int) e.getRawY();
		int x=(int) e.getRawX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "ExtendViewPage拦截 ACTION_DOWN==========================");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "ExtendViewPage拦截 ACTION_MOVE==========================");
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.i(TAG, "ExtendViewPage拦截 ACTION_UP ACTION_CANCEL==========================");
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "ExtendViewPage onTouchEvent ACTION_DOWN==========================");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "ExtendViewPage onTouchEvent ACTION_MOVE==========================");
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.i(TAG, "ExtendViewPage onTouchEvent ACTION_UP ACTION_CANCEL==========================");
			break;
		}
		return false;
	}
	
	

}
