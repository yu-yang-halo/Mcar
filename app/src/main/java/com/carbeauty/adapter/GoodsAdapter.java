package com.carbeauty.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carbeauty.ImageUtils;
import com.carbeauty.R;

import java.util.List;

import cn.service.WSConnector;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GoodsAdapter extends BaseAdapter {
    List<GoodInfo> goodInfos;
    Context ctx;
    public GoodsAdapter(Context ctx,List<GoodInfo> goodInfos){
         this.ctx=ctx;
         this.goodInfos=goodInfos;
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
       if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.grid_gooditem,null);
        }

        TextView goodName= (TextView) convertView.findViewById(R.id.goodName);
        TextView goodDesc= (TextView) convertView.findViewById(R.id.goodDesc);
        TextView goodPriceTxt= (TextView) convertView.findViewById(R.id.goodPriceTxt);
        ImageView goodImageView= (ImageView) convertView.findViewById(R.id.goodImageView);
        goodName.setText(goodInfos.get(position).getName());
        goodDesc.setText(goodInfos.get(position).getDesc());
        goodPriceTxt.setText(goodInfos.get(position).getPrice()+"元");

        new NetBitmapToImageViewTask(position,goodImageView).execute();


        return convertView;

    }
    class NetBitmapToImageViewTask extends AsyncTask<String,String,String>{
        private int position;
        Bitmap bm;
        ImageView goodImageView;
        public NetBitmapToImageViewTask(int position,ImageView goodImageView){
            this.position=position;
            this.goodImageView=goodImageView;
        }
        @Override
        protected String doInBackground(String... params) {
            String url= WSConnector.getGoodsURL(goodInfos.get(position).getShopId() + "", goodInfos.get(position).getSrc());
            bm= ImageUtils.convertNetToBitmap(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(bm!=null){
                goodImageView.setImageBitmap(bm);
            }
        }
    }
}
