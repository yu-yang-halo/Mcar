package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;

import java.util.List;

import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MetalInfoDelAdapter extends BaseAdapter {
    List<MetalplateInfo> metalplateInfos;
    Context ctx;
    MyHandlerCallback myHandlerCallback;
    public void setMyHandlerCallback(MyHandlerCallback myHandlerCallback) {
        this.myHandlerCallback=myHandlerCallback;
    }
    public MetalInfoDelAdapter(List<MetalplateInfo> metalplateInfos, Context ctx){
        this.metalplateInfos=metalplateInfos;
        this.ctx=ctx;
    }

    public List<MetalplateInfo> getMetalplateInfos() {
       return metalplateInfos;
    }

    @Override
    public int getCount() {
        if(metalplateInfos!=null){
            return metalplateInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (metalplateInfos==null){
            return null;
        }
        return metalplateInfos.get(position);
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
        itemName.setText(metalplateInfos.get(position).getName());
        itemPrice.setText(metalplateInfos.get(position).getPrice()+"元"
                +" "+metalplateInfos.get(position).getNumber());

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metalplateInfos.remove(metalplateInfos.get(position));
                myHandlerCallback.clickAfter();
            }
        });



        return convertView;
    }
}
