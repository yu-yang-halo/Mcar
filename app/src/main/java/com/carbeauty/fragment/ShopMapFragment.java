package com.carbeauty.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.web.PanoramaActivity;

import java.util.List;

import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ShopMapFragment extends Fragment {
    MapView bmapView;
    BaiduMap mBaiduMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View v= inflater.inflate(R.layout.fr_shoplook, null);
        bmapView= (MapView) v.findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
         //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        initMapData();
        String lgtlatStr=ContentBox.getValueString(getActivity(), ContentBox.KEY_LONG_LAT,null);
        if(lgtlatStr!=null){
            double lgt=Double.parseDouble(lgtlatStr.split(":")[0]);
            double lat=Double.parseDouble(lgtlatStr.split(":")[1]);
            //设定中心点坐标

            LatLng cenpt = new LatLng(lat,lgt);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(10)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);

        }
        return v;

    }
    private void addPointToMap(double lgt,double lat,String shopName){
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lgt);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(shopName)
                .icon(bitmap);

//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //创建InfoWindow展示的view

                View dialogMap=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_map,null);


//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(dialogMap, marker.getPosition(), -97);
//显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                Button closeBtn= (Button) dialogMap.findViewById(R.id.closeBtn);
                TextView contentView= (TextView) dialogMap.findViewById(R.id.contentView);
                Button photoRotoa= (Button) dialogMap.findViewById(R.id.photoRotoa);

                contentView.setText( marker.getTitle());
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
                    }
                });
                photoRotoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), PanoramaActivity.class);
                        intent.putExtra("Title","门店全景");
                        startActivity(intent);
                    }
                });
                return false;
            }
        });

    }

    private void initMapData(){
       List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();

        if(shopInfos==null){
            return;
        }
        for (ShopInfo shopInfo: shopInfos){
            addPointToMap(shopInfo.getLongitude(),shopInfo.getLatitude(),shopInfo.getName());
        }

    }


}
