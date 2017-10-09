package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.carbeauty.R;
import com.carbeauty.cache.IDataHandler;

import java.util.List;

import cn.service.Util;
import cn.service.WSConnector;
import cn.service.bean.GoodInfo;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/6/29.
 */
public class GoodListDetailShowAdapter extends BaseAdapter {
    private List<GoodInfo> goodInfos;
    private Context ctx;
    RequestQueue mQueue;
    public GoodListDetailShowAdapter(List<GoodInfo> goodInfos, Context ctx){
        this.goodInfos=goodInfos;
        this.ctx=ctx;
        mQueue = Volley.newRequestQueue(ctx);
    }
    @Override
    public int getCount() {
        return goodInfos.size();
    }

    @Override
    public Object getItem(int position) {
        if(goodInfos==null){
            return null;
        }
        return goodInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder holder;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_goodorder_detail_item,null);
            ImageView goodImageView= (ImageView) convertView.findViewById(R.id.imageView2);
            TextView goodBuyNumberTextView= (TextView) convertView.findViewById(R.id.textView53);
            TextView goodDescTextView= (TextView) convertView.findViewById(R.id.textView7);
            TextView goodPriceTextView= (TextView) convertView.findViewById(R.id.textView54);


            holder=new AdapterHolder();

            holder.goodBuyNumberTextView=goodBuyNumberTextView;
            holder.goodDescTextView=goodDescTextView;
            holder.goodImageView=goodImageView;
            holder.goodPriceTextView=goodPriceTextView;


            convertView.setTag(holder);


        }

        holder= (AdapterHolder) convertView.getTag();
        GoodInfo goodInfo=goodInfos.get(position);

        holder.goodPriceTextView.setText("￥"+goodInfo.getPrice());
        holder.goodDescTextView.setText(Util.formatHtml(goodInfo.getDesc()));
        holder.goodBuyNumberTextView.setText("x"+goodInfo.getBuyNumber());

        ImageLoader imageLoader=new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(holder.goodImageView
                , 0, 0);


        String src=goodInfo.getSrc();
        String url= WSConnector.getGoodsURL(goodInfo.getShopId() + "", src.split(",")[0]);

        imageLoader.get(url, listener);



        return convertView;
    }

    class AdapterHolder{
        ImageView goodImageView;
        TextView goodBuyNumberTextView;
        TextView goodDescTextView;
        TextView goodPriceTextView;
    }
}
