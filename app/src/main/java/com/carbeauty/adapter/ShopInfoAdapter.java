package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carbeauty.R;

import java.util.List;

import cn.service.bean.CarInfo;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ShopInfoAdapter extends BaseAdapter {
    List<ShopInfo> shopInfos;
    Context ctx;
    public ShopInfoAdapter(List<ShopInfo> shopInfos,Context ctx){
        this.shopInfos=shopInfos;
        this.ctx=ctx;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item3,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.title);
        TextView descriptionView= (TextView) convertView.findViewById(R.id.description);

        titleView.setText(shopInfos.get(position).getName());
        descriptionView.setText(shopInfos.get(position).getDesc());

        return convertView;
    }
}
