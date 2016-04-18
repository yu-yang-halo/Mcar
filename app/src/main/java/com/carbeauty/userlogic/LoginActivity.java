package com.carbeauty.userlogic;

import com.carbeauty.FirVersion;
import com.carbeauty.MainActivity;
import com.carbeauty.MyActivityManager;
import com.carbeauty.R;
import com.carbeauty.cache.ContentCacheUtils;
import com.carbeauty.dialog.AcCarInfoDialog;
import com.carbeauty.dialog.IpConfigDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.service.MD5Generator;
import cn.service.RegType;
import cn.service.Util;
import cn.service.WSConnector;
import cn.service.WSException;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

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
					Toast.makeText(LoginActivity.this, "用户名和密码不能为空",Toast.LENGTH_SHORT).show();
				}else{
					new LoginTask(LoginActivity.this, username, password).execute();
				}
			}
		});


		Button ipconfigBtn= (Button) findViewById(R.id.ipconfig);
		ipconfigBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, IpConfigDialog.class);
				startActivity(intent);

			}
		});


		FIR.checkForUpdateInFIR("11044c8cac8c136a31cb0b8ab5bd5162", new VersionCheckCallback() {
			@Override
			public void onSuccess(String versionJson) {
				Gson gson = new Gson();
				final FirVersion firVersion = gson.fromJson(versionJson, FirVersion.class);

				PackageInfo packageInfo = getVersionInfo(getApplicationContext());

				Log.i("fir", "check from fir.im success! "
						+ "\n" + firVersion + " packageInfo " + packageInfo.versionName + " " + packageInfo.versionCode);

				if (firVersion.getBuild().equals(packageInfo.versionCode + "")
						&& firVersion.getVersionShort().equals(packageInfo.versionName)) {
					Log.i("fir", "版本号一致,无需更新");
				} else {

					if (!MyActivityManager.getInstance().isRunBackground()) {
						new AlertDialog.Builder(MyActivityManager.getInstance().getCurrentActivity(), AlertDialog.THEME_HOLO_LIGHT)
								.setTitle("提示")
								.setMessage("发现新版本~")
								.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {


										Uri uri = Uri.parse(firVersion.getInstall_url());

										Intent intent = new Intent(Intent.ACTION_VIEW, uri);
										startActivity(intent);


									}
								})
								.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {

									}
								}).show();
					}


				}
			}

			@Override
			public void onFail(Exception exception) {
				Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

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
		KProgressHUD progressHUD;
		LoginTask(Context ctx, String loginName, String password){
			this.loginName=loginName;
			this.password=password;
			this.ctx=ctx;
		}
		@Override
		protected String doInBackground(String... params) {

			try {
				WSConnector.getInstance().appUserLogin(loginName, MD5Generator.reverseMD5Value(password), -1, "android", false);
			} catch (WSException e) {
				return e.getErrorMsg();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressHUD=KProgressHUD.create(ctx)
					.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
					.setLabel("登录中...")
					.setAnimationSpeed(1)
					.setDimAmount(0.3f)
					.show();
		}

		@Override
		protected void onPostExecute(String result) {
			progressHUD.dismiss();
			if(result==null){
				ContentCacheUtils.cacheUsernamePass(ctx, loginName, password);
				String typeStr=WSConnector.getInstance().getUserMap().get("type");
				Set<String> tags=new HashSet<String>();
				tags.add(typeStr);
				JPushInterface.setAliasAndTags(getApplicationContext(), loginName, tags, new TagAliasCallback() {
					@Override
					public void gotResult(int i, String s, Set<String> set) {

					}
				});
				Intent intent=new Intent();
				if("3".equals(typeStr)){
					intent.setClass(LoginActivity.this, MainActivity.class);
				}else{
					intent.setClass(LoginActivity.this, ConsoleActivity.class);
				}
				startActivity(intent);
				finish();






			}else{
				Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
			}
		}
		
	}


	public  PackageInfo getVersionInfo(Context ctx) {
		try {
			PackageManager manager = ctx.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			return info;
		} catch (Exception e) {
			return null;
		}
	}

}
