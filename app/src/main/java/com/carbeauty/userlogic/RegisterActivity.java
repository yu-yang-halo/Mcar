package com.carbeauty.userlogic;

import com.carbeauty.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.service.RegType;
import cn.service.Util;
import cn.service.WSConnector;
import cn.service.WSException;

public class RegisterActivity extends Activity{
    EditText accountEdit;
    EditText passEdit;
    EditText repassEdit;
    Button regBtn;
    ActionBar mActionbar;
    TextView tvTitle;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.register);
    	initCustomActionBar();
    	accountEdit=(EditText) findViewById(R.id.accountEdit);
    	passEdit=(EditText) findViewById(R.id.passEdit);
    	repassEdit=(EditText) findViewById(R.id.repassEdit);
    	regBtn=(Button) findViewById(R.id.regBtn);
    	
    	regBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String account = accountEdit.getText().toString();
				String pass    = passEdit.getText().toString();
				String repass  = repassEdit.getText().toString();
				if(Util.isEmpty(account)||Util.isEmpty(pass)||Util.isEmpty(repass)){
					Toast.makeText(getApplicationContext(), "注册用户名和密码不能为空",Toast.LENGTH_SHORT).show();
				}else if(!Util.isPhoneNumber(account)){
					Toast.makeText(getApplicationContext(), "用户名请使用手机号注册",Toast.LENGTH_SHORT).show();
				}else if(!Util.isOverMinSize(pass,6)){
					Toast.makeText(getApplicationContext(), "密码位数小于6位",Toast.LENGTH_SHORT).show();
				}else if(!pass.equals(repass)){
					Toast.makeText(getApplicationContext(), "两次输入密码不一致",Toast.LENGTH_SHORT).show();
				}else{
					new RegisterUserTask(getApplicationContext(), account, pass).execute();
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
		String loginName;
		String password;
		Context ctx;
		
		RegisterUserTask(Context ctx, String loginName, String password){
			this.loginName=loginName;
			this.password=password;
			this.ctx=ctx;
		}
		@Override
		protected String doInBackground(String... params) {
			try {
				WSConnector.getInstance().createUser(loginName, password, loginName, RegType.REGULAR_USER_TYPE.getVal(), -1, null, null);
			} catch (WSException e) {
				return e.getErrorMsg();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				finish();
			}else{
				Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
