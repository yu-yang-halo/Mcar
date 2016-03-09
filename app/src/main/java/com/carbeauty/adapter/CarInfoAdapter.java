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

/**
 * Created by Administrator on 2016/3/9.
 */
public class CarInfoAdapter extends BaseAdapter {
    List<CarInfo> carInfos;
    Context ctx;
    public CarInfoAdapter(List<CarInfo> carInfos,Context ctx){
        this.carInfos=carInfos;
        this.ctx=ctx;
    }

    public void setCarInfos(List<CarInfo> carInfos) {
        this.carInfos = carInfos;
    }

    @Override
    public int getCount() {
        if(carInfos!=null){
            return carInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (carInfos==null){
            return null;
        }
        return carInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item2,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.title);
        titleView.setText(carInfos.get(position).getNumber());


        return convertView;
    }
}
