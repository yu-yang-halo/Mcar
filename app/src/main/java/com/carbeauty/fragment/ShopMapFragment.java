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

import cn.service.WSConnector;
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
//        String lgtlatStr=ContentBox.getValueString(getActivity(), ContentBox.KEY_LONG_LAT, null);
//        updateMapStatus(lgtlatStr);

        return v;

    }

    private void updateMapStatus(String lgtlatStr){
        if(lgtlatStr!=null){
            double lgt=Double.parseDouble(lgtlatStr.split(":")[0]);
            double lat=Double.parseDouble(lgtlatStr.split(":")[1]);
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
    }

    private void addPointToMap(ShopInfo shopInfo){
        //定义Maker坐标点
        LatLng point = new LatLng(shopInfo.getLatitude(), shopInfo.getLongitude());
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker);
        Bundle bundle=new Bundle();
        bundle.putString("URL", WSConnector.getPanoramaURL(shopInfo.getShopId()+"",shopInfo.getPanorama()));
        bundle.putString("NAME",shopInfo.getName());
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(shopInfo.getName())
                .extraInfo(bundle)
                .icon(bitmap);
        showDialog(bundle,point);

//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                return false;
            }
        });

    }
    private void showDialog(Bundle bundle,LatLng position){
        //创建InfoWindow展示的view

        View dialogMap=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_map,null);
        final String url=bundle.getString("URL");
        final String name=bundle.getString("NAME");


//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(dialogMap, position, -97);
//显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
        Button closeBtn= (Button) dialogMap.findViewById(R.id.closeBtn);
        TextView contentView= (TextView) dialogMap.findViewById(R.id.contentView);
        Button photoRotoa= (Button) dialogMap.findViewById(R.id.photoRotoa);

        contentView.setText(name);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mBaiduMap.hideInfoWindow();
            }
        });
        photoRotoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PanoramaActivity.class);
                intent.putExtra("Title",name);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });
    }

    private void initMapData(){
       List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();

        if(shopInfos==null){
            return;
        }
        int shopId = ContentBox.getValueInt(getActivity(), ContentBox.KEY_SHOP_ID, -1);
        for (ShopInfo shopInfo: shopInfos){
            if(shopId==shopInfo.getShopId()){
                updateMapStatus(shopInfo.getLongitude()+":"+shopInfo.getLatitude());
                addPointToMap(shopInfo);
            }

        }

    }


}
