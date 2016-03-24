package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carbeauty.R;

import java.util.ArrayList;
import java.util.List;

import com.carbeauty.Constants;
import cn.service.bean.DecorationInfo;
import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class CommonAdapter extends BaseAdapter {
    private List<DecorationInfo> decorationInfos=new ArrayList<DecorationInfo>();
    private List<OilInfo> oilInfos=new ArrayList<OilInfo>();
    private List<MetalplateInfo> metalplateInfos=new ArrayList<MetalplateInfo>();
    int acType;
    Context ctx;

    public CommonAdapter(int acType, Context ctx){
        this.ctx=ctx;
        this.acType=acType;
    }

    @Override
    public int getCount() {
        if(acType== Constants.AC_TYPE_OIL){
            if(oilInfos!=null){
                return oilInfos.size();
            }
        }else if(acType==Constants.AC_TYPE_WASH){
            if(decorationInfos!=null){
                return decorationInfos.size();
            }
        }else if(acType==Constants.AC_TYPE_META){
            if(metalplateInfos!=null){
                return metalplateInfos.size();
            }
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {



        if(acType== Constants.AC_TYPE_OIL){
            if(oilInfos!=null){
                return oilInfos.get(position);
            }
        }else if(acType==Constants.AC_TYPE_WASH){
            if(decorationInfos!=null){
                return decorationInfos.get(position);
            }
        }else if(acType==Constants.AC_TYPE_META){
            if(metalplateInfos!=null){
                return metalplateInfos.get(position);
            }
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
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_oil_deco_meta,null);
        }
        TextView itemName= (TextView) convertView.findViewById(R.id.itemName);
        TextView itemPrice= (TextView) convertView.findViewById(R.id.itemPrice);
        TextView itemCount= (TextView) convertView.findViewById(R.id.itemCount);

        if(acType==Constants.AC_TYPE_META){
            itemName.setText(metalplateInfos.get(position).getName());
            itemPrice.setText(metalplateInfos.get(position).getPrice() + "元");

            if(metalplateInfos.get(position).getCount()>1){
                itemCount.setText("数量:"+metalplateInfos.get(position).getCount() );
            }else{
                itemCount.setText("");
            }


        }else{
            itemCount.setText("");
            if(acType==Constants.AC_TYPE_OIL){
                itemName.setText(oilInfos.get(position).getName());
                itemPrice.setText(oilInfos.get(position).getPrice() + "元");
            }else if(acType==Constants.AC_TYPE_WASH){
                itemName.setText(decorationInfos.get(position).getName());
                itemPrice.setText(decorationInfos.get(position).getPrice() + "元");
            }
        }



        return convertView;
    }


    public List<DecorationInfo> getDecorationInfos() {
        return decorationInfos;
    }

    public void setDecorationInfos(List<DecorationInfo> decorationInfos) {
        this.decorationInfos = decorationInfos;
    }

    public List<OilInfo> getOilInfos() {
        return oilInfos;
    }

    public void setOilInfos(List<OilInfo> oilInfos) {
        this.oilInfos = oilInfos;
    }

    public List<MetalplateInfo> getMetalplateInfos() {
        return metalplateInfos;
    }

    public void setMetalplateInfos(List<MetalplateInfo> metalplateInfos) {
        this.metalplateInfos = metalplateInfos;
    }
}
