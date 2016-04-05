package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.alertDialog.WindowUtils;
import com.carbeauty.cache.ContentBox;

import java.util.List;

import cn.service.bean.CarInfo;
import cn.service.bean.OrderStateType;

/**
 * Created by Administrator on 2016/3/9.
 */
public class OrderTimeAdapter extends BaseAdapter {
    List<OrderStateType> orderStateTypes;
    Context ctx;
    int BUTTON_WIDTH=50;
    public OrderTimeAdapter(List<OrderStateType> orderStateTypes, Context ctx){
        this.orderStateTypes=orderStateTypes;
        this.ctx=ctx;
        ContentBox.loadString(ctx, ContentBox.KEY_ORDER_TIME, null);

        int screenWindth=WindowUtils.getScreenWidth(ctx);
        BUTTON_WIDTH=(screenWindth-60)/6;


    }

    public void setOrderStateTypes(List<OrderStateType> orderStateTypes) {
        this.orderStateTypes = orderStateTypes;
    }

    @Override
    public int getCount() {
        if(orderStateTypes!=null){
            return orderStateTypes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (orderStateTypes==null){
            return null;
        }
        return orderStateTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_time,null);
        }
        final Button timeBtn= (Button) convertView.findViewById(R.id.timeBtn);

        ViewGroup.LayoutParams layoutParams=timeBtn.getLayoutParams();
        layoutParams.width=BUTTON_WIDTH;
        timeBtn.setLayoutParams(layoutParams);

        timeBtn.setText(orderStateTypes.get(position).getOrderTime());

        if(orderStateTypes.get(position).isFull()){
            timeBtn.setEnabled(false);
        }else {
            timeBtn.setEnabled(true);
        }
        String mSelecterTime=ContentBox.getValueString(ctx,ContentBox.KEY_ORDER_TIME,null);
        if(mSelecterTime!=null&&mSelecterTime.equals(orderStateTypes.get(position).getOrderTime())){
            timeBtn.setSelected(true);
        }else{
            timeBtn.setSelected(false);
        }
        timeBtn.setTag(orderStateTypes.get(position).getOrderTime());

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    ContentBox.loadString(ctx, ContentBox.KEY_ORDER_TIME, null);
                }else{
                    ContentBox.loadString(ctx, ContentBox.KEY_ORDER_TIME,v.getTag().toString());
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }
}
