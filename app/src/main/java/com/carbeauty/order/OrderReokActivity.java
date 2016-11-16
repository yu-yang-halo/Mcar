package com.carbeauty.order;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.adapter.DecorationDelAdapter;
import com.carbeauty.adapter.MetalInfoDelAdapter;
import com.carbeauty.adapter.MyHandlerCallback;
import com.carbeauty.adapter.OilInfoDelAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import com.carbeauty.Constants;
import com.pay.AlibabaPay;
import com.pay.AlipayInfo;
import com.pay.PayResult;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.AlipayInfoType;
import cn.service.bean.CouponInfo;
import cn.service.bean.DecoOrderInfo;
import cn.service.bean.DecorationInfo;
import cn.service.bean.MetaOrderInfo;
import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;
import cn.service.bean.OilOrderInfo;

/**
 * Created by Administrator on 2016/3/12.
 */
public class OrderReokActivity extends HeaderActivity {
    int ac_type_value;

    List<DecorationInfo> decorationInfoSet;
    List<OilInfo> oilInfoSet;
    List<MetalplateInfo> metalplateInfoSet;

    ListView selItemListView;
    DecorationDelAdapter decorationDelAdapter;
    OilInfoDelAdapter oilInfoDelAdapter;
    MetalInfoDelAdapter metalInfoDelAdapter;

    TextView promoTxt;
    TextView totalPriceTxt;
    TextView promoDescTxt;
    TextView offerDescTxt;
    Button  createOrderBtn;
    int shopId;
    int carId;
    private String orderTime;
    float totalPrice=0;
    RelativeLayout offerRelayout;
    RelativeLayout couponRelayout;
    List<CouponInfo> couponInfos;

    String[]  couponStrArrs;
    String[]  offerStrArrs;
    int orderType=-2;

