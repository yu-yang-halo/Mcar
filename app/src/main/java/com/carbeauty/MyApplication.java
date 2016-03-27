package com.carbeauty;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.carbeauty.cache.ContentBox;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyApplication extends Application {


    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BDLocation bdLocation;

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
