package com.carbeauty;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.carbeauty.alertDialog.DialogManagerUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.ContentCacheUtils;
import com.carbeauty.camera.MyCamera;
import com.carbeauty.userlogic.LoginActivity;
import com.pgyersdk.crash.PgyCrashManager;
import com.tutk.IOTC.Camera;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.service.ErrorCode;
import cn.service.IWSErrorCodeListener;
import cn.service.MD5Generator;
import cn.service.WSConnector;
import cn.service.WSException;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {
    private final static int CWJ_HEAP_SIZE = 6* 1024* 1024 ;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BDLocation bdLocation;

    private Handler applicationHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ErrorCode errorCode= ErrorCode.get(msg.what);

            if(errorCode==ErrorCode.PERMISSION_DENY){

                final String[] usernamePassArr= ContentCacheUtils.getUsernamePass(getApplicationContext());
                if(usernamePassArr!=null&&usernamePassArr.length==2){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WSConnector.getInstance().appUserLogin(usernamePassArr[0],
                                        MD5Generator.reverseMD5Value(usernamePassArr[1]), -1, "android", false);

                            } catch (WSException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
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

        enableJPUSH();
        enablePayer();

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
                MyActivityManager.getInstance().setRunBackground(false);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (MyActivityManager.getInstance().getCurrentActivity() == activity) {
                    MyActivityManager.getInstance().setRunBackground(true);
                }
                sendMessage(activity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println(e.getCause());
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        });

    }






    private void enableJPUSH(){
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        setStyleBasic();
    }
    private void enablePayer(){
        PgyCrashManager.register(this);
    }
    private void disablePayer(){
        PgyCrashManager.unregister();
    }


    /**
     *设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.appicon;
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

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    private void setMyLocation(BDLocation location){
        this.bdLocation=location;
    }
    public BDLocation getMyLocation(){
       return bdLocation;
    }



    List<ActivityFinishCallbacks> activityFinishCallbackses;

    public void addCallbacksToApplication(ActivityFinishCallbacks callbacks){
        if(activityFinishCallbackses==null){
            activityFinishCallbackses=new ArrayList<ActivityFinishCallbacks>();
        }
        activityFinishCallbackses.add(callbacks);
    }
    public void resetActivityCallbacks(ActivityFinishCallbacks callbacks){
        activityFinishCallbackses=null;
    }

    private void sendMessage(Activity activity){
        if(activityFinishCallbackses!=null&&activityFinishCallbackses.size()>0){
            for (ActivityFinishCallbacks callbacks:activityFinishCallbackses){
                if(callbacks!=null){
                    callbacks.onActivityClose(activity);
                }
            }
            activityFinishCallbackses=null;
        }
    }
    public interface  ActivityFinishCallbacks{
        public void onActivityClose(Activity activity);
    }

}
