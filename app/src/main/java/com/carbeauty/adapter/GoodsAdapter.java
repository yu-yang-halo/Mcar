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
import java.util.concurrent.Executors;

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
            convertView= LayoutInflater.from(ctx).inflate(R.layout.grid_gooditem,null);
            holder=new ItemHolder();
            holder.goodName= (TextView) convertView.findViewById(R.id.goodName);
            holder.goodDesc= (TextView) convertView.findViewById(R.id.goodDesc);
            holder.goodPriceTxt= (TextView) convertView.findViewById(R.id.goodPriceTxt);
            holder.goodImageView= (ImageView) convertView.findViewById(R.id.goodImageView);
            convertView.setTag(holder);

       }

        holder= (ItemHolder) convertView.getTag();

        holder.goodName.setText(goodInfos.get(position).getName());
        holder.goodDesc.setText(goodInfos.get(position).getDesc());
        holder.goodPriceTxt.setText(goodInfos.get(position).getPrice() + "元");

        if(goodInfos.get(position).getBitmap()!=null){
            holder.goodImageView.setImageBitmap(goodInfos.get(position).getBitmap());
        }else{
            new NetBitmapToImageViewTask(position,holder.goodImageView).executeOnExecutor(Executors.newCachedThreadPool());
        }

        return convertView;

    }
    class ItemHolder{
        TextView goodName;
        TextView goodDesc;
        TextView goodPriceTxt;
        ImageView goodImageView;

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
            String src=goodInfos.get(position).getSrc();

            String url= WSConnector.getGoodsURL(goodInfos.get(position).getShopId() + "",   src.split(",")[0]);
            bm= ImageUtils.convertNetToBitmap(url,100,100);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(bm!=null){
                goodImageView.setImageBitmap(bm);
                goodInfos.get(position).setBitmap(bm);
            }
        }
    }
}
