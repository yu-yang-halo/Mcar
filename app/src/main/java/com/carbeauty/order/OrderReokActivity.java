package com.carbeauty.order;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.adapter.DecorationDelAdapter;
import com.carbeauty.adapter.MetalInfoDelAdapter;
import com.carbeauty.adapter.MyHandlerCallback;
import com.carbeauty.adapter.OilInfoDelAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.dialog.AcCarSelectDialog;

import java.util.List;

import cn.service.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;
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
    Button  createOrderBtn;
    int shopId;
    int carId;

    float totalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_orderreok);
        shopId= ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID, 0);
        carId=ContentBox.getValueInt(this,ContentBox.KEY_CAR_ID,0);

        initCustomActionBar();
        initView();

        ac_type_value=getIntent().getIntExtra(Constants.AC_TYPE,
                Constants.AC_TYPE_WASH);
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

        setListViewHeight();


        initData();

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

        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
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
        totalPriceTxt.setText(totalPrice+"元");
        promoTxt.setText("");


    }
    private void initView(){
        promoTxt= (TextView) findViewById(R.id.textView11);
        totalPriceTxt= (TextView) findViewById(R.id.textView13);
        createOrderBtn= (Button) findViewById(R.id.button2);
        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ac_type_value==Constants.AC_TYPE_WASH){
                    if(decorationInfoSet!=null&&decorationInfoSet.size()>0){
                        new OrderCommitTask().execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                    if(oilInfoSet!=null&&oilInfoSet.size()>0){
                        new OrderCommitTask().execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }else  if(ac_type_value==Constants.AC_TYPE_META){
                    if(metalplateInfoSet!=null&&metalplateInfoSet.size()>0){
                        new OrderCommitTask().execute();
                    }else{
                        Toast.makeText(OrderReokActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    class OrderCommitTask extends AsyncTask<String,String,String>{
        private String orderTime;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           int incre=ContentBox.getValueInt(OrderReokActivity.this,ContentBox.KEY_WAHT_DAY,0);
           String hhmm=ContentBox.getValueString(OrderReokActivity.this, ContentBox.KEY_ORDER_TIME, null);

           orderTime=TimeUtils.createDateFormat(hhmm,incre);


        }

        @Override
        protected String doInBackground(String... params) {
            if(ac_type_value==Constants.AC_TYPE_WASH){

                DecoOrderInfo decoOrderInfo=new DecoOrderInfo(0,Constants.TYPE_PAY_TOSHOP,
                        Constants.STATE_ORDER_UNFINISHED,
                        Constants.PAY_STATE_UNFINISHED,0,carId,shopId,0,totalPrice,0,null,null,orderTime,null);

                try {
                    decoOrderInfo=WSConnector.getInstance().createDecoOrder(decoOrderInfo);

                    for (DecorationInfo decorationInfo :decorationInfoSet){

                        WSConnector.getInstance().createDecoOrderNumber(decorationInfo.getId(),decoOrderInfo.getId());

                    }

                } catch (WSException e) {
                    return e.getErrorMsg();
                }
            }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                OilOrderInfo oilOrderInfo=new OilOrderInfo(0,Constants.TYPE_PAY_TOSHOP,
                        Constants.STATE_ORDER_UNFINISHED,
                        Constants.PAY_STATE_UNFINISHED,0,carId,shopId,0,totalPrice,0,null,null,orderTime,null);

                try {
                    oilOrderInfo=WSConnector.getInstance().createOilOrder(oilOrderInfo);

                    for (OilInfo oilInfo :oilInfoSet){

                        WSConnector.getInstance().createOilOrderNumber(oilOrderInfo.getId(),oilInfo.getId());

                    }

                } catch (WSException e) {
                    return e.getErrorMsg();
                }

            }else if(ac_type_value==Constants.AC_TYPE_META){
                MetaOrderInfo metaOrderInfo=new MetaOrderInfo(0,Constants.TYPE_PAY_TOSHOP,
                        Constants.STATE_ORDER_UNFINISHED,Constants.PAY_STATE_UNFINISHED,0,
                carId,shopId,0,totalPrice, 0,null,null, null,
                        null,
                       null);

                try {
                    metaOrderInfo=WSConnector.getInstance().createMetaOrder(metaOrderInfo);

                    for (MetalplateInfo metalplateInfo :metalplateInfoSet){

                        WSConnector.getInstance().createMetaOrderNumber(metaOrderInfo.getId(),metalplateInfo.getId(),metalplateInfo.getCount());

                    }

                } catch (WSException e) {
                    return e.getErrorMsg();
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                Toast.makeText(OrderReokActivity.this,"订单提交成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(OrderReokActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100&&resultCode>0){
            ContentBox.loadInt(this,ContentBox.KEY_CAR_ID,resultCode);
            rightBtn.setText(data.getStringExtra("number"));
        }
    }
}
