package com.carbeauty.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.kaopiz.kprogresshud.KProgressHUD;

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
    public ShopInfoAdapter(List<ShopInfo> shopInfos,Context ctx){
        this.shopInfos=shopInfos;
        this.ctx=ctx;
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

        if(selectShopId==shopInfos.get(position).getShopId()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        titleView.setText(shopInfos.get(position).getName());
        descriptionView.setText(shopInfos.get(position).getDesc());
        convertView.setOnClickListener(new View.OnClickListener() {
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
                notifyDataSetChanged();
                ContentBox.loadInt(ctx, "shopId", shopId);
             }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
             }
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
