package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.service.bean.CarInfo;
import cn.service.bean.DecorationInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class DecorationAdapter extends BaseAdapter {
    List<DecorationInfo> decorationInfos;
    MyHandlerCallback myHandlerCallback;
    private List<DecorationInfo> decoCollections=new ArrayList<DecorationInfo>();
    Context ctx;
    public List<DecorationInfo> getDecoCollections(){
        return decoCollections;
    }
    public void setMyHandlerCallback(MyHandlerCallback myHandlerCallback) {
        this.myHandlerCallback=myHandlerCallback;
    }
    public DecorationAdapter(List<DecorationInfo> decorationInfos, Context ctx){
        this.decorationInfos=decorationInfos;
        this.ctx=ctx;
    }

    public void setDecorationInfos(List<DecorationInfo> decorationInfos) {
        this.decorationInfos = decorationInfos;
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
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_oil_deco,null);
        }
        TextView itemName= (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice= (TextView) convertView.findViewById(R.id.itemPrice);
        Button itemBtn= (Button) convertView.findViewById(R.id.itemBtn);
        itemName.setText(decorationInfos.get(position).getName());
        itemPrice.setText(decorationInfos.get(position).getPrice() + "元");

        if(decoCollections.contains(decorationInfos.get(position))){
            itemBtn.setSelected(true);
        }else {
            itemBtn.setSelected(false);
        }

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    v.setSelected(false);
                    decoCollections.remove(decorationInfos.get(position));
                }else {
                    v.setSelected(true);
                    decoCollections.add(decorationInfos.get(position));
                }
                myHandlerCallback.clickAfter();
            }
        });



        return convertView;
    }
}
