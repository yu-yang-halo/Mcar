package com.carbeauty.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.carbeauty.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/28.
 */

public class NumberAdapter extends BaseAdapter {
    private String[] lists;
    private Context ctx;
    private boolean disableNumber;
    public NumberAdapter(Context ctx,String[] lists){
        this.ctx=ctx;
        this.lists=lists;
    }

    public boolean isDisableNumber() {
        return disableNumber;
    }

    public void setDisableNumber(boolean disableNumber) {
        this.disableNumber = disableNumber;
    }

    public String[] getLists() {
        return lists;
    }

    public void setLists(String[] lists) {
        this.lists = lists;
    }

    @Override
    public int getCount() {
        if(lists!=null){
            return lists.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(lists!=null){
            return lists[position];
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
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_btn,null);
        }
        Button btnItem= (Button) convertView.findViewById(R.id.btnItem);
        btnItem.setText(lists[position]);

        if(disableNumber){
            String charStr=lists[position];
            String pattern = "^[0-9]$";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(charStr);
            if (m.matches()){
                btnItem.setEnabled(false);
            }else{
                btnItem.setEnabled(true);
            }

        }else{
            btnItem.setEnabled(true);
        }



        return convertView;
    }
}
