package com.carbeauty.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;

import java.util.List;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/28.
 */
public class HotGoodsAdapter extends BaseAdapter {
    List<GoodInfo> goodInfos;
    Context ctx;
    RequestQueue mQueue;
    public HotGoodsAdapter(Context ctx, List<GoodInfo> goodInfos){
         this.ctx=ctx;
         this.goodInfos=goodInfos;
         this.mQueue = Volley.newRequestQueue(ctx);
    }

    public void setGoodInfos(List<GoodInfo> goodInfos) {
        this.goodInfos = goodInfos;
    }

    @Override
    public int getCount() {
        if(goodInfos!=null){
            return goodInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(goodInfos!=null){
            return goodInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.grid_hotgooditem,null);
            holder=new ItemHolder();
            holder.goodName= (TextView) convertView.findViewById(R.id.goodName);
            holder.goodPriceTxt= (TextView) convertView.findViewById(R.id.goodPriceTxt);
            holder.goodImageView= (ImageView) convertView.findViewById(R.id.goodImageView);
            convertView.setTag(holder);

       }

        holder= (ItemHolder) convertView.getTag();

        holder.goodName.setText(goodInfos.get(position).getName());
        holder.goodPriceTxt.setText(goodInfos.get(position).getPrice() + "元");



        ImageLoader imageLoader=new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(holder.goodImageView
                , 0, 0);


        String src=goodInfos.get(position).getSrc();
        String url= WSConnector.getGoodsURL(goodInfos.get(position).getShopId() + "", src.split(",")[0]);

        imageLoader.get(url, listener);


        return convertView;

    }
    class ItemHolder{
        TextView goodName;
        TextView goodPriceTxt;
        ImageView goodImageView;

    }

}
