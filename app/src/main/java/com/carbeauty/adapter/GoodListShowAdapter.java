package com.carbeauty.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.ShowUnitUtils;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.fragment.GoodListShowFragment;
import com.carbeauty.order.ExpressActivity;
import com.pay.AlibabaPay;
import com.pay.AlipayInfo;
import com.pay.PayResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.AlipayInfoType;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsOrderListType;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GoodListShowAdapter extends BaseAdapter {
    Context ctx;
    List<GoodsOrderListType> goodsOrderListTypes;
    List<GoodInfo> goodInfos;
    private static final int SDK_PAY_FLAG=2001;
    GoodListShowFragment fragment;
    public GoodListShowAdapter(Context ctx, List<GoodsOrderListType> goodsOrderListTypes,
                               List<GoodInfo> goodInfos,
                               GoodListShowFragment fragment){
        this.ctx=ctx;
        this.goodsOrderListTypes=goodsOrderListTypes;
        this.goodInfos=goodInfos;
        this.fragment=fragment;
    }

    public List<GoodInfo> getGoodInfos() {
        return goodInfos;
    }

    public void setGoodInfos(List<GoodInfo> goodInfos) {
        this.goodInfos = goodInfos;
    }

    public List<GoodsOrderListType> getGoodsOrderListTypes() {
        return goodsOrderListTypes;
    }

    public void setGoodsOrderListTypes(List<GoodsOrderListType> goodsOrderListTypes) {
        this.goodsOrderListTypes = goodsOrderListTypes;
    }

    @Override
    public int getCount() {
        if(goodsOrderListTypes!=null){
            return goodsOrderListTypes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(goodsOrderListTypes!=null){
            return goodsOrderListTypes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.item_good_order,null);
            viewHolder=new ViewHolder();
            viewHolder.orderItemListView=(ListView) convertView.findViewById(R.id.orderItem);
            viewHolder.priceLabel=(TextView) convertView.findViewById(R.id.textView56);
            viewHolder.cancelButton= (Button) convertView.findViewById(R.id.button);
            viewHolder.payButton= (Button) convertView.findViewById(R.id.button6);

            viewHolder.itemHeader= (RelativeLayout) convertView.findViewById(R.id.itemHeader);
            viewHolder.itemFooter= (RelativeLayout) convertView.findViewById(R.id.itemFooter);

            viewHolder.finishedImageView= (ImageView) convertView.findViewById(R.id.finishedImageView);


            viewHolder.shopNameTxt=(TextView)convertView.findViewById(R.id.textView59);

            convertView.setTag(viewHolder);
        }




        viewHolder= (ViewHolder) convertView.getTag();

        viewHolder.orderItemListView.setDividerHeight(1);


        List<GoodInfo> goodInfos=getGoodInfosEachItem(goodsOrderListTypes.get(position));

        GoodListDetailShowAdapter goodListDetailShowAdapter=new GoodListDetailShowAdapter(goodInfos,ctx);

        viewHolder.orderItemListView.setAdapter(goodListDetailShowAdapter);
        viewHolder.priceLabel.setText(ShowUnitUtils.moneyUnitShow(goodsOrderListTypes.get(position).getPrice()));

        ShopInfo mShopInfo= IDataHandler.getInstance().getShopInfo(goodsOrderListTypes.get(position).getRealShopId());

        if(mShopInfo!=null){
            viewHolder.shopNameTxt.setText("服务店铺:"+mShopInfo.getName());
        }


        int footHeight=viewHolder.itemFooter.getLayoutParams().height;

        switch (goodsOrderListTypes.get(position).getState()){
            case Constants.GOOD_STATE_ORDER_DELIVE://1已发货
            {
                viewHolder.itemFooter.setVisibility(View.VISIBLE);
                viewHolder.payButton.setVisibility(View.VISIBLE);
                viewHolder.cancelButton.setVisibility(View.VISIBLE);
                viewHolder.payButton.setText("确认收货");
                viewHolder.cancelButton.setText("查看物流");
            }
            break;
            case Constants.GOOD_STATE_ORDER_NO_PAY://未支付
            {
                viewHolder.itemFooter.setVisibility(View.VISIBLE);
                viewHolder.payButton.setVisibility(View.VISIBLE);
                viewHolder.cancelButton.setVisibility(View.VISIBLE);
                viewHolder.payButton.setText("立即支付");
                viewHolder.cancelButton.setText("取消订单");
            }

            break;
            default: {
                viewHolder.itemFooter.setVisibility(View.GONE);
                footHeight=0;
            }
        }

        if(goodsOrderListTypes.get(position).getState()==Constants.GOOD_STATE_ORDER_FINISHED){
            viewHolder.finishedImageView.setVisibility(View.VISIBLE);
        }else {
            viewHolder.finishedImageView.setVisibility(View.GONE);
        }


        int totalHeight = 0;
        int row=goodListDetailShowAdapter.getCount();
        System.out.println("convertView "+convertView);
        ViewGroup.LayoutParams params = convertView.getLayoutParams();
        if (params==null){
            params=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,0);
        }
        if(row>0){
            View viewItem = goodListDetailShowAdapter.getView(0, null, viewHolder.orderItemListView);//这个很重要，那个展开的item的measureHeight比其他的大
            viewItem.measure(0, 0);
            totalHeight = viewItem.getMeasuredHeight()*row+(row-1)*viewHolder.orderItemListView.getDividerHeight();
            params.height = totalHeight
                            +viewHolder.itemHeader.getLayoutParams().height
                            +footHeight;
        }else{
            params.height =0;
        }

        convertView.setLayoutParams(params);



        viewHolder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsOrderListTypes.get(position).getState()==Constants.GOOD_STATE_ORDER_DELIVE){
                    //已发货 确认收货
                    setOrderStatus(position,false);
                }else {
                    //未支付
                    payOrder(position);
                }

            }
        });
        viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsOrderListTypes.get(position).getState()==Constants.GOOD_STATE_ORDER_DELIVE){
                    //已发货 查看物流
                    Intent intent=new Intent(ctx,ExpressActivity.class);
                    intent.putExtra("orderId",goodsOrderListTypes.get(position).getId());
                    ctx.startActivity(intent);
                }else {
                    //未支付 取消订单
                    setOrderStatus(position,true);
                }

            }
        });


        return convertView;
    }

    private void setOrderStatus(final int position, final boolean cancel){
        /**
         * 取消订单 可退款...
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sucYN=false;
                try {
                    int status=Constants.GOOD_STATE_ORDER_CANCEL;
                    if(!cancel){
                        status=Constants.GOOD_STATE_ORDER_FINISHED;
                    }
                    sucYN=WSConnector.getInstance().updGoodsOrder(goodsOrderListTypes.get(position).getId(),status);
                } catch (WSException e) {
                    e.printStackTrace();
                }

                Message msg=new Message();
                msg.what=sucYN?1002:0;
                Bundle data=new Bundle();
                data.putInt("position",position);
                data.putBoolean("b_cancel",cancel);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    private void payOrder(final int position){
        /**
         * 支付宝支付
         * 1.获取店铺alipay pid email
         * 2.签名
         * 3.支付
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                try {
                    AlipayInfoType alipayInfoType=WSConnector.getInstance().getAlipayByShopId(-1);
                    AlibabaPay alibabaPay = null;
                    if(alipayInfoType!=null&&alipayInfoType.getAliPid()!=null&&alipayInfoType.getSellerEmail()!=null){
                        alibabaPay=new AlibabaPay(alipayInfoType.getAliPid(),alipayInfoType.getSellerEmail());
                    }
                    AlipayInfo alipayInfo=new AlipayInfo("客乐养车坊商品","商品订单",
                            goodsOrderListTypes.get(position).getPrice()+"",goodsOrderListTypes.get(position).getTradeNo());

                    if(alibabaPay!=null){
                        String sign= WSConnector.getInstance().signContent(-1,alibabaPay.getOrderInfo(alipayInfo));
                        alipayInfo.setSign(sign);
                    }


                    if(alipayInfo!=null&&alibabaPay!=null&&alipayInfo.getSign()!=null){
                        final String payinfo=alibabaPay.getPayInfoData(alipayInfo);

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask((Activity) ctx);
                                // 调用支付接口，获取支付结果
                                Map<String, String>  result = alipay.payV2(payinfo, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }else{
                        msg.what=1000;

                    }


                } catch (WSException e) {
                    msg.what=1001;

                }


            }
        }).start();


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

                case 1000:
                    Toast.makeText(ctx,"在线支付错误,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(ctx,"无法支付,请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    int pos=msg.getData().getInt("position");
                    boolean b_cancel=msg.getData().getBoolean("b_cancel");
                    if(pos>=0){
                        goodsOrderListTypes.remove(pos);
                        notifyDataSetChanged();
                    }else{
                        if(b_cancel){
                            Toast.makeText(ctx, "暂时无法取消", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ctx, "暂时无法确认收货", Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;
                case 0:
                    boolean b_cancel2=msg.getData().getBoolean("b_cancel");
                    if(b_cancel2){
                        Toast.makeText(ctx, "暂时无法取消", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ctx, "暂时无法确认收货", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };





    private List<GoodInfo> getGoodInfosEachItem(GoodsOrderListType goodsOrderListType){
        String ginfos=goodsOrderListType.getGoodsInfo();
        String colors=goodsOrderListType.getColor();
        String tags=goodsOrderListType.getTag();
        String[] colorArrs=new String[0];
        String[] tagArrs = new String[0];
        if(colors!=null){
            colorArrs=colors.split("\\|");
        }
        if(tags!=null){
            tagArrs=tags.split("\\|");
        }

        List<GoodInfo> goodInfos=new ArrayList<GoodInfo>();
        String[]  items=ginfos.split(",");
        for (int i=0;i<items.length;i++){
            String[] idCounts=items[i].split("\\+");

            if(idCounts!=null&&idCounts.length==2){
                GoodInfo goodInfo=findGoodInfo(Integer.parseInt(idCounts[0]));
                if(goodInfo!=null){
                    goodInfo.setBuyNumber(Integer.parseInt(idCounts[1]));

                    if(tagArrs.length==items.length){
                        goodInfo.setTags(tagArrs[i]);
                    }
                    if(colorArrs.length==items.length){
                        goodInfo.setColors(colorArrs[i]);
                    }
                    goodInfos.add(goodInfo);
                }

            }



        }
        return goodInfos;

    }
    private GoodInfo findGoodInfo(int id){
        GoodInfo m_goodInfo=null;
        for (GoodInfo goodInfo:goodInfos){
            if(goodInfo.getId()==id){
                m_goodInfo=goodInfo;
                break;
            }
        }
        return m_goodInfo;
    }
    class ViewHolder{
        TextView priceLabel;
        Button   cancelButton;
        Button   payButton;
        ListView orderItemListView;
        RelativeLayout itemHeader;
        RelativeLayout itemFooter;
        TextView shopNameTxt;
        ImageView finishedImageView;
    }
}
