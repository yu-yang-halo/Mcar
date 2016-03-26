package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.TimeUtils;

import java.util.List;

import cn.service.bean.CouponInfo;

/**
 * Created by Administrator on 2016/3/26.
 */
public class CouponAdapter extends BaseAdapter {
    private static final int COUPON_TYPE_DISCOUNT=0;
    private static final int COUPON_TYPE_PRICE=1;
    Context ctx;
    List<CouponInfo> couponInfos;
    public CouponAdapter( Context ctx,List<CouponInfo> couponInfos){
        this.ctx=ctx;
        this.couponInfos=couponInfos;
    }
    @Override
    public int getCount() {
        if(couponInfos!=null){
            return couponInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(couponInfos!=null){
            return couponInfos.get(position);
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
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_my_coupon,null);
        }
        RelativeLayout rootView= (RelativeLayout) convertView.findViewById(R.id.relativeLayout6);
        TextView couponNameTxt= (TextView) convertView.findViewById(R.id.couponNameTxt);
        TextView couponEndtimeTxt= (TextView) convertView.findViewById(R.id.couponEndtimeTxt);
        TextView couponPriceDiscountTxt= (TextView) convertView.findViewById(R.id.couponPriceDiscountTxt);
        TextView couponDescTxt= (TextView) convertView.findViewById(R.id.couponDescTxt);
        ImageView invailedImageView= (ImageView) convertView.findViewById(R.id.invailedImageView);


        couponNameTxt.setText(couponInfos.get(position).getName());
        couponDescTxt.setText(couponInfos.get(position).getDesc());
        couponEndtimeTxt.setText("有效期至"+TimeUtils.getShowTime(couponInfos.get(position).getEndTime(),"yyyy-MM-dd"));


        if(TimeUtils.isOverTime(couponInfos.get(position).getEndTime())){
            invailedImageView.setVisibility(View.VISIBLE);
        }else{
            invailedImageView.setVisibility(View.GONE);
        }


        if(couponInfos.get(position).getType()==COUPON_TYPE_DISCOUNT) {

            rootView.setBackgroundResource(R.mipmap.coupon_bg0);
            couponNameTxt.setTextColor(ctx.getResources().getColor(R.color.font_gray0));
            couponPriceDiscountTxt.setTextColor(ctx.getResources().getColor(R.color.font_gray0));

            couponEndtimeTxt.setTextColor(ctx.getResources().getColor(R.color.font_gray1));
            couponDescTxt.setTextColor(ctx.getResources().getColor(R.color.font_gray1));


            couponPriceDiscountTxt.setText(couponInfos.get(position).getDiscount() + "折");

        }else{
            rootView.setBackgroundResource(R.mipmap.coupon_bg1);

            couponNameTxt.setTextColor(ctx.getResources().getColor(R.color.font_origan0));
            couponPriceDiscountTxt.setTextColor(ctx.getResources().getColor(R.color.font_origan0));

            couponEndtimeTxt.setTextColor(ctx.getResources().getColor(R.color.font_origan1));
            couponDescTxt.setTextColor(ctx.getResources().getColor(R.color.font_origan1));

            couponPriceDiscountTxt.setText("￥"+couponInfos.get(position).getPrice());
        }



        return convertView;
    }
}
