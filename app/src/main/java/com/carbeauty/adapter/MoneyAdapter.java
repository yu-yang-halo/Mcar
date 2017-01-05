package com.carbeauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.carbeauty.R;
import com.carbeauty.alertDialog.WindowUtils;
import com.carbeauty.cache.ContentBox;

import java.util.List;

import cn.service.bean.OrderStateType;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MoneyAdapter extends BaseAdapter {
    String[] arrs;
    Context ctx;
    int BUTTON_WIDTH=50;
    private int selPos=-1;
    public MoneyAdapter(String[] arrs, Context ctx){
        this.arrs=arrs;
        this.ctx=ctx;
        ContentBox.loadString(ctx, ContentBox.KEY_ORDER_TIME, null);

        int screenWindth=WindowUtils.getScreenWidth(ctx);
        BUTTON_WIDTH=(screenWindth-60)/6;


    }

    public int getSelPos() {
        return selPos;
    }

    public void setSelPos(int selPos) {
        this.selPos = selPos;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(arrs!=null){
            return arrs.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (arrs==null){
            return null;
        }
        return arrs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item_money,null);
        }
        final Button moneyBtn= (Button) convertView.findViewById(R.id.moneyBtn);

        ViewGroup.LayoutParams layoutParams=moneyBtn.getLayoutParams();
        layoutParams.width=BUTTON_WIDTH;
        moneyBtn.setLayoutParams(layoutParams);

        moneyBtn.setText(arrs[position]);
        moneyBtn.setSelected(selPos==position);

        return convertView;
    }
}
