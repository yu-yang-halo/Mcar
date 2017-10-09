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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.BaseActivity;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.adapter.MoneyAdapter;
import com.carbeauty.adapter.OrderTimeAdapter;
import com.carbeauty.alertDialog.WindowUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.OrderReokActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pay.AlibabaPay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TextView couponLabel;
    TextView deviceNameLabel;
    GridView moneyGV;
    int shopId;
    List<CouponInfo> couponInfos;
    String[]  couponStrArrs;
    int selectIndex=-1;
    int moneyValue=-1;
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





        RelativeLayout couponRelayout= (RelativeLayout) findViewById(R.id.couponRelayout);

        moneyGV= (GridView) findViewById(R.id.moneyGV);
        couponLabel= (TextView) findViewById(R.id.textView52);
        deviceNameLabel= (TextView) findViewById(R.id.deviceName);
        btnOrder= (Button) findViewById(R.id.button8);


        gridViewInit();

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
                if(moneyValue<0){
                    Toast.makeText(PayActivity.this,"请选择洗车金额",Toast.LENGTH_SHORT).show();
                    return;
                }

                couponId=couponInfos.get(selectIndex).getId();
                new DataRequestTask(1).execute();

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
                        couponLabel.setText("");
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("sel","ok which "+which);
                        if(selectIndex<0){
                            showMessage("请选择优惠券");
                            return;
                        }
                        couponLabel.setText(couponStrArrs[selectIndex]);
                    }
                }).show();
            }
        });

        new DataRequestTask(0).execute();



    }

    private void gridViewInit(){
        String[] arrs= null;
        List<String> listArrs=new ArrayList<>();

        for(int i=1;i<=25;i++){
            listArrs.add(i+"元");
        }

        arrs=listArrs.toArray(new String[]{});


        final MoneyAdapter moneyAdapter=new MoneyAdapter(arrs,this);

        moneyGV.setAdapter(moneyAdapter);
        moneyGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos","pos "+position+" "+view);
                moneyAdapter.setSelPos(position);

                moneyValue=position+1;

            }
        });
    }
    public void showPickerNumber() {
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(1, 3, 1);//数字范围
        picker.setSelectedItem(3);
        picker.setLabel("元");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                moneyValue=0;
                try {
                    moneyValue=Integer.parseInt(option);
                }catch (Exception e){

                }
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
        String message;
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
                    WSConnector.getInstance().createDeviceOrder(deviceId,couponId,moneyValue,Constants.PAY_TYPE_COUPON,moneyValue);
                } catch (WSException e) {

                    message=e.getMessage();

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
                        String desc="券号:"+info.getNumber()+" 余额:"+info.getBalance();
                        couponStrArrs[i]=desc;
                    }

                }
            }else{

                if(progressHUD!=null){
                    progressHUD.dismiss();
                }

                if(message==null){
                    message="下单完成,请立即使用";
                    finish();
                }
                Toast.makeText(PayActivity.this,message,Toast.LENGTH_SHORT).show();

            }


        }
    }


}
