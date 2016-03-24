package com.carbeauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.web.WebBroswerActivity;

import java.util.List;

import cn.service.WSConnector;
import cn.service.bean.PromotionInfoType;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/10.
 */
public class PromotionAdapter extends BaseAdapter {
    List<PromotionInfoType> promotionInfoTypes;
    Context ctx;
    public PromotionAdapter(List<PromotionInfoType> promotionInfoTypes,Context ctx){
        this.promotionInfoTypes=promotionInfoTypes;
        this.ctx=ctx;
    }
    @Override
    public int getCount() {
        if(promotionInfoTypes==null){
            return 0;
        }
        return promotionInfoTypes.size();
    }

    @Override
    public Object getItem(int position) {
        if(promotionInfoTypes==null){
            return null;
        }
        return promotionInfoTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item1,null);
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.active_01);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ctx,WebBroswerActivity.class);
                intent.putExtra("URL",promotionInfoTypes.get(position).getSrc());
                intent.putExtra("Title","活动详情");
                ctx.startActivity(intent);




            }
        });

        return convertView;
    }
}
