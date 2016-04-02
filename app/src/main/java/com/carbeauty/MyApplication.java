package com.carbeauty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.carbeauty.alertDialog.DialogManagerUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.camera.MyCamera;
import com.carbeauty.userlogic.LoginActivity;
import com.tutk.IOTC.Camera;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.service.ErrorCode;
import cn.service.IWSErrorCodeListener;
import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {


    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BDLocation bdLocation;

    private Handler applicationHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ErrorCode errorCode= ErrorCode.get(msg.what);
            if(errorCode==ErrorCode.PERMISSION_DENY){

                DialogManagerUtils.showMessage(MyActivityManager.getInstance().getCurrentActivity(), "提示", "登录超时，请重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MyActivityManager.getInstance().getCurrentActivity(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });




            }
        }
    };

    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        mLocationClient.unRegisterLocationListener(myListener);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush



        setStyleBasic();

        WSConnector.getInstance().setWSErrorCodeListener(new IWSErrorCodeListener() {
            @Override
            public void handleErrorCode(ErrorCode errorcode) {
                Message message = new Message();
                message.what = errorcode.getCode();
                applicationHandler.sendMessage(message);
            }
        });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }




    /**
     *设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    private void initLocation(){
        SDKInitializer.initialize(getApplicationContext());

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );
        option.setCoorType("bd09ll");
        int span=1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            ContentBox.loadString(getApplicationContext(), ContentBox.KEY_LONG_LAT,
                    location.getLongitude() + ":" + location.getLatitude());
            setMyLocation(location);
        }
    }
    private void setMyLocation(BDLocation location){
        this.bdLocation=location;
    }
    public BDLocation getMyLocation(){
       return bdLocation;
    }

}
