package com.carbeauty;

import android.app.Application;
import android.app.Notification;

import com.baidu.mapapi.SDKInitializer;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        setStyleBasic();
    }
    /**
     *设置通知提示方式 - 基础属性
     */
    private void setStyleBasic(){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);

    }

}
