package com.grst.hotelapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.grst.hotelapp.R;
import com.grst.hotelapp.adapter.PopAdapter;
import com.grst.hotelapp.module.CheckIn;
import com.grst.hotelapp.module.RoomInfo;
import com.grst.hotelapp.module.RoomType;
import com.grst.hotelapp.util.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class OnShoreActivity extends BaseActivity {

    EditText et_number, et_code;
    private static final int REQUEST_CODE_IDCARD = 110;

    String outputFile = "";

    @InjectView(R.id.pic_photo)
    ImageView picPhoto;

    @InjectView(R.id.room_type)
    EditText roomTypeTV;

    @InjectView(R.id.room_num)
    EditText roomNumTV;

    @InjectView(R.id.idCard)
    EditText idCard;

    @InjectView(R.id.name)
    EditText name;

    @InjectView(R.id.sex)
    EditText sex;

    @InjectView(R.id.birthday)
    EditText birthday;

    @InjectView(R.id.address)
    EditText address;

    @InjectView(R.id.nation)
    EditText nation;

//	@InjectView(R.id.isDisabled)
//	EditText isDisabled;

    @InjectView(R.id.linkPhone)
    EditText linkPhone;


    private PopupWindow popupWindow;
    private PopAdapter popAdapter;

    private String roomTypeId;
    private String roomId;

    private String currentView;

    private AlertDialog.Builder alertDialog;

    ProgressDialog dialog =null;

    private static String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_shore_regist);

        ButterKnife.inject(this);

    }

    @OnClick(R.id.take_photo)
    public void takePhotoClick() {
        Intent intent = new Intent(this, IDCardActivity.class);
        startActivityForResult(intent, REQUEST_CODE_IDCARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IDCARD && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();

            idCard.setText(bundle.getString("idNumber"));
            name.setText(bundle.getString("name"));
            sex.setText(bundle.getString("gender"));
            birthday.setText(bundle.getString("birthday"));
            address.setText(bundle.getString("address"));
            nation.setText(bundle.getString("ethnic"));

            outputFile = bundle.getString("outputFile");

//            Log.i(TAG, "outputFile===========: " + outputFile);
            Bitmap bitmap = BitmapFactory.decodeFile(outputFile);

            picPhoto.setImageBitmap(bitmap);
        }
    }


    @OnClick(R.id.room_type)
    public void roomTypeClick() {
//		toast("查询用户===========：" );

        currentView = "room_type";
        BmobQuery<RoomType> query = new BmobQuery<RoomType>();
        addSubscription(query.findObjects(new FindListener<RoomType>() {

            @Override
            public void done(List<RoomType> object, BmobException e) {
                if (e == null) {

                    ArrayList<HashMap<String, String>> spinnerData = new ArrayList<HashMap<String, String>>();

                    for (RoomType rt : object) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", rt.getObjectId());
                        map.put("name", rt.getName());
                        spinnerData.add(map);
                    }

                    showPopupWindow(spinnerData, roomTypeTV);
                } else {
                    loge(e);
                }
            }

        }));
    }


    @OnClick(R.id.room_num)
    public void roomNumClick() {

        if (null == roomTypeId || roomTypeId.equals("")) {
            toast("请先选择房间类型");
            return;
        }
        currentView = "room_num";
        BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
        query.addWhereEqualTo("typeId", roomTypeId);
        query.addWhereEqualTo("isEmpty", true);
        addSubscription(query.findObjects(new FindListener<RoomInfo>() {

            @Override
            public void done(List<RoomInfo> object, BmobException e) {
                if (e == null) {

                    ArrayList<HashMap<String, String>> spinnerData = new ArrayList<HashMap<String, String>>();

                    for (RoomInfo rt : object) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", rt.getObjectId());
                        map.put("name", rt.getName());
                        spinnerData.add(map);
                    }

                    showPopupWindow(spinnerData, roomNumTV);
                } else {
                    loge(e);
                }
            }

        }));
    }

    @OnClick(R.id.submit)
    public void submitClick() {
        if (checkValue()) {
            insertObject();
        }
    }

    /**
     * 插入对象
     */
    private void insertObject() {

        File fileUrl = new File(outputFile);
        final BmobFile cardPic = new BmobFile(fileUrl);

        final CheckIn checkIn = new CheckIn();

        checkIn.setAddress(address.getText().toString());
        checkIn.setBirthday(birthday.getText().toString());
        checkIn.setCheckInTime(Tools.getSystemTime());
        checkIn.setCountry("中国");
        checkIn.setCredentialType("身份证");
        checkIn.setIdCard(idCard.getText().toString());
        checkIn.setLinkPhone(linkPhone.getText().toString());
        checkIn.setNation(nation.getText().toString());
        checkIn.setName(name.getText().toString());
        checkIn.setSex(sex.getText().toString());
        checkIn.setRoomNo(roomNumTV.getText().toString());
        checkIn.setRoomId(roomId);
        checkIn.setCredentialPic(cardPic);


        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("上传中...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        cardPic.uploadObservable(new ProgressCallback() {//上传文件操作
            @Override
            public void onProgress(Integer value, long total) {
                log("uploadMovoieFile-->onProgress:"+value);
                dialog.setProgress(value);
            }
        }).doOnNext(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                url = cardPic.getUrl();
                log("上传成功："+url+","+cardPic.getFilename());

            }
        }).concatMap(new Func1<Void, Observable<String>>() {//将bmobFile保存到checkIn表中
            @Override
            public Observable<String> call(Void aVoid) {
                return saveObservable(checkIn);
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

                log("--onCompleted--");

            }

            @Override
            public void onError(Throwable e) {
                log("--onError--:"+e.getMessage());
                dialog.dismiss();
            }

            @Override
            public void onNext(String s) {
                dialog.dismiss();
                log("download的文件地址："+s);
                tipDialog(checkIn.getName(), checkIn.getRoomNo());
            }
        });


    }


    private Observable<String> saveObservable(BmobObject obj){

        Log.i(TAG, ((CheckIn)obj).getRoomNo() + "房间已入住");
        //把房间置为“已入住”
        setRoomCheckIn(((CheckIn)obj).getRoomId());

        return obj.saveObservable();
    }

    private void setRoomCheckIn(final String roomId){
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setEmpty(false);
        roomInfo.update(roomId, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i(TAG, roomId + "房间已入住更新成功");
                }else{
                    Log.i(TAG,"更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void tipDialog(String name, String roomNo){
        alertDialog = new AlertDialog.Builder(OnShoreActivity.this);

        alertDialog.setTitle("入住房间")
                .setMessage(name + "您入住的房间号为：" + roomNo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                        finish();
                    }
                })
                .show();
    }

    private boolean checkValue() {

        if (roomTypeTV.getText().toString().equals("")) {
            toast("房间不能为空");
            return false;
        }

        if (roomNumTV.getText().toString().equals("")) {
            toast("房间号不能为空");
            return false;
        }

        if (idCard.getText().toString().equals("")) {
            toast("身份证不能为空");
            return false;
        }

        if (name.getText().toString().equals("")) {
            toast("姓名不能为空");
            return false;
        }

        if (sex.getText().toString().equals("")) {
            toast("性别不能为空");
            return false;
        }

        if (birthday.getText().toString().equals("")) {
            toast("生日不能为空");
            return false;
        }

        if (address.getText().toString().equals("")) {
            toast("地址不能为空");
            return false;
        }

        if (nation.getText().equals("")) {
            toast("民族不能为空");
            return false;
        }

        return true;
    }


    private void showPopupWindow(ArrayList<HashMap<String, String>> spinnerValue, View v) {

        View view = LayoutInflater.from(this).inflate(R.layout.popmenu, null);
        TextView spinnerTitle = (TextView) view.findViewById(R.id.spinner_title);

        if (v.getId() == R.id.room_type) {
            spinnerTitle.setText(R.string.room_type);
        } else {
            spinnerTitle.setText(R.string.room_num);
        }


        popAdapter = new PopAdapter(this, spinnerValue, null);

        //设置 listview
        ListView popListView = (ListView) view.findViewById(R.id.listView);
        popListView.setOnItemClickListener(popmenuItemClickListener);
        popListView.setAdapter(popAdapter);
        popListView.setFocusableInTouchMode(true);
        popListView.setFocusable(true);


//	    popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view, 300, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();

        popupWindow.showAsDropDown(v);
    }

    // 弹出菜单监听器
    OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            if (currentView.equals("room_type")) {

                HashMap<String, String> map = (HashMap<String, String>) popAdapter.getItem(position);

                roomTypeId = map.get("id");
                roomTypeTV.setText(map.get("name"));

            } else {
                HashMap<String, String> map = (HashMap<String, String>) popAdapter.getItem(position);

                roomId = map.get("id");
                roomNumTV.setText(map.get("name"));
            }


            popupWindow.dismiss();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
