package com.carbeauty.userlogic;

import com.carbeauty.BaseActivity;
import com.carbeauty.Constants;
import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.admin.AdminUI;
import com.carbeauty.admin.NotificationUI;
import com.carbeauty.alertDialog.DialogManagerUtils;
import com.carbeauty.cache.ContentCacheUtils;
import com.carbeauty.dialog.IpConfigDialog;
import com.carbeauty.message.MessageActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.permission.PermissionUtils;
import com.pgyersdk.update.PgyUpdateManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import cn.service.Util;
import cn.service.WSConnector;
import cn.service.WSException;

public class LoginActivity extends BaseActivity {
	ActionBar mActionbar;
	TextView tvTitle;
	EditText usernameEdit;
	EditText passwordEdit;
	Button registerBtn;

	TextView versionTxt;
	PackageInfo packageInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initCustomActionBar();

		PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
		PgyUpdateManager.register(this, "provider_car");

		versionTxt= (TextView) findViewById(R.id.versionTxt);
		
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
//					new LoginTask(LoginActivity.this, username, password).execute();
					login( username, password);
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
		packageInfo = getVersionInfo(getApplicationContext());
		versionTxt.setText("v" + packageInfo.versionName);


		String message=getIntent().getStringExtra(MessageActivity.MESSAGE_CONTENT);
        if(message!=null){
			DialogManagerUtils.showMessage(this, "新消息", message, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		}
//		PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
//			@Override
//			public void onPermissionGranted(int requestCode) {
//
//			}
//		});
//		PermissionUtils.requestPermission(this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, new PermissionUtils.PermissionGrant() {
//			@Override
//			public void onPermissionGranted(int requestCode) {
//
//			}
//		});

		PermissionUtils.requestMultiPermissions(this,new PermissionUtils.PermissionGrant() {
			@Override
			public void onPermissionGranted(int requestCode) {

			}
		});



	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected boolean initCustomActionBar() {
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
	private  void login(final String username, final String password){
		final KProgressHUD	progressHUD=KProgressHUD.create(this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("登录中...")
				.setAnimationSpeed(1)
				.setDimAmount(0.3f)
				.show();



		executorService.execute(new Runnable() {
			@Override
			public void run() {
				boolean loginYN=false;
				try {
					loginYN=WSConnector.getInstance().appUserLogin(username, MD5Generator.reverseMD5Value(password), false);
				} catch (WSException e) {

				}
				final boolean finalLoginYN = loginYN;
				mainHandler.post(new Runnable() {
					@Override
					public void run() {
						progressHUD.dismiss();
						if(finalLoginYN){
							ContentCacheUtils.cacheUsernamePass(LoginActivity.this, username, password);
							String typeStr=WSConnector.getInstance().getUserMap().get("type");
							Set<String> tags=new HashSet<String>();
							tags.add(typeStr);
							JPushInterface.setAliasAndTags(getApplicationContext(), username, tags, new TagAliasCallback() {
								@Override
								public void gotResult(int i, String s, Set<String> set) {
									System.out.println("s: "+s+" "+set);
								}
							});
							Intent intent=new Intent();

							if(typeStr.equals(String.valueOf(Constants.USER_TYPE_NOMAL))
									||typeStr.equals(String.valueOf(Constants.USER_TYPE_VIP))){
								intent.setClass(LoginActivity.this, MainActivity.class);
							}else{
								intent.setClass(LoginActivity.this, AdminUI.class);
							}
							startActivity(intent);
							finish();
						}else{
							Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});






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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PgyUpdateManager.unregister();
	}
}
