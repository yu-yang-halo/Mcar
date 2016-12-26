package com.carbeauty.auto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.carbeauty.BaseActivity;
import com.carbeauty.MyApplication;
import com.carbeauty.R;
import com.carbeauty.web.PanoramaActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.DevInfoType;

/**
 * Created by Administrator on 2016/12/16.
 */

public class AutowashActivity extends BaseActivity {

    MapView bmapView;
    BaiduMap mBaiduMap;
    List<DevInfoType> devInfoTypeList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_autowash);

        ZXingLibrary.initDisplayOpinion(this);
        initCustomActionBar();
        titleLabel.setText("自助洗");
        rightBtn.setText("扫一扫");
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutowashActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

        bmapView= (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        MyApplication myApplication= (MyApplication) getApplicationContext();

        BDLocation bdLocation=myApplication.getMyLocation();


        updateMapStatus(bdLocation.getLongitude(),bdLocation.getLatitude());


        new GetWashDataListTask().execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==1001&&resultCode==RESULT_OK){
           //处理扫描结果（在界面上显示）
           if (null != data) {
               Bundle bundle = data.getExtras();
               if (bundle == null) {
                   return;
               }
               if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                   String result = bundle.getString(CodeUtils.RESULT_STRING);

                   DevInfoType devInfoType=findScanDevice(result);
                   if(devInfoType!=null){
                       Intent intent=new Intent(AutowashActivity.this,PayActivity.class);
                       intent.putExtra(PayActivity.KEY_DEVICE_ID,devInfoType.getId());
                       intent.putExtra(PayActivity.KEY_DEVICE_NAME,devInfoType.getName());
                       startActivity(intent);
                   }else{
                       showMessage("找不到该设备");
                   }


               } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                   showMessage("解析二维码失败");
               }
           }
       }

    }

    private void updateMapStatus(double lgt, double lat){

            //设定中心点坐标

            LatLng cenpt = new LatLng(lat,lgt);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(12)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);


    }

    private void addPointToMap(DevInfoType devInfoType){
        //定义Maker坐标点
        LatLng point = new LatLng(devInfoType.getLatitude(), devInfoType.getLongitude());
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.autowash);
        Bundle bundle=new Bundle();
        bundle.putInt("deviceId",devInfoType.getId());

//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(devInfoType.getName())
                .extraInfo(bundle)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);





//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                View popupView=LayoutInflater.from(AutowashActivity.this).inflate(R.layout.popup_dialog,null);

                final TextView nameText= (TextView) popupView.findViewById(R.id.nameTextView);

                final RelativeLayout dialogView= (RelativeLayout) popupView.findViewById(R.id.dialogView);




                nameText.setText(marker.getTitle()
                        + "\n"
                        + "(自助洗车)");

                nameText.measure(0, 0);


                dialogView.setLayoutParams(new RelativeLayout.LayoutParams(nameText.getMeasuredWidth()+40, nameText.getMeasuredHeight()+60));



                nameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(AutowashActivity.this,PayActivity.class);
                        intent.putExtra(PayActivity.KEY_DEVICE_ID,marker.getExtraInfo().getInt("deviceId"));
                        intent.putExtra(PayActivity.KEY_DEVICE_NAME,marker.getTitle());
                        startActivity(intent);



                    }
                });
//定义用于显示该InfoWindow的坐标点

//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(popupView, marker.getPosition(), -60);
//显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });

    }
    private void showDialog(final Bundle bundle, final LatLng position){
        //创建InfoWindow展示的view

        View dialogMap= LayoutInflater.from(this).inflate(R.layout.dialog_map,null);
        final String url=bundle.getString(PanoramaActivity.KEY_PANORAMA);
        final String name=bundle.getString(PanoramaActivity.KEY_TITLE);
        final String address=bundle.getString(PanoramaActivity.KEY_ADDRESS);
        final String icon=bundle.getString(PanoramaActivity.KEY_ICON);
        final int shopId=bundle.getInt(PanoramaActivity.KEY_SHOPID);

        Button closeBtn= (Button) dialogMap.findViewById(R.id.closeBtn);
        TextView contentView= (TextView) dialogMap.findViewById(R.id.contentView);
        TextView addressView= (TextView) dialogMap.findViewById(R.id.textView5);
        Button photoRotoa= (Button) dialogMap.findViewById(R.id.photoRotoa);
        Button navigateBtn= (Button) dialogMap.findViewById(R.id.navigateBtn);



        addressView.setText(address);
        contentView.setText(name);
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        photoRotoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(dialogMap, position, -97);
//显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    class GetWashDataListTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
               devInfoTypeList=WSConnector.getInstance().getDeviceList();
            } catch (WSException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            for (DevInfoType devInfoType:devInfoTypeList){
                addPointToMap(devInfoType);
            }

        }
    }

    private DevInfoType findScanDevice(String deviceIdStr){
        int devId;
        try {
            devId=Integer.parseInt(deviceIdStr);
        } catch (NumberFormatException e) {
           devId=-1;
        }


        DevInfoType devInfoType = null;
        for (DevInfoType dev:devInfoTypeList){
             if(dev.getId()==devId){
                 devInfoType=dev;
                 break;
             }
        }

        return  devInfoType;
    }

}
