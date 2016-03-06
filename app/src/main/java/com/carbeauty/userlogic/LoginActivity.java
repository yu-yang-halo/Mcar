package com.carbeauty.userlogic;

import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.cache.ContentCacheUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.service.MD5Generator;
import cn.service.RegType;
import cn.service.Util;
import cn.service.WSConnector;
import cn.service.WSException;

public class LoginActivity extends Activity {
	ActionBar mActionbar;
	TextView tvTitle;
	EditText usernameEdit;
	EditText passwordEdit;
	Button registerBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initCustomActionBar();
		
		usernameEdit=(EditText) findViewById(R.id.usernameEdit);
		passwordEdit=(EditText) findViewById(R.id.passwordEdit);
		Button loginBtn=(Button) findViewById(R.id.loginBtn);
		
		String[] usernamePassArr=ContentCacheUtils.getUsernamePass(this);
		if(usernamePassArr!=null&&usernamePassArr.length==2){
			if(usernamePassArr[0]!=null){
				usernameEdit.setText(usernamePassArr[0]);
			}
			if(usernamePassArr[1]!=null){
				passwordEdit.setText(usernamePassArr[1]);
			}
		}
		
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username=usernameEdit.getText().toString();
				String password=passwordEdit.getText().toString();
				if(Util.isEmpty(username)||Util.isEmpty(password)){
					Toast.makeText(getApplicationContext(), "用户名和密码不能为空",Toast.LENGTH_SHORT).show();
				}else{
					new LoginTask(getApplicationContext(), username, password).execute();
				}
			}
		});
		
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
		registerBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
		Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
		leftBtn.setVisibility(View.GONE);
		tvTitle.setText("用户登录");
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				
			}
		});
		
		return true;
	}
	class LoginTask extends AsyncTask<String, String, String>{
		String loginName;
		String password;
		Context ctx;
		
		LoginTask(Context ctx, String loginName, String password){
			this.loginName=loginName;
			this.password=password;
			this.ctx=ctx;
		}
		@Override
		protected String doInBackground(String... params) {

//			try {
//				WSConnector.getInstance().appUserLogin(loginName, MD5Generator.reverseMD5Value(password), -1, "android", false);
//			} catch (WSException e) {
//				return e.getErrorMsg();
//			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				ContentCacheUtils.cacheUsernamePass(ctx, loginName, password);
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
