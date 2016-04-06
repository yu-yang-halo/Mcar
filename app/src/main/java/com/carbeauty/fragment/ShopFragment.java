package com.carbeauty.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import cn.service.RegType;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ShopFragment extends Fragment{
    MapView bmapView;
    BaiduMap mBaiduMap;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_shop, null);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        bmapView= (MapView) v.findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        String lgtlatStr=ContentBox.getValueString(getActivity(), ContentBox.KEY_LONG_LAT,null);
        if(lgtlatStr!=null){
            double lgt=Double.parseDouble(lgtlatStr.split(":")[0]);
            double lat=Double.parseDouble(lgtlatStr.split(":")[1]);
            //设定中心点坐标

            LatLng cenpt = new LatLng(lat,lgt);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(13)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);

        }


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ShopInfosTask(-100).execute();
    }

    private void addPointToMap(ShopInfo shopInfo,boolean selected){
        int resId=R.mipmap.maker;
        if(selected){
            resId=R.mipmap.maker2;
        }

        //定义Maker坐标点
        LatLng point = new LatLng(shopInfo.getLatitude(), shopInfo.getLongitude());
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(resId);

        final Bundle bundle=new Bundle();
        bundle.putInt("shopId",shopInfo.getShopId());
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(shopInfo.getName())
                .extraInfo(bundle)
                .icon(bitmap);



//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {


                //创建InfoWindow展示的view
                Button button = new Button(getActivity());
                button.setBackgroundResource(R.drawable.btn_popup);
                button.setText("点击选择 '" + marker.getTitle()+"' 店铺");
                button.setTextSize(10);
                button.setTextColor(getResources().getColor(R.color.white));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        marker.setIcon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.maker2));
                        new ShopInfosTask(marker.getExtraInfo().getInt("shopId")).execute();
                    }
                });
//定义用于显示该InfoWindow的坐标点

//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(button, marker.getPosition(), -90);
//显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });

    }
    private void initMapData(int shopId){
        List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();

        if(shopInfos==null){
            return;
        }
        mBaiduMap.clear();
        for (ShopInfo shopInfo: shopInfos){
            addPointToMap(shopInfo,(shopId==shopInfo.getShopId()));
        }

    }


    class ShopInfosTask extends AsyncTask<String,String,String>{

        List<ShopInfo> shopInfos;
        UserInfo userInfo;
        int shopID;
        KProgressHUD progressHUD;
        public ShopInfosTask(int shopID){
            this.shopID=shopID;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(shopID>0){
                progressHUD= KProgressHUD.create(getActivity())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setAnimationSpeed(1)
                        .setDimAmount(0.3f)
                        .setLabel("切换店铺中...")
                        .show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                userInfo=WSConnector.getInstance().getUserInfoById();
                if(shopID>0&&userInfo.getShopId()!=shopID){
                    userInfo.setShopId(shopID);
                    WSConnector.getInstance().updUser(RegType.REGULAR_USER_TYPE.getVal(), userInfo);
                    String loginName=WSConnector.getInstance().getUserMap().get("loginName");
                    String password=WSConnector.getInstance().getUserMap().get("password");
                    WSConnector.getInstance().appUserLogin(loginName,password, -1, "android", false);
                }

                shopInfos=WSConnector.getInstance().getShopList();

                System.err.println(shopInfos);
                IDataHandler.getInstance().setShopInfos(shopInfos);

            } catch (WSException e) {
               return e.getErrorMsg();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(shopID>0){
                progressHUD.dismiss();
                if(s==null){
                    mainActivity.setSelectPos(0);
                }
            }

            if(s==null){

                if(shopID>0){
                    initMapData(shopID);
                    ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,shopID);
                }else{
                    initMapData(userInfo.getShopId());
                    ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,userInfo.getShopId());
                }

            }else {

                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }

        }
    }

}

