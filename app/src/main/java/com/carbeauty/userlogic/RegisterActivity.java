package com.carbeauty.userlogic;

import com.bigkoo.quicksidebardemo.CarSelectorActivity;
import com.carbeauty.R;
import com.carbeauty.dialog.AcCarInfoDialog;
import com.carbeauty.dialog.NumberSelectorDialog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import cn.service.MD5Generator;
import cn.service.RegType;
import cn.service.Util;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;

public class RegisterActivity extends Activity{
    EditText accountEdit;
    EditText passEdit;
    EditText repassEdit;
    Button regBtn;
    ActionBar mActionbar;
    TextView tvTitle;

	EditText carBrandEdit;
	EditText carnumberEdit;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.register);
    	initCustomActionBar();
    	accountEdit=(EditText) findViewById(R.id.accountEdit);
    	passEdit=(EditText) findViewById(R.id.passEdit);
    	repassEdit=(EditText) findViewById(R.id.repassEdit);
		carBrandEdit=(EditText)findViewById(R.id.carBrandEdit);
		carnumberEdit=(EditText)findViewById(R.id.carnumberEdit);

		regBtn=(Button) findViewById(R.id.regBtn);


    	
    	regBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String account  = accountEdit.getText().toString();
				String pass     = passEdit.getText().toString();
				String repass   = repassEdit.getText().toString();

				String carNumber= carnumberEdit.getText().toString();
				String carBrand = carBrandEdit.getText().toString();


				if(Util.isEmpty(account)||Util.isEmpty(pass)||Util.isEmpty(repass)){
					Toast.makeText(getApplicationContext(), "注册用户名和密码不能为空",Toast.LENGTH_SHORT).show();
				}else if(!Util.isPhoneNumber(account)){
					Toast.makeText(getApplicationContext(), "用户名请使用手机号注册",Toast.LENGTH_SHORT).show();
				}else if(!Util.isOverMinSize(pass,6)){
					Toast.makeText(getApplicationContext(), "密码位数小于6位",Toast.LENGTH_SHORT).show();
				}else if(!pass.equals(repass)){
					Toast.makeText(getApplicationContext(), "两次输入密码不一致",Toast.LENGTH_SHORT).show();
				}else if(Util.isEmpty(carNumber)){
					Toast.makeText(getApplicationContext(), "车牌号不能为空",Toast.LENGTH_SHORT).show();
				}else if(Util.isEmpty(carBrand)){
					Toast.makeText(getApplicationContext(), "车品牌信息不能为空",Toast.LENGTH_SHORT).show();
				}else{
					new RegisterUserTask(getApplicationContext(), account, pass,carBrand,carNumber).execute();
				}
				
			}
		});

		carBrandEdit.setKeyListener(null);
		carBrandEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					Intent it=new Intent(RegisterActivity.this,CarSelectorActivity.class);
					startActivityForResult(it,0);
				}
			}
		});
		carBrandEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it=new Intent(RegisterActivity.this,CarSelectorActivity.class);
				startActivityForResult(it,0);
			}
		});


		carnumberEdit.setKeyListener(null);
		carnumberEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					Intent it=new Intent(RegisterActivity.this,NumberSelectorDialog.class);
					startActivityForResult(it,1);
				}
			}
		});
		carnumberEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it=new Intent(RegisterActivity.this,NumberSelectorDialog.class);
				startActivityForResult(it,1);
			}
		});


    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==NumberSelectorDialog.resultCode){
			String result=data.getStringExtra(NumberSelectorDialog.KEY_RESULT);
			Log.v("numberCar","result "+result);
			carnumberEdit.setText(result);
		}else if(resultCode==CarSelectorActivity.RESULT_CODE){
			String result=data.getStringExtra(CarSelectorActivity.KEY_RESULT);
			Log.v("CarSelectorActivity","result "+result);
			carBrandEdit.setText(result);
		}
	}
	private boolean initCustomActionBar() {
		mActionbar = getActionBar();
		if (mActionbar == null) {
			return false;
		}
		mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionbar.setDisplayShowCustomEnabled(true);
		mActionbar.setCustomView(R.layout.top_back_center_bar);
		tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
		Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
		rightBtn.setVisibility(View.GONE);
		Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
		
		tvTitle.setText("用户注册");
		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		return true;
	}
	class RegisterUserTask extends AsyncTask<String, String, String>{
		String  loginName;
		String  password;
		Context ctx;
		String  carbrand;
		String  carNumber;
		int userId;
		
		RegisterUserTask(Context ctx, String loginName, String password,String carbrand,String carnumber){
			this.loginName=loginName;
			this.password=password;
			this.ctx=ctx;
			this.carbrand=carbrand;
			this.carNumber=carnumber;
		}
		@Override
		protected String doInBackground(String... params) {
			try {
				userId=WSConnector.getInstance().createUser(loginName, password, loginName, RegType.REGULAR_USER_TYPE.getVal(), -1, null, null);

				if(userId>0){
					boolean loginYN=WSConnector.getInstance().appUserLogin(loginName, MD5Generator.reverseMD5Value(password), -1, "android", false);

					if(loginYN){
						CarInfo carInfo=new CarInfo(0,0,carNumber,0,carbrand);
						WSConnector.getInstance().createCar(carInfo);
					}
				}

				return userId+"";



			} catch (WSException e) {
				return e.getErrorMsg();
			}catch (UnsupportedEncodingException e) {
				return e.getMessage();
			}
		}
		@Override
		protected void onPostExecute(String result) {
			try{

				if(userId>0){
					finish();
				}

			}catch (NumberFormatException e){
				Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
			}


		}
		
	}
	
}
