package com.carbeauty.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.carbeauty.R;
import com.carbeauty.cache.CartManager;

import java.util.ArrayList;
import java.util.List;

import cn.service.Util;
import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodLookAdapter extends BaseAdapter {
    Context ctx;
    List<CartManager.MyCartClass> myCartClassList;
    TextView totalPriceTxt;
    RequestQueue mQueue;
    public GoodLookAdapter(Context ctx,List<CartManager.MyCartClass> myCartClassList){
        this.ctx=ctx;
        this.myCartClassList=myCartClassList;
        this.mQueue= Volley.newRequestQueue(ctx);
    }
    @Override
    public int getCount() {
        if(myCartClassList!=null){
            return myCartClassList.size();
        }
        return 0;
    }


    public TextView getTotalPriceTxt() {
        return totalPriceTxt;
    }

    public void setTotalPriceTxt(TextView totalPriceTxt) {
        this.totalPriceTxt = totalPriceTxt;
    }

    @Override
    public Object getItem(int position) {
        if(myCartClassList!=null){
            return myCartClassList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.goodslook_item,null);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.textView44);
        TextView descTxt= (TextView) convertView.findViewById(R.id.textView45);
        TextView priceTxt= (TextView) convertView.findViewById(R.id.textView46);
        TextView countTxt= (TextView) convertView.findViewById(R.id.textView47);

        ImageView showImageView= (ImageView) convertView.findViewById(R.id.imageView10);

        final CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.checkBox2);


        if(myCartClassList.get(position).isCheckYN()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }


        nameTxt.setText(myCartClassList.get(position).getGoodInfo().getName());
        descTxt.setText(Util.formatHtml(myCartClassList.get(position).getGoodInfo().getDesc()));
        countTxt.setText("x"+myCartClassList.get(position).getCount());
        priceTxt.setText(myCartClassList.get(position).getGoodInfo().getPrice() + "元");

        ImageLoader imageLoader=new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(showImageView
                , 0, 0);



        imageLoader.get(myCartClassList.get(position).getImageURL(), listener);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myCartClassList.get(position).setCheckYN(isChecked);
                totalPriceTxt.setText(getLastestNewPrice() + "元");
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

               new AlertDialog.Builder(ctx).setTitle("提示").setMessage("是否删除该商品").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       myCartClassList.remove(position);
                       CartManager.getInstance().cacheMyCartClassToDisk(ctx);
                       notifyDataSetChanged();

                   }
               }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               }).show();


                return true;
            }
        });

        return convertView;
    }

    public void selectAll(boolean isAll){
        if (myCartClassList==null){
            return;
        }
        for (int i=0;i<myCartClassList.size();i++){
            myCartClassList.get(i).setCheckYN(isAll);
        }
        totalPriceTxt.setText(getLastestNewPrice()+"元");
        notifyDataSetChanged();
    }

    public float getLastestNewPrice(){
        if (myCartClassList==null){
            return 0;
        }
        float totalPrice=0;
        for (int i=0;i<myCartClassList.size();i++){
            if(myCartClassList.get(i).isCheckYN()){
                int count=myCartClassList.get(i).getCount();
                float price=myCartClassList.get(i).getGoodInfo().getPrice();
                totalPrice+=(price*count);
            }

        }

        return totalPrice;
    }

    public List<CommitDataBean> getCommitData(){
        if (myCartClassList==null){
            return null;
        }
        CommitDataBean commitDataBean0=new CommitDataBean();
        CommitDataBean commitDataBean1=new CommitDataBean();

        String data0="";
        String data1="";
        float  totalPrice0=0;
        float  totalPrice1=0;
        for (CartManager.MyCartClass myCartClass:myCartClassList){
            if(myCartClass.isCheckYN()){
                if(myCartClass.getGoodInfo().getShopId()==-1){
                    data0+=myCartClass.getId()
                            +"+"+myCartClass.getCount()+",";
                    commitDataBean0.setShopId(-1);
                    totalPrice0+=(myCartClass.getCount()*myCartClass.getGoodInfo().getPrice());
                }else{
                    data1+=myCartClass.getId()
                            +"+"+myCartClass.getCount()+",";
                    commitDataBean1.setShopId(myCartClass.getGoodInfo().getShopId());
                    totalPrice1+=(myCartClass.getCount()*myCartClass.getGoodInfo().getPrice());
                }
            }
        }

        if(!data0.equals("")){
            data0=data0.substring(0,data0.length()-1);
        }
        if(!data1.equals("")){
            data1=data1.substring(0,data1.length()-1);
        }

        List<CommitDataBean> commitDataBeans=new ArrayList<CommitDataBean>();

        if(totalPrice0>0){
            commitDataBean0.setTotalPrice(totalPrice0);
            commitDataBean0.setData(data0);
            commitDataBeans.add(commitDataBean0);
        }
        if(totalPrice1>0){
            commitDataBean1.setTotalPrice(totalPrice1);
            commitDataBean1.setData(data1);
            commitDataBeans.add(commitDataBean1);
        }


        return commitDataBeans;

    }

    public class CommitDataBean{
        private String data;
        private float totalPrice;
        private int shopId;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        @Override
        public String toString() {
            return "CommitDataBean{" +
                    "data='" + data + '\'' +
                    ", totalPrice=" + totalPrice +
                    ", shopId=" + shopId +
                    '}';
        }
    }
}
