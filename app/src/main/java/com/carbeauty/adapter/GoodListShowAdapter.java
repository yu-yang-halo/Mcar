package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.TimeUtils;

import java.util.ArrayList;
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
        ListView orderItemListView=null;

        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_good_order,null);
            orderItemListView= (ListView) convertView.findViewById(R.id.orderItem);
            convertView.setTag(orderItemListView);
        }
        orderItemListView= (ListView) convertView.getTag();
        orderItemListView.setDividerHeight(1);

        String ginfos=goodsOrderListTypes.get(position).getGoodsInfo();

        List<GoodInfo> goodInfos=getGoodInfosEachItem(ginfos);

        GoodListDetailShowAdapter goodListDetailShowAdapter=new GoodListDetailShowAdapter(goodInfos,ctx);

        orderItemListView.setAdapter(goodListDetailShowAdapter);



        int totalHeight = 0;
        int row=goodListDetailShowAdapter.getCount();


        View viewItem = goodListDetailShowAdapter.getView(0, null, orderItemListView);//这个很重要，那个展开的item的measureHeight比其他的大
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight()*row;

        ViewGroup.LayoutParams params = orderItemListView.getLayoutParams();
        params.height = totalHeight;
        orderItemListView.setLayoutParams(params);




        return convertView;
    }


    private List<GoodInfo> getGoodInfosEachItem(String ginfos){
        List<GoodInfo> goodInfos=new ArrayList<GoodInfo>();
        String[]  items=ginfos.split(",");
        for (int i=0;i<items.length;i++){
            String[] idCounts=items[i].split("\\+");

            if(idCounts!=null&&idCounts.length==2){
                GoodInfo goodInfo=findGoodInfo(Integer.parseInt(idCounts[0]));
                if(goodInfo!=null){
                    goodInfo.setBuyNumber(Integer.parseInt(idCounts[1]));
                    goodInfos.add(goodInfo);
                }

            }



        }
        return goodInfos;

    }
    private GoodInfo findGoodInfo(int id){
        GoodInfo m_goodInfo=null;
        for (GoodInfo goodInfo:goodInfos){
            if(goodInfo.getId()==id){
                m_goodInfo=goodInfo;
                break;
            }
        }
        return m_goodInfo;
    }
}
