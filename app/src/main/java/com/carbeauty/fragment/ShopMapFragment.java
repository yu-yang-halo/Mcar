package com.carbeauty.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;

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
                    .zoom(18)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);

        }
        return v;

    }
    private void addPointToMap(double lgt,double lat){
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lgt);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);
    }

    private void initMapData(){
       List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();

        for (ShopInfo shopInfo: shopInfos){
            addPointToMap(shopInfo.getLongitude(),shopInfo.getLatitude());
        }

    }


}