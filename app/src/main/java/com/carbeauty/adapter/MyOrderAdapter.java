package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.order.MyOrderActivity;

import java.util.List;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MyOrderAdapter  extends BaseAdapter {
    Context ctx;
    List<MyOrderActivity.CommonOrderBean> commonOrderBeans;
    public MyOrderAdapter(Context ctx, List<MyOrderActivity.CommonOrderBean> commonOrderBeans){
        this.ctx=ctx;
        this.commonOrderBeans=commonOrderBeans;
    }
    @Override
    public int getCount() {
        if(commonOrderBeans!=null){
            return commonOrderBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(commonOrderBeans!=null){
            return commonOrderBeans.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_my_order,null);
        }
        TextView titleNameTxt= (TextView) convertView.findViewById(R.id.titleNameTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);
        TextView statusTxt= (TextView) convertView.findViewById(R.id.statusTxt);
        TextView priceTxt= (TextView) convertView.findViewById(R.id.priceTxt);
        TextView createTimeTxt= (TextView) convertView.findViewById(R.id.createTimeTxt);
        ImageView descImageView= (ImageView) convertView.findViewById(R.id.descImageView);
        Button delBtn= (Button) convertView.findViewById(R.id.delBtn);

        titleNameTxt.setText(commonOrderBeans.get(position).getTitle());

        if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_DECO){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_8);
        }else if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_OIL){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_6);
        }else if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_7);
        }


        String status="";
        if(commonOrderBeans.get(position).getState()== Constants.STATE_ORDER_FINISHED){
            status="已完成";
        }else if(commonOrderBeans.get(position).getState()== Constants.STATE_ORDER_UNFINISHED){
            status="正在处理";
        }else if(commonOrderBeans.get(position).getState()== Constants.STATE_ORDER_WAIT){
            status="等待客服确认";
        }
        statusTxt.setText(status);

        descTxt.setText(commonOrderBeans.get(position).getDesc());

        priceTxt.setText("总价:￥"+commonOrderBeans.get(position).getPrice());
        createTimeTxt.setText("创建时间:"+ TimeUtils.getShowTime(commonOrderBeans.get(position).getCreateTime()));
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "" + commonOrderBeans.get(position).getItemType()+":"+commonOrderBeans.get(position).getId(),
                        Toast.LENGTH_SHORT).show();


            }
        });

        return convertView;
    }
}
