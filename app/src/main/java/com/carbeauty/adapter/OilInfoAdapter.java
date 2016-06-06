package com.carbeauty.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carbeauty.DensityUtil;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.bean.DecorationInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class OilInfoAdapter extends BaseAdapter {
    List<OilInfo> oilInfos;
    Context ctx;
    MyHandlerCallback myHandlerCallback;


    private List<OilInfo> oilInfoSet=new ArrayList<OilInfo>();
    public List<OilInfo> getOilCollections(){
        return oilInfoSet;
    }
    public void setMyHandlerCallback(MyHandlerCallback myHandlerCallback) {
        this.myHandlerCallback=myHandlerCallback;
    }
    public OilInfoAdapter(List<OilInfo> oilInfos, Context ctx){
        this.oilInfos=oilInfos;
        this.ctx=ctx;
    }

    public void setOilInfos(List<OilInfo> oilInfos) {
        this.oilInfos = oilInfos;
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
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_oil_deco,null);
        }
        TextView itemName= (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice= (TextView) convertView.findViewById(R.id.itemPrice);

        Button itemBtn= (Button) convertView.findViewById(R.id.itemBtn);
        itemName.setText(oilInfos.get(position).getName());
        itemPrice.setText(oilInfos.get(position).getPrice()+"元");




        OilInfo oilInfo=oilInfos.get(position);
        String src=oilInfo.getSrc();



        if(oilInfoSet.contains(oilInfos.get(position))){
            itemBtn.setSelected(true);
        }else {
            itemBtn.setSelected(false);
        }
        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    oilInfoSet.remove(oilInfos.get(position));
                } else {
                    v.setSelected(true);
                    oilInfoSet.add(oilInfos.get(position));
                }
                myHandlerCallback.clickAfter();
            }
        });


        return convertView;
    }
    public void addOneItem(int pos){
        if(pos>=0&&pos<oilInfos.size()){
            if(!oilInfoSet.contains(oilInfos.get(pos))){
                oilInfoSet.add(oilInfos.get(pos));
            }
            notifyDataSetChanged();
        }

    }



}
