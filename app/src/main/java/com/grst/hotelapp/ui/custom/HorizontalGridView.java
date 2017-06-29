package com.grst.hotelapp.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class HorizontalGridView extends GridView {

	private String TAG = HorizontalGridView.class.getCanonicalName();

	public HorizontalGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalGridView(Context context) {
		super(context);
	}

	public HorizontalGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		//int类型32位。而模式有三种，要表示三种状  态，至少得2位二进制位。于是系统
		//采用了最高的2位表示模式,余下的30未表示允许的最大尺寸（即Integer.MAX_VALUE >> 2 最大整型值右移2位得到30位）
		//最高两位是00的时候表示"未指定模式"。即MeasureSpec.UNSPECIFIED
		//最高两位是01的时候表示"'精确模式"。即MeasureSpec.EXACTLY
		//最高两位是11的时候表示"最大模式"。即MeasureSpec.AT_MOST

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		
//		Log.i(TAG, "计算GRIDVIEW的高度================="+expandSpec);
		super.onMeasure(expandSpec, heightMeasureSpec);
	}
}