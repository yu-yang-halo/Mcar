package com.carbeauty.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.service.MD5Generator;
import cn.service.RegType;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ShopInfoAdapter extends BaseAdapter {
    List<ShopInfo> shopInfos;
    Context ctx;
    private int selectShopId=0;
    BDLocation bdLocation;
    public ShopInfoAdapter(List<ShopInfo> shopInfos,Context ctx,BDLocation bdLocation){
        this.shopInfos=shopInfos;
        this.ctx=ctx;
        this.bdLocation=bdLocation;
        this.shopInfos=filterShopInfosNear(shopInfos,bdLocation);
    }
    public void setSelectShopId(int selectShopId){
        this.selectShopId=selectShopId;
    }

    public void setShopInfos(List<ShopInfo> shopInfos) {
        this.shopInfos = shopInfos;
    }

    @Override
    public int getCount() {
        if(shopInfos!=null){
            return shopInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (shopInfos==null){
            return null;
        }
        return shopInfos.get(position);
    }

    private List<ShopInfo> filterShopInfosNear(List<ShopInfo> tmps,BDLocation location){
        if(location==null){
            return tmps;
        }
        List<ShopInfo> nearShopInfos=new ArrayList<ShopInfo>();
        for (ShopInfo shopInfo:tmps){
            LatLng latLng0=new LatLng(location.getLatitude(),location.getLongitude());
            LatLng latLng1=new LatLng(shopInfo.getLatitude(),shopInfo.getLongitude());

            int distance=(int)(DistanceUtil.getDistance(latLng0,latLng1)/1000);
            shopInfo.setKilometerDistance(distance);
            nearShopInfos.add(shopInfo);
        }



        Collections.sort(nearShopInfos, new Comparator<ShopInfo>() {
            @Override
            public int compare(ShopInfo lhs, ShopInfo rhs) {
                return lhs.getKilometerDistance()-rhs.getKilometerDistance();
            }
        });


        return nearShopInfos;

    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item3,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.title);
        TextView descriptionView= (TextView) convertView.findViewById(R.id.description);
        CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.checkBox);
        TextView distanceText= (TextView) convertView.findViewById(R.id.distanceText);
        TextView textPhone= (TextView) convertView.findViewById(R.id.textPhone);
        ImageView phoneImageView= (ImageView) convertView.findViewById(R.id.imageView13);

        if(selectShopId==shopInfos.get(position).getShopId()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        titleView.setText(shopInfos.get(position).getName());
        descriptionView.setText(shopInfos.get(position).getDesc());

        String phoneStr=shopInfos.get(position).getPhone();
        if(phoneStr!=null&&!phoneStr.trim().equals("")){
            textPhone.setText(phoneStr);
            phoneImageView.setVisibility(View.VISIBLE);
        }else{
            textPhone.setText("");
            phoneImageView.setVisibility(View.GONE);
        }



        distanceText.setText(shopInfos.get(position).getKilometerDistance()+"公里");




        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BindShopTask(shopInfos.get(position).getShopId()).execute();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BindShopTask(shopInfos.get(position).getShopId()).execute();
            }
        });

        return convertView;
    }

    class BindShopTask extends AsyncTask<String,String,String>{
        int shopId;
        KProgressHUD progressHUD;
        BindShopTask(int shopId){
            this.shopId=shopId;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                UserInfo userInfo=WSConnector.getInstance().getUserInfoById();
                if(shopId!=userInfo.getShopId()){
                    userInfo.setShopId(shopId);
                    WSConnector.getInstance().updUser(RegType.REGULAR_USER_TYPE.getVal(), userInfo);
                    String loginName=WSConnector.getInstance().getUserMap().get("loginName");
                    String password=WSConnector.getInstance().getUserMap().get("password");
                    WSConnector.getInstance().appUserLogin(loginName,password, -1, "android", false);
                }else {
                    return "已经选中";
                }

            } catch (WSException e) {
                return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s==null){
                setSelectShopId(shopId);
                ContentBox.loadInt(ctx, ContentBox.KEY_SHOP_ID, shopId);
             }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
             }
             notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }
    }

}
