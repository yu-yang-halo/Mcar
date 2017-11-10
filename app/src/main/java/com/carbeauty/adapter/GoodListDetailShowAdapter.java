package com.carbeauty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.carbeauty.MyApplication;
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
    ImageLoader imageLoader;
    public GoodListDetailShowAdapter(List<GoodInfo> goodInfos, Context ctx){
        this.goodInfos=goodInfos;
        this.ctx=ctx;
        mQueue = Volley.newRequestQueue(ctx);
        MyApplication myApplication= (MyApplication) ctx.getApplicationContext();

        imageLoader=new ImageLoader(mQueue, myApplication.getBitmapCache());
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

            TextView sizeTxt= (TextView) convertView.findViewById(R.id.textView66);
            TextView colorTxt= (TextView) convertView.findViewById(R.id.textView67);
            holder=new AdapterHolder();

            holder.goodBuyNumberTextView=goodBuyNumberTextView;
            holder.goodDescTextView=goodDescTextView;
            holder.goodImageView=goodImageView;
            holder.goodPriceTextView=goodPriceTextView;
            holder.sizeTxt=sizeTxt;
            holder.colorTxt=colorTxt;


            convertView.setTag(holder);


        }

        holder= (AdapterHolder) convertView.getTag();
        GoodInfo goodInfo=goodInfos.get(position);

        holder.goodPriceTextView.setText("￥"+goodInfo.getPrice());
        holder.goodDescTextView.setText(Util.formatHtml(goodInfo.getDesc()));
        holder.goodBuyNumberTextView.setText("x"+goodInfo.getBuyNumber());


        if(goodInfo.getTags()!=null&& !TextUtils.isEmpty(goodInfo.getTags().trim())){
            holder.sizeTxt.setVisibility(View.VISIBLE);
            holder.sizeTxt.setText(goodInfo.getTags());
        }else{
            holder.sizeTxt.setVisibility(View.INVISIBLE);
        }
        if(goodInfo.getColors()!=null&& !TextUtils.isEmpty(goodInfo.getColors().trim())){
            holder.colorTxt.setVisibility(View.VISIBLE);
            holder.colorTxt.setBackgroundColor(Util.rgbArrsToInt(goodInfo.getColors()));
        }else{
            holder.colorTxt.setVisibility(View.INVISIBLE);
        }



        ImageLoader.ImageListener listener=ImageLoader.getImageListener(holder.goodImageView
                , 0, 0);


        String src=goodInfo.getSrc();
        String url= WSConnector.getGoodsURL(goodInfo.getShopId() + "", src.split(",")[0]);

        imageLoader.get(url, listener,160,160);



        return convertView;
    }

    class AdapterHolder{
        ImageView goodImageView;
        TextView goodBuyNumberTextView;
        TextView goodDescTextView;
        TextView goodPriceTextView;
        TextView sizeTxt;
        TextView colorTxt;
    }
}
