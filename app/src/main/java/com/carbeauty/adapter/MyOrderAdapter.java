package com.carbeauty.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.fragment.MyOrderFragment;
import com.carbeauty.order.MyOrderActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pay.AlibabaPay;
import com.pay.AlipayInfo;
import com.pay.PayResult;

import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.AlipayInfoType;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MyOrderAdapter  extends BaseAdapter {
    Context ctx;
    List<MyOrderActivity.CommonOrderBean> commonOrderBeans;
    /** type  "待付款"--0,"已付款"--1,"待确认"--2,"已完成"--3,"已退款"--4  **/
    String[] statusArr=new String[]{"待付款","已付款","待确认","已完成","已退款"};
    int type;
    //int shopId;
    private static final int SDK_PAY_FLAG = 1;
    MyOrderFragment fragment;
    public MyOrderAdapter(Context ctx,
                          List<MyOrderActivity.CommonOrderBean> commonOrderBeans,
                          int type,
                          MyOrderFragment fragment){
        this.ctx=ctx;
        this.commonOrderBeans=commonOrderBeans;
        this.type=type;
        //shopId= ContentBox.getValueInt(ctx, ContentBox.KEY_SHOP_ID, 0);
        this.fragment=fragment;
    }
    public void setCommonOrderBeans(List<MyOrderActivity.CommonOrderBean> commonOrderBeans){
        this.commonOrderBeans=commonOrderBeans;
    }
    @Override
    public int getCount() {
        if(commonOrderBeans!=null){
            return commonOrderBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(commonOrderBeans!=null){
            return commonOrderBeans.get(position);
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
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_my_order,null);
        }
        TextView titleNameTxt= (TextView) convertView.findViewById(R.id.titleNameTxt);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);
        TextView statusTxt= (TextView) convertView.findViewById(R.id.statusTxt);
        TextView priceTxt= (TextView) convertView.findViewById(R.id.priceTxt);
        TextView createTimeTxt= (TextView) convertView.findViewById(R.id.createTimeTxt);
        ImageView descImageView= (ImageView) convertView.findViewById(R.id.descImageView);
        Button delBtn= (Button) convertView.findViewById(R.id.delBtn);
        Button payButton= (Button) convertView.findViewById(R.id.button7);

        TextView shopNameTxt= (TextView) convertView.findViewById(R.id.textView58);

        if(position>=commonOrderBeans.size()||commonOrderBeans.get(position)==null){
            Toast.makeText(ctx,"数据错误，请刷新重试~",Toast.LENGTH_SHORT).show();
            return convertView;
        }


        titleNameTxt.setText(commonOrderBeans.get(position).getTitle());

        if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_DECO){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_8);
        }else if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_OIL){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_6);
        }else if(commonOrderBeans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){
            descImageView.setBackgroundResource(R.drawable.homepage_gridview_7);
        }

        ShopInfo mShopInfo= IDataHandler.getInstance().getShopInfo(commonOrderBeans.get(position).getShopId());
        if(mShopInfo!=null){
            shopNameTxt.setText("服务店铺:"+mShopInfo.getName());
        }

        String status="";

        if(type>=0&&type<statusArr.length){
            status=statusArr[type];
        }
        statusTxt.setText(status);
        boolean isOverTime=TimeUtils.isOverTime(commonOrderBeans.get(position).getCreateTime());

        if(type==0){
            if(isOverTime){
                statusTxt.setText("已过期");
                delBtn.setVisibility(View.GONE);
                payButton.setVisibility(View.GONE);
            }else{
                delBtn.setVisibility(View.VISIBLE);
                payButton.setVisibility(View.VISIBLE);
            }
        }else if(type==1){
            payButton.setVisibility(View.GONE);
            if(isOverTime){
                delBtn.setVisibility(View.GONE);
            }else{
                delBtn.setVisibility(View.VISIBLE);
            }
        }else if(type==2){
            payButton.setVisibility(View.GONE);
            if(isOverTime){
                statusTxt.setText("已过期");
                delBtn.setVisibility(View.GONE);
            }else{
                delBtn.setVisibility(View.VISIBLE);
            }
        }else if(type==3||type==4){
            payButton.setVisibility(View.GONE);
            delBtn.setVisibility(View.GONE);

        }


        descTxt.setText(commonOrderBeans.get(position).getDesc());

        priceTxt.setText("总价:￥"+commonOrderBeans.get(position).getPrice());
        createTimeTxt.setText("预约时间:" + TimeUtils.getShowTime(commonOrderBeans.get(position).getCreateTime()));


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new CancelOrderTask(commonOrderBeans.get(position).getItemType(),
                        commonOrderBeans.get(position).getId(),position).execute();


            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyOrderActivity.CommonOrderBean bean=commonOrderBeans.get(position);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {

                            AlipayInfoType alipayInfoType= null;
                            Message msg = new Message();
                            try {
                                alipayInfoType = WSConnector.getInstance().getAlipayByShopId(bean.getShopId());
                                AlibabaPay alibabaPay=new AlibabaPay(alipayInfoType.getAliPid(),alipayInfoType.getSellerEmail());
                                AlipayInfo  alipayInfo=new AlipayInfo(bean.getTitle(),bean.getDesc(),bean.getPrice()+"",bean.getTradeNo());

                                String content=alibabaPay.getOrderInfo(alipayInfo);
                                String sign=WSConnector.getInstance().signContent(bean.getShopId(),content);
                                alipayInfo.setSign(sign);

                                final String payinfo=alibabaPay.getPayInfoData(alipayInfo);



                                // 构造PayTask 对象
                                PayTask alipay = new PayTask((Activity)ctx);
                                // 调用支付接口，获取支付结果
                                Map<String, String> result = alipay.payV2(payinfo, true);


                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;


                            } catch (WSException e) {
                                msg.what = 2;
                            }

                            mHandler.sendMessage(msg);


                        }
                    };

                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();


            }
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(ctx).setTitle("提示").setMessage("是否删除该订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       new OrderDelTask(ctx,commonOrderBeans,position).execute();

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


    class OrderDelTask extends  AsyncTask<String,String,Boolean>{

        private int position;
        private List<MyOrderActivity.CommonOrderBean> beans;
        private Context ctx;

        public OrderDelTask(Context ctx,List<MyOrderActivity.CommonOrderBean> commonOrderBeans,int position){
            this.position=position;
            this.beans=commonOrderBeans;
            this.ctx=ctx;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(beans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_DECO){

                try {
                    WSConnector.getInstance().delDecoOrder(beans.get(position).getId());
                } catch (WSException e) {
                    return false;
                }

            }else if(beans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_OIL){

                try {
                    WSConnector.getInstance().delOilOrder(beans.get(position).getId());
                } catch (WSException e) {
                    return false;
                }

            }else if(beans.get(position).getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){


                try {
                    WSConnector.getInstance().delMetaOrder(beans.get(position).getId());
                } catch (WSException e) {
                    return false;
                }
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                commonOrderBeans.remove(position);
            }else{
                Toast.makeText(ctx,"删除失败",Toast.LENGTH_SHORT).show();

            }
            notifyDataSetChanged();
        }
    }


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ctx, "支付成功", Toast.LENGTH_SHORT).show();
                        fragment.startRefreshing();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ctx, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ctx, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case 2:
                    Toast.makeText(ctx,"在线支付错误,请重试",Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };


    class  CancelOrderTask extends AsyncTask<String,String,String>{
        int type;
        int id;
        int pos;
        KProgressHUD progressHUD;
        CancelOrderTask(int type,int id,int pos){
            this.type=type;
            this.id=id;
            this.pos=pos;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("取消中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s==null){
                commonOrderBeans.remove(pos);
                notifyDataSetChanged();
            }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if(type== MyOrderActivity.CommonOrderBean.ITEM_TYPE_DECO){
                try {
                    WSConnector.getInstance().updDecoOrder(id,Constants.STATE_ORDER_CANCEL);
                } catch (WSException e) {
                   return e.getErrorMsg();
                }

            }else  if(type== MyOrderActivity.CommonOrderBean.ITEM_TYPE_OIL){
                try {
                    WSConnector.getInstance().updOilOrder(id, Constants.STATE_ORDER_CANCEL);
                } catch (WSException e) {
                    return e.getErrorMsg();
                }

            }else  if(type== MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){
                try {
                    WSConnector.getInstance().updMetaOrder(id, Constants.STATE_ORDER_CANCEL);
                } catch (WSException e) {
                    return e.getErrorMsg();
                }

            }



            return null;
        }
    }
}
