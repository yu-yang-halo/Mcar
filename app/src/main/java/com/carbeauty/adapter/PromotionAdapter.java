package com.carbeauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.web.WebBroswerActivity;
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
    public PromotionAdapter(List<PromotionInfoType> promotionInfoTypes,Context ctx){
        this.promotionInfoTypes=promotionInfoTypes;
        this.ctx=ctx;
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
        imageView.setBackgroundResource(R.drawable.active_01);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCouponTask(promotionInfoTypes.get(position).getId()).execute();
            }
        });

        return convertView;
    }
    class GetCouponTask extends AsyncTask<String,String,String>{
        int promotionId;
        KProgressHUD progressHUD;
        GetCouponTask(int promotionId){
            this.promotionId=promotionId;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("领取中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                WSConnector.getInstance().updCoupon(promotionId);
            } catch (WSException e) {
                return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s==null){
                Toast.makeText(ctx,"购物券领取成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
