package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.service.bean.DecorationInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class DecorationDelAdapter extends BaseAdapter {
    List<DecorationInfo> decorationInfos;
    Context ctx;
    MyHandlerCallback myHandlerCallback;
    public void setMyHandlerCallback(MyHandlerCallback myHandlerCallback) {
        this.myHandlerCallback=myHandlerCallback;
    }

    public DecorationDelAdapter(List<DecorationInfo> decorationInfos, Context ctx){
        this.decorationInfos=decorationInfos;
        this.ctx=ctx;
    }

    public List<DecorationInfo> getDecorationInfos() {
        return decorationInfos;
    }

    @Override
    public int getCount() {
        if(decorationInfos!=null){
            return decorationInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (decorationInfos==null){
            return null;
        }
        return decorationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_oil_deco_del,null);
        }
        TextView itemName= (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice= (TextView) convertView.findViewById(R.id.itemPrice);
        Button itemBtn= (Button) convertView.findViewById(R.id.itemBtn);
        itemName.setText(decorationInfos.get(position).getName());
        itemPrice.setText(decorationInfos.get(position).getPrice()+"元");

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorationInfos.remove(decorationInfos.get(position));
                myHandlerCallback.clickAfter();
            }
        });



        return convertView;
    }
}
