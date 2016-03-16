package com.carbeauty.order;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.carbeauty.R;
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
import cn.service.bean.MetalplateInfo;
import cn.service.bean.OilInfo;

/**
 * Created by Administrator on 2016/3/12.
 */
public class OrderReokActivity extends Activity {
    int ac_type_value;
    ActionBar mActionbar;
    TextView tvTitle;
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
    Button rightBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_orderreok);
        shopId= ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID, 0);
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

                    //IDataHandler.getInstance().getDecorationInfoSet();
                    initData();
                    decorationDelAdapter.notifyDataSetChanged();
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
                }
            });
            selItemListView.setAdapter(oilInfoDelAdapter);
        }else if(ac_type_value==Constants.AC_TYPE_META){
            metalplateInfoSet=IDataHandler.getInstance().getMetalplateInfoSet();

            metalInfoDelAdapter=new MetalInfoDelAdapter(metalplateInfoSet,this);
            metalInfoDelAdapter.setMyHandlerCallback(new MyHandlerCallback() {
                @Override
                public void clickAfter() {

                }
            });

            selItemListView.setAdapter(metalInfoDelAdapter);
        }




        initData();

    }
    private void initData(){

        float totalPrice=0;
        if(ac_type_value==Constants.AC_TYPE_OIL){

            for (OilInfo oilInfo:oilInfoSet){
                totalPrice+=oilInfo.getPrice();
            }

        }else if(ac_type_value==Constants.AC_TYPE_WASH){
            for (DecorationInfo decorationInfo:decorationInfoSet){
                totalPrice+=decorationInfo.getPrice();
            }
        }
        totalPriceTxt.setText(totalPrice+"元");
        promoTxt.setText("");

        List<CarInfo> carInfos=IDataHandler.getInstance().getCarInfos();

        if(carInfos!=null&&carInfos.size()>0){
            CarInfo selCar=null;
            for (CarInfo carInfo:carInfos){
                if(ContentBox.getValueInt(this,ContentBox.KEY_CAR_ID,-1)==carInfo.getId()){
                    selCar=carInfo;
                }
            }
            if(selCar==null){
                selCar=carInfos.get(0);
            }
            rightBtn.setText(selCar.getNumber());
            ContentBox.loadInt(this,ContentBox.KEY_CAR_ID,selCar.getId());
        }
        if(carInfos.size()>1){
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(OrderReokActivity.this, AcCarSelectDialog.class);
                    startActivityForResult(intent,100);
                }
            });

        }
    }
    private void initView(){
        promoTxt= (TextView) findViewById(R.id.textView11);
        totalPriceTxt= (TextView) findViewById(R.id.textView13);
        createOrderBtn= (Button) findViewById(R.id.button2);
        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class OrderCommitTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            if(ac_type_value==Constants.AC_TYPE_WASH){
                DecoOrderInfo decoOrderInfo=new DecoOrderInfo(0,0, 0, 0,0,
                0, shopId,0,0, 0,
                null,null,null,
                        null);
                try {
                    WSConnector.getInstance().createDecoOrder(decoOrderInfo);
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }else  if(ac_type_value==Constants.AC_TYPE_OIL){

            }
            return null;
        }
    }

    private boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home1);
        tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        tvTitle.setText(getIntent().getStringExtra("Title"));

        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);


        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100&&resultCode>0){
            ContentBox.loadInt(this,ContentBox.KEY_CAR_ID,resultCode);
            rightBtn.setText(data.getStringExtra("number"));
        }
    }
}
