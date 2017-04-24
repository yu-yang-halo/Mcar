package com.carbeauty.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.carbeauty.CommonUtils;
import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.adapter.ShopInfoAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.github.florent37.viewanimator.ViewAnimator;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Iterator;
import java.util.List;

import cn.service.RegType;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/6.
 * 门店列表---地图模式和一般模式
 */
public class ShopFragment extends Fragment implements MainActivity.IShowModeListenser {
    MapView bmapView;
    BaiduMap mBaiduMap;
    MainActivity mainActivity;
    ListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity());

        View v=inflater.inflate(R.layout.fr_shop, null);

        bmapView= (MapView) v.findViewById(R.id.bmapView);
        listView=(ListView)v.findViewById(R.id.listView);



        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        String lgtlatStr=ContentBox.getValueString(getActivity(), ContentBox.KEY_LONG_LAT,null);

        updateMapStatus(lgtlatStr);

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
                    .zoom(13)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
        mainActivity.setiShowModeListenser(this);
    }

    @Override
    public void onStart() {
        super.onStart();

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
        bundle.putString("desc", shopInfo.getDesc());
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(shopInfo.getName())
                .extraInfo(bundle)
                .icon(bitmap)
                .animateType(MarkerOptions.MarkerAnimateType.grow);;



//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                View popupView=LayoutInflater.from(getActivity()).inflate(R.layout.popup_dialog,null);

                final TextView nameText= (TextView) popupView.findViewById(R.id.nameTextView);

                final RelativeLayout dialogView= (RelativeLayout) popupView.findViewById(R.id.dialogView);




                nameText.setText(marker.getTitle()
                        + "\n"
                        + marker.getExtraInfo().getString("desc")
                        + "(点击绑定)");

                nameText.measure(0, 0);


                dialogView.setLayoutParams(new RelativeLayout.LayoutParams(nameText.getMeasuredWidth()+40, nameText.getMeasuredHeight()+60));



                nameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        marker.setIcon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.maker2));
                        new ShopInfosTask(marker.getExtraInfo().getInt("shopId")).execute();
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
    private void initMapData(int shopId){
        List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();

        if(shopInfos==null){
            return;
        }
        mBaiduMap.clear();
        for (ShopInfo shopInfo: shopInfos){
            int cityId=ContentBox.getValueInt(getActivity(),ContentBox.KEY_CITY_ID,-1);

            if(cityId!=shopInfo.getCityId()){
                continue;
            }

            updateMapStatus(shopInfo.getLongitude()+":"+shopInfo.getLatitude());

            addPointToMap(shopInfo,(shopId==shopInfo.getShopId()));
        }

    }

    private List<ShopInfo> filterShopInfoList(List<ShopInfo> shopInfos){

        int cityId=ContentBox.getValueInt(getActivity(), ContentBox.KEY_CITY_ID, -1);
        Iterator<ShopInfo> iterator=shopInfos.iterator();
        while (iterator.hasNext()){
            ShopInfo shopInfo=iterator.next();
            if(cityId!=shopInfo.getCityId()){
                iterator.remove();
            }
        }
        return shopInfos;
    }

    private void initListView(int selectShopId){
        List<ShopInfo> shopInfos=IDataHandler.getInstance().getShopInfos();
        ShopInfoAdapter shopInfoAdapter=new ShopInfoAdapter(shopInfos,getActivity(),mainActivity.getLocation());
        shopInfoAdapter.setSelectShopId(selectShopId);
        listView.setAdapter(shopInfoAdapter);
    }

    @Override
    public void onShowMode(int mode) {
        new ShopInfosTask(-100).execute();
        if(mode==0){
            listView.setVisibility(View.VISIBLE);
            bmapView.setVisibility(View.GONE);
            ViewAnimator.animate(listView).fadeIn().duration(600).start();
        }else{
            listView.setVisibility(View.GONE);
            bmapView.setVisibility(View.VISIBLE);
            ViewAnimator.animate(bmapView).fadeIn().duration(600).start();
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
                    userInfo.setType(RegType.REGULAR_USER_TYPE.getVal());
                    WSConnector.getInstance().updUser(userInfo);
                    String loginName=WSConnector.getInstance().getUserMap().get("loginName");
                    String password=WSConnector.getInstance().getUserMap().get("password");
                    WSConnector.getInstance().appUserLogin(loginName,password, -1, "android", false);
                }

                shopInfos=WSConnector.getInstance().getShopList();
                shopInfos=filterShopInfoList(shopInfos);

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
            }

            if(s==null){

                if(shopID>0){
                    initMapData(shopID);
                    initListView(shopID);
                    ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,shopID);
                }else{

                    int _mshopId=ContentBox.getValueInt(getActivity(),ContentBox.KEY_SHOP_ID,-2);
                    if(_mshopId==-1){
                        ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,-1);
                        userInfo.setShopId(-1);
                    }else{
                        ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,userInfo.getShopId());
                    }
                    initMapData(userInfo.getShopId());
                    initListView(userInfo.getShopId());

                }

            }else {

                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            new ShopInfosTask(-100).execute();
        }

    }
}

