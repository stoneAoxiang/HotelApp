package com.grst.hotelapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grst.hotelapp.util.ConstantValue;

import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class BaseFragment extends Fragment {
    private CompositeSubscription mCompositeSubscription;

    public static String TAG = BaseFragment.class.getCanonicalName();

    /**
     * 当前页数据索引
     */
    public int nowPage = 0;
    /**
     * 每页显示数据量: 20条
     */
    public int countPage = ConstantValue.PAGE_SIZE;
    /**
     * 数据总页数
     */
    public int totalPage = 1;

    protected FragmentActivity activity;

    /**
     * 解决Subscription内存泄露问题
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

    }



    public void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    Toast mToast;

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resId,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public static void log(String msg) {
        Log.i(TAG,"===============================================================================");
        Log.i(TAG, msg);
    }

    public static void loge(Throwable e) {
        Log.i(TAG,"===============================================================================");
        if(e instanceof BmobException){
            Log.e(TAG, "错误码："+((BmobException)e).getErrorCode()+",错误描述："+((BmobException)e).getMessage());
        }else{
            Log.e(TAG, "错误描述："+e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
