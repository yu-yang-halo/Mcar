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

import cn.service.bean.DecorationInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class OilInfoDelAdapter extends BaseAdapter {
    List<OilInfo> oilInfos;
    Context ctx;
    MyHandlerCallback myHandlerCallback;
    public void setMyHandlerCallback(MyHandlerCallback myHandlerCallback) {
        this.myHandlerCallback=myHandlerCallback;
    }
    public OilInfoDelAdapter(List<OilInfo> oilInfos, Context ctx){
        this.oilInfos=oilInfos;
        this.ctx=ctx;
    }

    public List<OilInfo> getOilInfos() {
       return oilInfos;
    }

    @Override
    public int getCount() {
        if(oilInfos!=null){
            return oilInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (oilInfos==null){
            return null;
        }
        return oilInfos.get(position);
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
        itemName.setText(oilInfos.get(position).getName());
        itemPrice.setText(oilInfos.get(position).getPrice()+"元");

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oilInfos.remove(oilInfos.get(position));
                myHandlerCallback.clickAfter();
            }
        });



        return convertView;
    }
}
