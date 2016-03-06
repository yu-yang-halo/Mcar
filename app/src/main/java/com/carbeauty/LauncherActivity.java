package com.carbeauty;

import com.carbeauty.userlogic.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LauncherActivity extends Activity {
	private static final int MSG_CODE_DISMISS=101;
	private static final int ANIMATION_DURATION=3000;
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==MSG_CODE_DISMISS){
				redirectToNext();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.alpha);
		
		ImageView imageView1=(ImageView) findViewById(R.id.imageView1);
		imageView1.startAnimation(animation);
		
		dissmissDelay(ANIMATION_DURATION);
	}
	private void dissmissDelay(int delayMillis){
	    handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Message msg=new Message();
				msg.what=MSG_CODE_DISMISS;
				handler.sendMessage(msg);
			}
		}, delayMillis)	;
	}
	private void redirectToNext(){
		Intent intent=new Intent();
	   	intent.setClass(LauncherActivity.this,LoginActivity.class);
	   	startActivity(intent);
	   	finish();
	}
}
