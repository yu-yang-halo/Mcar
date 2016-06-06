package com.carbeauty.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.web.WebBroswerActivity;
import com.carbeauty.web.WebBroswerPromotionActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.PromotionInfoType;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/10.
 */
public class PromotionAdapter extends BaseAdapter {
    List<PromotionInfoType> promotionInfoTypes;
    Context ctx;
    List<Bitmap> bitmaps;
    public PromotionAdapter(List<PromotionInfoType> promotionInfoTypes,Context ctx,List<Bitmap> bms){
        this.promotionInfoTypes=promotionInfoTypes;
        this.ctx=ctx;
        this.bitmaps=bms;
    }
    @Override
    public int getCount() {
        if(promotionInfoTypes==null){
            return 0;
        }
        return promotionInfoTypes.size();
    }

    @Override
    public Object getItem(int position) {
        if(promotionInfoTypes==null){
            return null;
        }
        return promotionInfoTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item1,null);
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.image);
        Button  btnCoupon= (Button) convertView.findViewById(R.id.imageView16);

        imageView.setBackgroundDrawable(new BitmapDrawable(bitmaps.get(position)));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx,WebBroswerPromotionActivity.class);

                intent.putExtra("Title","优惠活动");
                intent.putExtra("URL",promotionInfoTypes.get(position).getSrc());
                intent.putExtra("promotionId",promotionInfoTypes.get(position).getId());

                ctx.startActivity(intent);

            }
        });

        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

}
