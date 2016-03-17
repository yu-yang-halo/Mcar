package com.carbeauty.order;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.adapter.DecorationAdapter;
import com.carbeauty.adapter.MyHandlerCallback;
import com.carbeauty.adapter.OilInfoAdapter;
import com.carbeauty.adapter.OrderTimeAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.dialog.AcCarSelectDialog;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.service.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;
import cn.service.bean.DecorationInfo;
import cn.service.bean.OilInfo;
import cn.service.bean.OrderStateType;

/**
 * Created by Administrator on 2016/3/10.
 */
public class WashOilActivity extends Activity {


    private int ac_type_value=0;

    private ActionBar mActionbar;
    private TextView tvTitle;
    private int shopId;
    private GridView gridView;
    private ListView listView;
    TextView itemTotalPrice;
    TextView itemNums;
    Button bOrderBtn;

    Button rightBtn;

    DecorationAdapter decorationAdapter;
    OilInfoAdapter oilInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_washoil);
        ac_type_value=getIntent().getIntExtra(Constants.AC_TYPE,Constants.AC_TYPE_WASH);
        initCustomActionBar();

        shopId= ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID, 0);

        gridView= (GridView) findViewById(R.id.gridView2);
        listView= (ListView) findViewById(R.id.listView2);

        itemTotalPrice= (TextView) findViewById(R.id.itemTotalPrice);
        itemNums= (TextView) findViewById(R.id.itemNums);
        bOrderBtn= (Button) findViewById(R.id.bOrderBtn);

        bOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String selTime=ContentBox.getValueString(WashOilActivity.this, ContentBox.KEY_ORDER_TIME,null);
                if(selTime==null){
                    Toast.makeText(WashOilActivity.this,"请选择预定时间",Toast.LENGTH_SHORT).show();
                }else if(ac_type_value==Constants.AC_TYPE_WASH){
                    if(decorationAdapter.getDecoCollections().size()==0){
                        Toast.makeText(WashOilActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }else{
                        //commit
                        Intent intent=new Intent(WashOilActivity.this,OrderReokActivity.class);
                        intent.putExtra("Title",getIntent().getStringExtra("Title"));
                        intent.putExtra(Constants.AC_TYPE,ac_type_value);
                        IDataHandler.getInstance().setDecorationInfoSet(decorationAdapter.getDecoCollections());
                        startActivity(intent);
                    }
                }else if(ac_type_value==Constants.AC_TYPE_OIL){
                    if(oilInfoAdapter.getOilCollections().size()==0){
                        Toast.makeText(WashOilActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }else{
                        //commit
                        Intent intent=new Intent(WashOilActivity.this,OrderReokActivity.class);
                        intent.putExtra(Constants.AC_TYPE, ac_type_value);
                        intent.putExtra("Title",getIntent().getStringExtra("Title"));
                        IDataHandler.getInstance().setOilInfoSet(oilInfoAdapter.getOilCollections());
                        startActivity(intent);

                    }
                }

            }
        });


        new GetDataList().execute();

    }

    private void initGridView(List<OrderStateType> orderStateTypes){

        OrderTimeAdapter orderTimeAdapter=new OrderTimeAdapter(orderStateTypes,this);

        gridView.setAdapter(orderTimeAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData(){
        float total=0;
        int data=0;

        if(ac_type_value==Constants.AC_TYPE_WASH){
            if(decorationAdapter==null){
                return;
            }
            List<DecorationInfo> decorationInfoSet=decorationAdapter.getDecoCollections();
            for (DecorationInfo decorationInfo:decorationInfoSet){
                total+=decorationInfo.getPrice();
                data++;
            }
            decorationAdapter.notifyDataSetChanged();
        }else if(ac_type_value==Constants.AC_TYPE_OIL){
            if(oilInfoAdapter==null){
                return;
            }
            List<OilInfo> oilInfoSet=oilInfoAdapter.getOilCollections();

            for (OilInfo oilInfo:oilInfoSet){
                total+=oilInfo.getPrice();
                data++;
            }
            oilInfoAdapter.notifyDataSetChanged();
        }
        itemNums.setText(data+"");
        itemTotalPrice.setText(total+"元");

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
                    Intent intent=new Intent(WashOilActivity.this, AcCarSelectDialog.class);
                    startActivityForResult(intent,100);
                }
            });

        }
    }
    private void initDecorationListView(List<DecorationInfo> decorationInfos){
        decorationAdapter=new DecorationAdapter(decorationInfos,this);
        decorationAdapter.setMyHandlerCallback(new MyHandlerCallback() {
            @Override
            public void clickAfter() {
                initData();
            }
        });
        listView.setAdapter(decorationAdapter);
    }
    private void initOilInfoListView(List<OilInfo> oilInfos){
        oilInfoAdapter=new OilInfoAdapter(oilInfos,this);
        oilInfoAdapter.setMyHandlerCallback(new MyHandlerCallback() {
            @Override
            public void clickAfter() {
                initData();
            }
        });
        listView.setAdapter(oilInfoAdapter);

    }



    class GetDataList extends AsyncTask<String,String,String>{
        List<OrderStateType> orderStateTypes;
        List<OilInfo> oilInfos;
        List<DecorationInfo> decorationInfos;
        @Override
        protected String doInBackground(String... params) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd+HH+mm+ss");
            Date date=new Date();
            String time=sdf.format(date);
            try {
                orderStateTypes=WSConnector.getInstance().getDayOrderStateList(Constants.SEARCH_TYPE_DECO, shopId, time);
                if(ac_type_value==Constants.AC_TYPE_OIL){
                    oilInfos=WSConnector.getInstance().getOilList(shopId);
                }else  if(ac_type_value==Constants.AC_TYPE_WASH){
                    decorationInfos=WSConnector.getInstance().getDecorationList(shopId);
                }

            } catch (WSException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            initGridView(orderStateTypes);
            if(ac_type_value==Constants.AC_TYPE_WASH){
                initDecorationListView(decorationInfos);
            }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                initOilInfoListView(oilInfos);
            }

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
