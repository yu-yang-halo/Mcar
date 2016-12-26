package com.carbeauty.auto;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.BaseActivity;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.OrderReokActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pay.AlibabaPay;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.AlipayInfoType;
import cn.service.bean.CouponInfo;

/**
 * Created by Administrator on 2016/12/16.
 */

public class PayActivity extends BaseActivity {
    public static final String KEY_DEVICE_ID="device_ID";
    public static final String KEY_DEVICE_NAME="device_NAME";
    NumberPicker picker;
    TextView timeLabel;
    TextView couponLabel;
    TextView deviceNameLabel;
    int shopId;
    List<CouponInfo> couponInfos;
    String[]  couponStrArrs;
    int selectIndex=-1;
    int timeValue=-1;
    Button btnOrder;
    int deviceId=-1;
    int couponId=-1;
    String deviceName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pay);
        initCustomActionBar();

        deviceId=getIntent().getIntExtra(KEY_DEVICE_ID,-1);
        deviceName=getIntent().getStringExtra(KEY_DEVICE_NAME);




        RelativeLayout relayout= (RelativeLayout) findViewById(R.id.relayout);
        RelativeLayout couponRelayout= (RelativeLayout) findViewById(R.id.couponRelayout);

        timeLabel= (TextView) findViewById(R.id.textView53);
        couponLabel= (TextView) findViewById(R.id.textView52);
        deviceNameLabel= (TextView) findViewById(R.id.deviceName);
        btnOrder= (Button) findViewById(R.id.button8);

        if(deviceName!=null){
            deviceNameLabel.setText(deviceName);
        }



        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);
        titleLabel.setText("自助洗服务");


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectIndex<0){
                    Toast.makeText(PayActivity.this,"请选择优惠券",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(timeValue<0){
                    Toast.makeText(PayActivity.this,"请选择洗车时间",Toast.LENGTH_SHORT).show();
                    return;
                }

                couponId=couponInfos.get(selectIndex).getId();
                new DataRequestTask(1).execute();

            }
        });

        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerNumber();
            }
        });

        couponRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponStrArrs==null||couponStrArrs.length<=0){
                    Toast.makeText(PayActivity.this,"没有可用的优惠券",Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(PayActivity.this)
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
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("sel","ok which "+which);
                        couponLabel.setText(couponStrArrs[selectIndex]);
                    }
                }).show();
            }
        });

        new DataRequestTask(0).execute();



    }
    public void showPickerNumber() {
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(1, 20, 1);//数字范围
        picker.setSelectedItem(15);
        picker.setLabel("分钟");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                timeValue=item.intValue();
                timeLabel.setText(item.intValue()+"分钟");
            }
        });
        picker.show();
    }

    class DataRequestTask extends AsyncTask<String,String,String> {
        int type;
        KProgressHUD progressHUD;
        DataRequestTask(int type){
            this.type=type;
        }
        List<CouponInfo> allCoupons;
        @Override
        protected void onPreExecute() {

            if(type!=0){
                progressHUD= KProgressHUD.create(PayActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("下单中...")
                        .setAnimationSpeed(1)
                        .setDimAmount(0.3f)
                        .show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(type==0){
                try {
                    allCoupons= WSConnector.getInstance().getCouponList(shopId);

                } catch (WSException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    WSConnector.getInstance().createDeviceOrder(deviceId,couponId,1,Constants.PAY_TYPE_COUPON);
                } catch (WSException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(type==0){

                couponInfos=new ArrayList<CouponInfo>();
                if(allCoupons!=null){
                    for (CouponInfo info:allCoupons){
                        if(!TimeUtils.isOverTime(info.getEndTime())&&(info.getIsUsed()!=1)){
                            if(info.getOrderType()== Constants.ORDER_TYPE_AUTO){
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
            }else{

                if(progressHUD!=null){
                    progressHUD.dismiss();
                    Toast.makeText(PayActivity.this,"下单完成,请立即使用",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }


        }
    }


}
