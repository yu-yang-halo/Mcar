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
import cn.service.bean.CityInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class CityAdapter extends BaseAdapter {
    List<CityInfo> cityInfos;
    Context ctx;
    public CityAdapter(List<CityInfo> cityInfos, Context ctx){
        this.cityInfos=cityInfos;
        this.ctx=ctx;
    }

    public void setCityInfos(List<CityInfo> cityInfos) {
        this.cityInfos = cityInfos;
    }

    @Override
    public int getCount() {
        if(cityInfos!=null){
            return cityInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (cityInfos==null){
            return null;
        }
        return cityInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item4,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.title);
        titleView.setText(cityInfos.get(position).getName());


        return convertView;
    }
}
