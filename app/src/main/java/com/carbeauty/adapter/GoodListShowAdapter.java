package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.TimeUtils;

import java.util.List;

import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsOrderListType;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GoodListShowAdapter extends BaseAdapter {
    Context ctx;
    List<GoodsOrderListType> goodsOrderListTypes;
    List<GoodInfo> goodInfos;
    public GoodListShowAdapter(Context ctx, List<GoodsOrderListType> goodsOrderListTypes,
                               List<GoodInfo> goodInfos){
        this.ctx=ctx;
        this.goodsOrderListTypes=goodsOrderListTypes;
        this.goodInfos=goodInfos;
    }
    @Override
    public int getCount() {
        if(goodsOrderListTypes!=null){
            return goodsOrderListTypes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(goodsOrderListTypes!=null){
            return goodsOrderListTypes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_good_order,null);
        }
        TextView titleNameTxt= (TextView) convertView.findViewById(R.id.titleNameTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);
        TextView statusTxt= (TextView) convertView.findViewById(R.id.statusTxt);
        TextView priceTxt= (TextView) convertView.findViewById(R.id.priceTxt);
        TextView createTimeTxt= (TextView) convertView.findViewById(R.id.createTimeTxt);
        ImageView descImageView= (ImageView) convertView.findViewById(R.id.descImageView);
        Button delBtn= (Button) convertView.findViewById(R.id.delBtn);

        titleNameTxt.setText("");
        statusTxt.setText("已完成");
        descTxt.setText(getContent(goodsOrderListTypes.get(position).getGoodsInfo()));
        priceTxt.setText("总价:￥"+goodsOrderListTypes.get(position).getPrice());
        createTimeTxt.setText("创建时间:" + TimeUtils.getShowTime(goodsOrderListTypes.get(position).getCreateTime()));

        return convertView;
    }

    private String getContent(String ginfos){
        String content="";
        String[]  items=ginfos.split(",");
        for (int i=0;i<items.length;i++){
            String[] idCounts=items[i].split("\\+");

            String name=findName(Integer.parseInt(idCounts[0]));

            content+=name+"             x"+idCounts[1]+"\n";


        }
        return content;

    }
    private String findName(int id){
        String name="";
        for (GoodInfo goodInfo:goodInfos){
            if(goodInfo.getId()==id){
                name=goodInfo.getName();
                break;
            }
        }
        return name;
    }
}
