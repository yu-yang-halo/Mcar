package com.example.jpushdemo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.carbeauty.MyActivityManager;
import com.carbeauty.alertDialog.DialogManagerUtils;
import com.carbeauty.cache.MessageManager;
import com.carbeauty.message.MessageActivity;
import com.carbeauty.userlogic.ICallData;
import com.carbeauty.userlogic.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(final Context context, final Intent intent) {
        Bundle bundle = intent.getExtras();
		int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		int orderId  =-1;
		int orderType=-1;
		try {
			String extra_data=bundle.getString(JPushInterface.EXTRA_EXTRA);
			if(extra_data!=null&&!extra_data.isEmpty()){
				JSONObject json = new JSONObject(extra_data);
				orderId=json.getInt("orderId");
				orderType=json.getInt("orderType");
				Log.d(TAG, "[MyReceiver] orderId - " +orderId+ ", orderType: " + orderType);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String alertMessage=intent.getStringExtra(JPushInterface.EXTRA_ALERT);
		MessageManager.JPMessage jpMessage=new MessageManager.JPMessage(alertMessage);
		jpMessage.setOrderId(orderId);
		jpMessage.setOrderType(orderType);
		MessageManager.getInstance().addJPMessage(context,jpMessage);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");

            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			if(!MyActivityManager.getInstance().isRunBackground()){
				DialogManagerUtils.showMessage(MyActivityManager.getInstance().getCurrentActivity(), "新消息", intent.getStringExtra(JPushInterface.EXTRA_ALERT), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				notificationToUI(context,notifactionId);

			}

        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			if(!MyActivityManager.getInstance().isRunBackground()){
				DialogManagerUtils.showMessage(MyActivityManager.getInstance().getCurrentActivity(), "新消息", intent.getStringExtra(JPushInterface.EXTRA_ALERT), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				notificationToUI(context,notifactionId);

			}else{
				//打开自定义的Activity
				Intent i = new Intent(context, LoginActivity.class);
				i.putExtra(MessageActivity.MESSAGE_CONTENT,intent.getStringExtra(JPushInterface.EXTRA_ALERT));
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
			//DialogManagerUtils.showMyToast(context, intent.getStringExtra(JPushInterface.EXTRA_ALERT));

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}
	private void notificationToUI(Context context,int notifactionId){
		Activity activity=MyActivityManager.getInstance().getCurrentActivity();
		Log.e(TAG, "[activity]  " + activity+" context "+context);
		if(ICallData.class.isAssignableFrom(activity.getClass()) ){
			ICallData iCallData= (ICallData) activity;
			iCallData.onDataRefresh();
			JPushInterface.clearNotificationById(context,notifactionId);
		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));

					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
}
