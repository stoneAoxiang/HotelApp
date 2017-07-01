package com.grst.hotelapp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.grst.hotelapp.R;
import com.grst.hotelapp.adapter.PopAdapter;
import com.grst.hotelapp.module.BankCard;
import com.grst.hotelapp.module.MyUser;
import com.grst.hotelapp.module.Person;
import com.grst.hotelapp.module.RoomInfo;
import com.grst.hotelapp.module.RoomType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;

public class OnShoreActivity extends BaseActivity {
	
	EditText et_number,et_code;
	private static final int REQUEST_CODE_IDCARD = 110;


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

	@InjectView(R.id.isDisabled)
	EditText isDisabled;

	@InjectView(R.id.linkPhone)
	EditText linkPhone;


	private PopupWindow popupWindow;
	private PopAdapter popAdapter;

	private String roomTypeId;

	private String currentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.on_shore_regist);

		ButterKnife.inject(this);
		/*et_number = (EditText) findViewById(R.id.et_number);
		et_code = (EditText) findViewById(R.id.et_code);
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, getResources().getStringArray(R.array.bmob_user_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				testBmob(position + 1);
			}
		});*/

	}

	@OnClick(R.id.take_photo)
	public void takePhotoClick() {
		Intent intent = new Intent(this, IDCardActivity.class);
		startActivityForResult(intent, REQUEST_CODE_IDCARD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_IDCARD && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();



			idCard.setText(bundle.getString("idNumber"));
			name.setText(bundle.getString("name"));
			sex.setText(bundle.getString("gender"));
			birthday.setText(bundle.getString("birthday"));
			address.setText(bundle.getString("address"));
			nation.setText(bundle.getString("ethnic"));



			String outputFile = bundle.getString("outputFile");
			Bitmap bitmap = BitmapFactory.decodeFile(outputFile);

//			setCardInfo(message);
			/*String infoS[] = message.split(",");

			Log.i(TAG, "身份证信息: " + infoS[2].substring(infoS[2].lastIndexOf("=") ));
			Log.i(TAG, "身份证信息: " + infoS[3].substring(infoS[3].lastIndexOf("=")));
			Log.i(TAG, "身份证信息: " + infoS[4].substring(infoS[4].lastIndexOf("=")));
			Log.i(TAG, "身份证信息: " + infoS[5].substring(infoS[5].lastIndexOf("=")));
			Log.i(TAG, "身份证信息: " + infoS[6].substring(infoS[6].lastIndexOf("=")));
			Log.i(TAG, "身份证信息: " + infoS[7].substring(infoS[7].lastIndexOf("=")));*/

			picPhoto.setImageBitmap(bitmap);
//			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//			alertDialog.setTitle(outputFile)
//					.setMessage(message)
//					.setPositiveButton("确定", null)
//					.show();
		}
	}

	private void setCardInfo(String message){
//
//		JSONObject jsonObject;
//		String result = new String();
//
//		try {
//			jsonObject = new JSONObject(message);
//
//			result = jsonObject.getJSONArray("");
//
//		} catch (JSONException e) {
//			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//			e.printStackTrace();
//		}
//
//			Log.i(TAG, "身份证信息: " + infoList.get(0));
//		Log.i(TAG, "身份证信息: " + infoList.get(1));
//		Log.i(TAG, "身份证信息: " + infoList.get(2));
//


	}


	@OnClick(R.id.room_type)
	public void roomTypeClick() {
//		toast("查询用户===========：" );

		currentView = "room_type";
		BmobQuery<RoomType> query = new BmobQuery<RoomType>();
		addSubscription(query.findObjects(new FindListener<RoomType>() {

			@Override
			public void done(List<RoomType> object, BmobException e) {
				if(e==null){
//					toast("查询用户成功：" + object.size());

					ArrayList<HashMap<String, String>> spinnerData = new ArrayList<HashMap<String, String>>();

					for (RoomType rt : object){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", rt.getObjectId());
						map.put("name", rt.getName());
						spinnerData.add(map);
					}

					showPopupWindow(spinnerData, roomTypeTV);
				}else{
					loge(e);
				}
			}

		}));
	}


	@OnClick(R.id.room_num)
	public void roomNumClick() {

		if(null == roomTypeId || roomTypeId.equals("")){
			toast("请先选择房间类型" );
			return;
		}
		currentView ="room_num";
		BmobQuery<RoomInfo> query = new BmobQuery<RoomInfo>();
		query.addWhereEqualTo("typeId", roomTypeId);
		addSubscription(query.findObjects(new FindListener<RoomInfo>() {

			@Override
			public void done(List<RoomInfo> object, BmobException e) {
				if(e==null){

					ArrayList<HashMap<String, String>> spinnerData = new ArrayList<HashMap<String, String>>();

					for (RoomInfo rt : object){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", rt.getObjectId());
						map.put("name", rt.getName());
						spinnerData.add(map);
					}

					showPopupWindow(spinnerData, roomNumTV);
				}else{
					loge(e);
				}
			}

		}));
	}



	private void showPopupWindow(ArrayList<HashMap<String, String>> spinnerValue, View v) {

		View view = LayoutInflater.from(this).inflate(R.layout.popmenu, null);
		TextView spinnerTitle = (TextView) view.findViewById(R.id.spinner_title);
		Log.i(TAG, "====================" + v.getId() );

		if(v.getId() == R.id.room_type){
			spinnerTitle.setText(R.string.room_type);
		}else{
			spinnerTitle.setText(R.string.room_num);
		}


		popAdapter = new PopAdapter(this, spinnerValue, null);

		//设置 listview
		ListView popListView = (ListView)view.findViewById(R.id.listView);
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



			if(currentView.equals("room_type")){

				HashMap<String, String> map = (HashMap<String, String>) popAdapter.getItem(position);

				roomTypeId = map.get("id");
				roomTypeTV.setText(map.get("name"));

			}else{
				HashMap<String, String> map = (HashMap<String, String>) popAdapter.getItem(position);


				roomNumTV.setText(map.get("name"));
			}


			popupWindow.dismiss();
		}
	};
	
	private void testBmob(int pos) {
		switch (pos) {
		case 1:
			testSignUp();
			break;
		case 2:
			testLogin();
			break;
		case 3:
			testGetCurrentUser();
			break;
		case 4:
			testLogOut();
			break;
		case 5:
			updateUser();
			break;
		case 6:
			checkPassword();
			break;
		case 7:
			testResetPasswrod();
			break;
		case 8:
			emailVerify();
			break;
		case 9:
			testFindBmobUser();
			break;
		case 10:
			loginByEmailPwd();
			break;
		case 11:
			loginByPhonePwd();
			break;
		case 12:
			loginByPhoneCode();
			break;
		case 13:
			signOrLogin();
			break;
		case 14:
			resetPasswordBySMS();
			break;
		case 15:
			updateCurrentUserPwd();
			break;
		}
	}
	
	@SuppressLint("UseValueOf")
	private void testSignUp() {
		final MyUser myUser = new MyUser();
		myUser.setUsername("smile0420");
		myUser.setPassword("123456");
		myUser.setAge(18);
		addSubscription(myUser.signUp(new SaveListener<MyUser>() {
			@Override
			public void done(MyUser s, BmobException e) {
				if(e==null){
					toast("注册成功:" +s.toString());
				}else{
					loge(e);
				}
			}
		}));
	}

	/**
	 * 注意下如果返回206错误 一般是多设备登录导致
	 */
	private void testLogin() {
		final BmobUser user = new BmobUser();
		user.setUsername("smile0420");
		user.setPassword("123456");
		//login回调
		/*user.login(new SaveListener<BmobUser>() {

			@Override
			public void done(BmobUser bmobUser, BmobException e) {
				if(e==null){
					toast(user.getUsername() + "登陆成功");
					testGetCurrentUser();
				}else{
					loge(e);
				}
			}
		});*/
		//v3.5.0开始新增加的rx风格的Api
		user.loginObservable(BmobUser.class).subscribe(new Subscriber<BmobUser>() {
			@Override
			public void onCompleted() {
				log("----onCompleted----");
			}

			@Override
			public void onError(Throwable e) {
				loge(new BmobException(e));
			}

			@Override
			public void onNext(BmobUser bmobUser) {
				toast(bmobUser.getUsername() + "登陆成功");
				testGetCurrentUser();
			}
		});
	}

	/**
	 * 获取本地用户
	 */
	private void testGetCurrentUser() {
//		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
//		if (myUser != null) {
//			log("本地用户信息:objectId = " + myUser.getObjectId() + ",name = " + myUser.getUsername()
//					+ ",age = "+ myUser.getAge());
//		} else {
//			toast("本地用户为null,请登录。");
//		}
		//V3.4.5版本新增加getObjectByKey方法获取本地用户对象中某一列的值
		String username = (String) BmobUser.getObjectByKey("username");
		Integer age = (Integer) BmobUser.getObjectByKey("age");
		Boolean sex = (Boolean) BmobUser.getObjectByKey("sex");
		JSONArray hobby= (JSONArray) BmobUser.getObjectByKey("hobby");
		JSONArray cards= (JSONArray) BmobUser.getObjectByKey("cards");
		JSONObject banker= (JSONObject) BmobUser.getObjectByKey("banker");
		JSONObject mainCard= (JSONObject) BmobUser.getObjectByKey("mainCard");
		log("username："+username+",\nage："+age+",\nsex："+ sex);
		log("hobby:"+(hobby!=null?hobby.toString():"为null")+"\ncards:"+(cards!=null ?cards.toString():"为null"));
		log("banker:"+(banker!=null?banker.toString():"为null")+"\nmainCard:"+(mainCard!=null ?mainCard.toString():"为null"));
	}

	/**
	 * 清除本地用户
	 */
	private void testLogOut() {
		BmobUser.logOut();
	}

	/**
	 * 更新用户操作并同步更新本地的用户信息
	 */
	private void updateUser() {
		final MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
		if (bmobUser != null) {
			final MyUser newUser = new MyUser();
			//-----------------------普通setter操作-------------------------------
			//number类型
			newUser.setAge(25);
			newUser.setSex(false);
			//object类型
//			newUser.setMainCard(new BankCard("工行", "10086"));
			//BmobObject类型
			Person person = new Person();
			person.setObjectId("721fe0cdf2");
			newUser.setBanker(person);
			//---------------------数组操作(add、addAll、addUnique、addAllUnique)---------------------------------------
			//添加Object类型的数组,Object数组调用addAllUnique、addUnique方法后本地用户信息未支持去重
			List<BankCard> cards =new ArrayList<BankCard>();
			cards.add(new BankCard("建行", "111"));
			newUser.addAll("cards", cards);
//			添加String类型的数组--String数组支持去重
			newUser.addAllUnique("hobby", Arrays.asList("游泳"));
			//----------------------自增操作---------------------------------------
//			newUser.increment("num",-2);
//			//----------------------setValue方式更新用户信息（必须先保证更新的列存在，否则会报internal error）----------------------------
//			//更新number
//			newUser.setValue("age",25);
//			//更新整个Object
//			newUser.setValue("banker",person);
//			//更新String数组
//			newUser.setValue("hobby",Arrays.asList("看书","游泳"));
////			//更新某个Object的值
//			newUser.setValue("mainCard.cardNumber","10011");
//			//更新数组中某个Object
//			newUser.setValue("cards.0", new BankCard("工行", "10086"));
			//更新数组中某个Object的某个字段的值
//			newUser.setValue("cards.0.bankName", "中行");
			addSubscription(newUser.update(bmobUser.getObjectId(),new UpdateListener() {

				@Override
				public void done(BmobException e) {
					if(e==null){
						testGetCurrentUser();
					}else{
						loge(e);
					}
				}
			}));
		} else {
			toast("本地用户为null,请登录。");
		}
	}

	/**
	 * 验证旧密码是否正确
	 * @param
	 * @return void
	 */
	private void checkPassword() {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		final MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
		// 如果你传的密码是正确的，那么arg0.size()的大小是1，这个就代表你输入的旧密码是正确的，否则是失败的
		query.addWhereEqualTo("password", "123456");
		query.addWhereEqualTo("username", bmobUser.getUsername());
		addSubscription(query.findObjects(new FindListener<MyUser>() {

			@Override
			public void done(List<MyUser> object, BmobException e) {
				if(e==null){
					toast("查询密码成功:" + object.size());
				}else{
					loge(e);
				}
			}

		}));
	}

	/**
	 * 重置密码
	 */
	private void testResetPasswrod() {
		final String email = "123456789@qq.com";
		addSubscription(BmobUser.resetPasswordByEmail(email, new UpdateListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
				}else{
					loge(e);
				}
			}
		}));
	}

	/**
	 * 查询用户
	 */
	private void testFindBmobUser() {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo("username", "lucky");
		addSubscription(query.findObjects(new FindListener<MyUser>() {

			@Override
			public void done(List<MyUser> object, BmobException e) {
				if(e==null){
					toast("查询用户成功：" + object.size());

				}else{
					loge(e);
				}
			}

		}));
	}

	/**
	 * 验证邮件
	 */
	private void emailVerify() {
		final String email = "75727433@qq.com";
		addSubscription(BmobUser.requestEmailVerify(email, new UpdateListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					toast("请求验证邮件成功，请到" + email + "邮箱中进行激活账户。");
				}else{
					loge(e);
				}
			}

		}));
	}

	private void loginByEmailPwd(){
		addSubscription(BmobUser.loginByAccount("123456@163.com", "123456", new LogInListener<MyUser>() {

			@Override
			public void done(MyUser user, BmobException e) {
				if(user!=null){
					log(user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}
			}
		}));
	}

	private void loginByPhonePwd(){
		String number = et_number.getText().toString();
		addSubscription(BmobUser.loginByAccount(number, "123456", new LogInListener<MyUser>() {

			@Override
			public void done(MyUser user, BmobException e) {
				if(user!=null){
					toast("登录成功");
					log(user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		}));
	}

	private void loginByPhoneCode(){
		//1、调用请求验证码接口
//		BmobSMS.requestSMSCode("手机号码", "模板名称",new QueryListener<Integer>() {
//
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				if(ex==null){//验证码发送成功
//					log("短信id："+smsId);
//				}
//			}
//		});
		String number = et_number.getText().toString();
		String code = et_code.getText().toString();
		//2、使用验证码进行登陆
		addSubscription(BmobUser.loginBySMSCode(number, code, new LogInListener<MyUser>() {

			@Override
			public void done(MyUser user, BmobException e) {
				if(user!=null){
					toast("登录成功");
					log(""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		}));
	}
	/** 一键注册登录
	 * @method signOrLogin
	 * @return void
	 * @exception
	 */
	private void signOrLogin(){
		//1、调用请求验证码接口
//		BmobSMS.requestSMSCode("18312662735", "模板名称",new QueryListener<Integer>() {
//
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				if(ex==null){//验证码发送成功
//					log("smile", "短信id："+smsId);
//				}
//			}
//
//		});
		String number = et_number.getText().toString();
		String code = et_code.getText().toString();
		//2、使用手机号和短信验证码进行一键注册登录,这步有两种方式可以选择
//		//第一种：
//		BmobUser.signOrLoginByMobilePhone(number, code,new LogInListener<MyUser>() {
//
//			@Override
//			public void done(MyUser user, BmobException e) {
//				if(user!=null){
//					toast("登录成功");
//					log(""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
//				}else{
//					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
//				}
//			}
//		});
		//第二种：这种方式比较灵活，可以在注册或登录的同时设置保存多个字段值
		final MyUser user = new MyUser();
		user.setPassword("123456");
		user.setMobilePhoneNumber("15018879340");
		addSubscription(user.signOrLogin(code, new SaveListener<MyUser>() {

			@Override
			public void done(MyUser myUser, BmobException e) {
				if(e==null){
					toast("登录成功");
					log(""+myUser.getAge()+"-"+myUser.getObjectId()+"-"+myUser.getEmail());
				}else{
					loge(e);
				}
			}

		}));
	}

	/** 通过短信验证码来重置用户密码
	 * @method requestSmsCode
	 * @return void
	 * 注：整体流程是先调用请求验证码的接口获取短信验证码，随后调用短信验证码重置密码接口来重置该手机号对应的用户的密码
	 */
	private void resetPasswordBySMS(){
		//1、请求短信验证码
//		BmobSMS.requestSMSCode("手机号码", "模板名称",new QueryListener<Integer>() {
//
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				if(ex==null){//验证码发送成功
//					log("短信id："+smsId);
//				}
//			}
//		});
		String code = et_code.getText().toString();
		//2、重置的是绑定了该手机号的账户的密码
		addSubscription(BmobUser.resetPasswordBySMSCode(code,"1234567", new UpdateListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					toast("密码重置成功");
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		}));
	}

	/**修改当前用户密码
	 * @return void
	 * @exception
	 */
	private void updateCurrentUserPwd(){
		addSubscription(BmobUser.updateCurrentUserPassword("旧密码", "新密码", new UpdateListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					toast("密码修改成功，可以用新密码进行登录");
				}else{
					loge(e);
				}
			}
		}));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