    int selectIndex=-1;
    int offerIndex=0;
    AlibabaPay alibabaPay;
    private static final int SDK_PAY_FLAG = 1;


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderReokActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(OrderReokActivity.this,OrderResultActivity.class);
                        intent.putExtra(Constants.AC_TYPE,ac_type_value);
                        intent.putExtra(Constants.ORDER_RESULT_IS_OK,true);
                        intent.putExtra(Constants.OFFER_PRICE,totalPrice);
                        intent.putExtra("Title","");
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderReokActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderReokActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_orderreok);

        offerRelayout= (RelativeLayout) findViewById(R.id.offerRelayout);
        couponRelayout= (RelativeLayout) findViewById(R.id.couponRelayout);

        promoDescTxt= (TextView) findViewById(R.id.textView52);

        offerDescTxt= (TextView) findViewById(R.id.textView51);



        int type=ContentBox.getValueInt(this,ContentBox.KEY_USER_TYPE,3);

        if(type==5){
            offerStrArrs=new String[]{"在线支付","到店支付"};
            offerIndex=1;
        }else{
            offerStrArrs=new String[]{"在线支付"};
            offerIndex=0;
        }


        offerDescTxt.setText(offerStrArrs[offerIndex]);


        offerRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OrderReokActivity.this)
                        .setTitle("支付方式")
                        .setSingleChoiceItems(offerStrArrs, offerIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("offer","choose which "+which);
                                offerIndex=which;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("offer", "cancel which " + which);

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("offer", "ok which " + which);
                        offerDescTxt.setText(offerStrArrs[offerIndex]);
                    }
                }).show();
            }
        });

        couponRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponStrArrs==null||couponStrArrs.length<=0){
                    Toast.makeText(OrderReokActivity.this,"没有可用的优惠券",Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(OrderReokActivity.this)
                        .setTitle("选择优惠券")
                        .setSingleChoiceItems(couponStrArrs, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("sel","choose which "+which);
                                selectIndex=which;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("sel","cancel which "+which);
                        selectIndex=-1;
                        initData();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("sel","ok which "+which);
                        initData();

                    }
                }).show();
            }
        });


        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);
        carId=ContentBox.getValueInt(this, ContentBox.KEY_CAR_ID, 0);
        ac_type_value=getIntent().getIntExtra(Constants.AC_TYPE,
                Constants.AC_TYPE_WASH);

        if(ac_type_value==Constants.AC_TYPE_WASH){
            orderType=Constants.ORDER_TYPE_DECO;
        }else if(ac_type_value==Constants.AC_TYPE_OIL){
            orderType=Constants.ORDER_TYPE_OIL;
        }else{
            orderType=Constants.ORDER_TYPE_META;
        }



        initCustomActionBar();
        initView();

        selItemListView= (ListView) findViewById(R.id.listView3);
        if(ac_type_value==Constants.AC_TYPE_WASH){
            decorationInfoSet= IDataHandler.getInstance().getDecorationInfoSet();

            decorationDelAdapter=new DecorationDelAdapter(decorationInfoSet,this);
            decorationDelAdapter.setMyHandlerCallback(new MyHandlerCallback() {
                @Override
                public void clickAfter() {
                    decorationInfoSet=decorationDelAdapter.getDecorationInfos();
                    //IDataHandler.getInstance().getDecorationInfoSet();
                    initData();
                    decorationDelAdapter.notifyDataSetChanged();
                    setListViewHeight();
                }
            });
            selItemListView.setAdapter(decorationDelAdapter);
        }else if(ac_type_value==Constants.AC_TYPE_OIL){
            oilInfoSet=IDataHandler.getInstance().getOilInfoSet();

            oilInfoDelAdapter=new OilInfoDelAdapter(oilInfoSet,this);
            oilInfoDelAdapter.setMyHandlerCallback(new MyHandlerCallback() {
                @Override
                public void clickAfter() {
                    oilInfoSet=oilInfoDelAdapter.getOilInfos();
                    //IDataHandler.getInstance().getDecorationInfoSet();
                    initData();
                    oilInfoDelAdapter.notifyDataSetChanged();
                    setListViewHeight();
                }
            });
            selItemListView.setAdapter(oilInfoDelAdapter);
        }else if(ac_type_value==Constants.AC_TYPE_META){
            metalplateInfoSet=IDataHandler.getInstance().getMetalplateInfoSet();

            metalInfoDelAdapter=new MetalInfoDelAdapter(metalplateInfoSet,this);
            metalInfoDelAdapter.setMyHandlerCallback(new MyHandlerCallback() {
                @Override
                public void clickAfter() {
                    metalplateInfoSet=metalInfoDelAdapter.getMetalplateInfos();
                    //IDataHandler.getInstance().getDecorationInfoSet();
                    initData();
                    metalInfoDelAdapter.notifyDataSetChanged();
                    setListViewHeight();
                }
            });

            selItemListView.setAdapter(metalInfoDelAdapter);
        }

        if(ac_type_value!=Constants.AC_TYPE_META
                &&ac_type_value!=Constants.AC_TYPE_META2){

            int incre=ContentBox.getValueInt(OrderReokActivity.this,ContentBox.KEY_WAHT_DAY,0);
            String hhmm=ContentBox.getValueString(OrderReokActivity.this, ContentBox.KEY_ORDER_TIME, null);


            orderTime=TimeUtils.createDateFormat(hhmm, incre);
        }


        setListViewHeight();


        initData();

        new DataRequestTask().execute();

    }

    class DataRequestTask extends AsyncTask<String,String,String>{
        List<CouponInfo> allCoupons=new ArrayList<CouponInfo>();
        AlipayInfoType alipayInfoType;
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                allCoupons= WSConnector.getInstance().getCouponList(shopId);

                alipayInfoType=WSConnector.getInstance().getAlipayByShopId(shopId);

            } catch (WSException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
             if(alipayInfoType!=null&&alipayInfoType.getAliPid()!=null&&alipayInfoType.getSellerEmail()!=null){
                 alibabaPay=new AlibabaPay(alipayInfoType.getAliPid(),alipayInfoType.getSellerEmail());
             }else{
                 alibabaPay=null;
             }



             couponInfos=new ArrayList<CouponInfo>();

             if(allCoupons!=null){
                 for (CouponInfo info:allCoupons){
                     if(!TimeUtils.isOverTime(info.getEndTime())&&(info.getIsUsed()!=1)){
                         if(info.getOrderType()==Constants.ORDER_TYPE_ALL
                                 ||info.getOrderType()==orderType){
                             couponInfos.add(info);
                         }
                     }
                 }

                 couponStrArrs=new String[couponInfos.size()];
                 for (int i=0;i<couponInfos.size();i++){
                     CouponInfo info=couponInfos.get(i);
                     String desc="";
                     if(info.getType()==Constants.COUPON_TYPE_DISCOUNT){
                         desc+=info.getDiscount()+"折优惠";
                     }else{
                         desc+="抵扣"+info.getPrice();
                     }

                     couponStrArrs[i]=desc;
                 }

             }
        }
    }

    private void setListViewHeight(){
        ListAdapter listAdapter = selItemListView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, selItemListView);
            if(listItem==null){
                return;
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = selItemListView.getLayoutParams();
        params.height = totalHeight+(selItemListView.getDividerHeight()*(listAdapter.getCount()-1)) ;

        selItemListView.setLayoutParams(params);

    }
    private void initData(){

        totalPrice=0;
        if(ac_type_value==Constants.AC_TYPE_OIL){

            for (OilInfo oilInfo:oilInfoSet){
                totalPrice+=oilInfo.getPrice();
            }

        }else if(ac_type_value==Constants.AC_TYPE_WASH){
            for (DecorationInfo decorationInfo:decorationInfoSet){
                totalPrice+=decorationInfo.getPrice();
            }
        }else if(ac_type_value==Constants.AC_TYPE_META){
            for (MetalplateInfo metalplateInfo:metalplateInfoSet){
                 totalPrice+=metalplateInfo.getPrice();
            }
        }

        promoTxt.setText("");


        if(selectIndex>=0){
             if(couponInfos.size()>selectIndex){

                 CouponInfo couponInfo=couponInfos.get(selectIndex);
                 if(couponInfo.getType()==Constants.COUPON_TYPE_DISCOUNT){
                     promoDescTxt.setText(couponInfo.getDiscount()+"折优惠");
                     float pr=(int)(totalPrice*couponInfo.getDiscount()/10);
                     totalPriceTxt.setText("￥"+pr);
                     promoTxt.setText("已优惠￥"+(int)(totalPrice-pr));
                     totalPrice=(int)pr;
                 }else{
                     promoDescTxt.setText("优惠"+couponInfo.getPrice());
                     totalPrice=(int)(totalPrice-couponInfo.getPrice());
                     if(totalPrice<0){
                         totalPrice=0;
                     }
                     totalPriceTxt.setText("￥"+totalPrice);
                     promoTxt.setText("已优惠￥"+couponInfo.getPrice());
                 }

             }


        }else{
            totalPriceTxt.setText(totalPrice+"元");
            promoDescTxt.setText("");
        }


    }
    private void initView(){
        promoTxt= (TextView) findViewById(R.id.textView11);
        totalPriceTxt= (TextView) findViewById(R.id.textView13);
        createOrderBtn= (Button) findViewById(R.id.button2);
        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carId<=0){
                    Toast.makeText(OrderReokActivity.this,"没有车牌信息，请添加车牌",Toast.LENGTH_SHORT).show();
                    return ;
                }


                if(ac_type_value==Constants.AC_TYPE_WASH){
                    if(decorationInfoSet!=null&&decorationInfoSet.size()>0){
                        new OrderCommitTask(OrderReokActivity.this).execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                    if(oilInfoSet!=null&&oilInfoSet.size()>0){
                        new OrderCommitTask(OrderReokActivity.this).execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }else  if(ac_type_value==Constants.AC_TYPE_META){
                    if(metalplateInfoSet!=null&&metalplateInfoSet.size()>0){
                        new OrderCommitTask(OrderReokActivity.this).execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    class OrderCommitTask extends AsyncTask<String,String,String>{
        Activity ctx;
        public OrderCommitTask(Activity ctx){
            this.ctx=ctx;
        }
        KProgressHUD progressHUD;
        AlipayInfo alipayInfo;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("订单提交中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            int couponId=0;
            String detail="";
            if(selectIndex>=0){
                couponId=couponInfos.get(selectIndex).getId();
            }

            if(ac_type_value==Constants.AC_TYPE_WASH){
                DecoOrderInfo decoOrderInfo=new DecoOrderInfo(0,offerIndex,
                        Constants.STATE_ORDER_UNFINISHED,
                        Constants.PAY_STATE_UNFINISHED,0,carId,shopId,0,totalPrice,couponId,null,null,orderTime,null);

                try {
                    decoOrderInfo=WSConnector.getInstance().createDecoOrder(decoOrderInfo);



                    for (DecorationInfo decorationInfo :decorationInfoSet){
                        detail+=decorationInfo.getName()+"\n";
                        WSConnector.getInstance().createDecoOrderNumber(decorationInfo.getId(),decoOrderInfo.getId());

                    }

                    alipayInfo=new AlipayInfo("洗车美容",detail,totalPrice+"",decoOrderInfo.getOut_trade_no());

                    if(alibabaPay!=null){
                        String sign=WSConnector.getInstance().signContent(shopId,alibabaPay.getOrderInfo(alipayInfo));
                        alipayInfo.setSign(sign);

                    }

                } catch (WSException e) {
                    return e.getErrorMsg();
                }
            }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                OilOrderInfo oilOrderInfo=new OilOrderInfo(0,offerIndex,
                        Constants.STATE_ORDER_UNFINISHED,
                        Constants.PAY_STATE_UNFINISHED,0,carId,shopId,0,totalPrice,couponId,null,null,orderTime,null);

                try {
                    oilOrderInfo=WSConnector.getInstance().createOilOrder(oilOrderInfo);

                    for (OilInfo oilInfo :oilInfoSet){
                        detail+=oilInfo.getName()+"\n";
                        WSConnector.getInstance().createOilOrderNumber(oilOrderInfo.getId(),oilInfo.getId());

                    }
                    alipayInfo=new AlipayInfo("换油保养",detail,totalPrice+"",oilOrderInfo.getOut_trade_no());
                    if(alibabaPay!=null){
                        String sign=WSConnector.getInstance().signContent(shopId,alibabaPay.getOrderInfo(alipayInfo));
                        alipayInfo.setSign(sign);

                    }

                } catch (WSException e) {
                    return e.getErrorMsg();
                }

            }else if(ac_type_value==Constants.AC_TYPE_META){
                MetaOrderInfo metaOrderInfo=new MetaOrderInfo(0,offerIndex,
                        Constants.STATE_ORDER_UNFINISHED,Constants.PAY_STATE_UNFINISHED,0,
                carId,shopId,0,totalPrice, couponId,null,null, null,
                        null,
                       null);

                try {
                    metaOrderInfo=WSConnector.getInstance().createMetaOrder(metaOrderInfo);

                    for (MetalplateInfo metalplateInfo :metalplateInfoSet){

                        WSConnector.getInstance().createMetaOrderNumber(metaOrderInfo.getId(),metalplateInfo.getId(),metalplateInfo.getCount());
                        detail+=metalplateInfo.getName()+"\n";
                    }
                    alipayInfo=null;
                    alibabaPay=null;

                } catch (WSException e) {
                    return e.getErrorMsg();
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            boolean orderIsOk=false;
            progressHUD.dismiss();
            if(s==null){
                orderIsOk=true;
            }else {
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            }

            if(offerIndex==0){

                if(alipayInfo!=null&&alibabaPay!=null&&alipayInfo.getSign()!=null){
                   final String payinfo=alibabaPay.getPayInfoData(alipayInfo);

                   Runnable payRunnable = new Runnable() {

                       @Override
                       public void run() {
                           // 构造PayTask 对象
                           PayTask alipay = new PayTask(ctx);
                           // 调用支付接口，获取支付结果
                           String result = alipay.pay(payinfo, true);

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
                    Toast.makeText(ctx,"在线支付错误,请重试",Toast.LENGTH_SHORT).show();
                }





            }else{

                Intent intent=new Intent(OrderReokActivity.this,OrderResultActivity.class);

                intent.putExtra(Constants.AC_TYPE,ac_type_value);
                intent.putExtra(Constants.ORDER_RESULT_IS_OK,orderIsOk);
                intent.putExtra(Constants.OFFER_PRICE,totalPrice);
                intent.putExtra("Title","");
                startActivity(intent);
                finish();

            }



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100&&resultCode>0){
            ContentBox.loadInt(this,ContentBox.KEY_CAR_ID,resultCode);
            carId=ContentBox.getValueInt(this, ContentBox.KEY_CAR_ID, 0);
            rightBtn.setText(data.getStringExtra("number"));
        }
    }



}
