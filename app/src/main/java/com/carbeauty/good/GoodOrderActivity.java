package com.carbeauty.good;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.UserAddressManager;
import com.carbeauty.adapter.GoodLookAdapter;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;
import com.carbeauty.order.OrderResultActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pay.AlibabaPay;
import com.pay.AlipayInfo;
import com.pay.PayResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.AlipayInfoType;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodOrderActivity extends FragmentActivity {
    ListView goodslistview;

    ActionBar mActionbar;

    CheckBox checkAll;
    TextView totalPriceLabel;
    Button createOrderButton;
    GoodLookAdapter goodLookAdapter;
    Button rightBtn;

    String address;
    String name;
    String phone;

    TextView addressDescription;
    List<CartManager.MyCartClass> myCartClassList;
    int shopId;
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
                        Toast.makeText(GoodOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();


                        Iterator<CartManager.MyCartClass> iterator=myCartClassList.iterator();
                        while (iterator.hasNext()){
                            CartManager.MyCartClass myCartClass=iterator.next();
                            if(myCartClass.isCheckYN()){
                                iterator.remove();
                            }
                        }

                        CartManager.getInstance().cacheMyCartClassToDisk(GoodOrderActivity.this);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(GoodOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(GoodOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.ac_goodsorder);
        initCustomActionBar();
        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);



        goodslistview= (ListView) findViewById(R.id.goodslistview);



        checkAll= (CheckBox) findViewById(R.id.checkBox3);
        totalPriceLabel= (TextView) findViewById(R.id.textView13);

        createOrderButton= (Button) findViewById(R.id.button2);

        addressDescription= (TextView) findViewById(R.id.textView4);

        myCartClassList=CartManager.getInstance().getMyCartClassList(this);

        Iterator<CartManager.MyCartClass> iterator=myCartClassList.iterator();
        while (iterator.hasNext()){
            CartManager.MyCartClass myCartClass=iterator.next();
            if(myCartClass.getGoodInfo().getShopId()!=-1&&myCartClass.getGoodInfo().getShopId()!=shopId){
                iterator.remove();
            }
        }

        CartManager.getInstance().cacheMyCartClassToDisk(GoodOrderActivity.this);




        goodLookAdapter=new GoodLookAdapter(this,myCartClassList);

        goodLookAdapter.setTotalPriceTxt(totalPriceLabel);

        goodslistview.setAdapter(goodLookAdapter);

        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                goodLookAdapter.selectAll(isChecked);

            }
        });

        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (address.equals("") || name.equals("") || phone.equals("")) {
                    Toast.makeText(GoodOrderActivity.this, "请添加我的地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (goodLookAdapter.getCommitData()==null||goodLookAdapter.getCommitData().size() == 0) {
                    Toast.makeText(GoodOrderActivity.this, "请选择购买的商品", Toast.LENGTH_SHORT).show();
                } else if (name.trim().equals("") || address.trim().equals("") || phone.trim().equals("")) {
                    Toast.makeText(GoodOrderActivity.this, "地址/姓名/手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    new CommitOrderTask(GoodOrderActivity.this,goodLookAdapter.getCommitData()).execute();
                }


            }
        });
        addressDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddress();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        address=ContentBox.getValueString(GoodOrderActivity.this, UserAddressManager.KEY_ADDRESS, "");
        name=ContentBox.getValueString(GoodOrderActivity.this,  UserAddressManager.KEY_NAME, "");
        phone=ContentBox.getValueString(GoodOrderActivity.this,  UserAddressManager.KEY_PHONE, "");
        if(!address.equals("")&&!name.equals("")&&!phone.equals("")){
            addressDescription.setText("地址:"+address+"\n收货人姓名:"+name+" 联系电话:"+phone);
        }

    }

    class CommitOrderTask extends AsyncTask<String,String,String>{
        List<GoodLookAdapter.CommitDataBean> commitDataBeanList;
        KProgressHUD progressHUD;
        AlibabaPay alibabaPay;
        AlipayInfo  alipayInfo;
        Activity ctx;

        CommitOrderTask(Activity ctx,List<GoodLookAdapter.CommitDataBean> commitDataBeanList){
            this.commitDataBeanList=commitDataBeanList;
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("订单提交中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {

            int size=commitDataBeanList.size();

            int index=0;
            String descontent="";

            String out_trade_no="";
            AlipayInfoType alipayInfoType = null;

            try {
                alipayInfoType=WSConnector.getInstance().getAlipayByShopId(-1);
            } catch (WSException e) {
                e.printStackTrace();
            }


            for (GoodLookAdapter.CommitDataBean dataBean:commitDataBeanList){
                try {
                  if(size==1){
                      Map<String,Object> retObject=WSConnector.getInstance().createGoodsOrder(dataBean.getData()
                              ,dataBean.getShopId()
                              ,dataBean.getTotalPrice(),address,name,phone,"0",shopId);

                      if(retObject.get("out_trade_no")!=null){
                          out_trade_no=(String)retObject.get("out_trade_no");
                      }


                  }else{

                      if(index<size-1){
                          Map<String,Object> retObject= WSConnector.getInstance().createGoodsOrder(dataBean.getData()
                                  ,dataBean.getShopId()
                                  ,dataBean.getTotalPrice(),address,name,phone,null,shopId);

                          if(index==size-2){
                              descontent+=retObject.get("id");
                          }else{
                              descontent+=retObject.get("id")+",";
                          }

                      }else{
                          Map<String,Object> retObject=WSConnector.getInstance().createGoodsOrder(dataBean.getData()
                                  ,dataBean.getShopId()
                                  ,dataBean.getTotalPrice(),address,name,phone,descontent,shopId);
                          if(retObject.get("out_trade_no")!=null){
                              out_trade_no=(String)retObject.get("out_trade_no");
                          }

                      }

                      index++;
                  }

                } catch (WSException e) {
                    e.printStackTrace();
                }
            }


            System.out.println("out_trade_no ::: "+out_trade_no);

            alipayInfo=new AlipayInfo("客乐养车坊商品","商品订单编号",goodLookAdapter.getLastestNewPrice()+"",out_trade_no);
            if(alipayInfoType!=null&&alipayInfoType.getAliPid()!=null&&alipayInfoType.getSellerEmail()!=null){
                alibabaPay=new AlibabaPay(alipayInfoType.getAliPid(),alipayInfoType.getSellerEmail());
            }

            if(alibabaPay!=null){
                String sign= null;
                try {
                    sign = WSConnector.getInstance().signContent(-1,alibabaPay.getOrderInfo(alipayInfo));
                    alipayInfo.setSign(sign);
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            boolean orderIsOk=false;
            progressHUD.dismiss();


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



//            Intent intent=new Intent(GoodOrderActivity.this,OrderResultActivity.class);
//
//            intent.putExtra(Constants.AC_TYPE,Constants.AC_TYPE_GOOD);
//            intent.putExtra(Constants.ORDER_RESULT_IS_OK,orderIsOk);
//            intent.putExtra("Title","");
//            startActivity(intent);
//            finish();
        }
    }

    protected boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home2);

        Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);

        TextView titleTxt=(TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        titleTxt.setText(getIntent().getStringExtra("Title"));

        leftBtn.setText("返回");
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightBtn.setText("我的地址");
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddress();
            }
        });





        return true;
    }
    private void toAddress(){
        Intent intent = new Intent(GoodOrderActivity.this, MyAddressActivity.class);
        intent.putExtra("Title", "我的地址");
        startActivity(intent);
    }
}
