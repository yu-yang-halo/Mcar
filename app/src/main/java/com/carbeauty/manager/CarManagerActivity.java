package com.carbeauty.manager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.adapter.CarInfoAdapter;
import com.carbeauty.dialog.AcCarInfoDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class CarManagerActivity extends Activity{
    ListView carlistView;
    ActionBar mActionbar;
    TextView  tvTitle;
    List<CarInfo> carInfos;
    CarInfoAdapter carInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_carinfo);
        carlistView= (ListView) findViewById(R.id.carlist);



        initCustomActionBar();


        new RefreshCarInfoTask(0,null).execute();


    }

    private void initListView(){
        carInfoAdapter=new CarInfoAdapter(carInfos,this);
        carlistView.setAdapter(carInfoAdapter);
        carlistView.setDividerHeight(1);
    }


    private boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home2);
        tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        tvTitle.setText("我的车牌");

        Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("添加");
        rightBtn.setVisibility(View.VISIBLE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CarManagerActivity.this, AcCarInfoDialog.class);
                startActivityForResult(intent,1000);

            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode>0){
            if(data.getStringExtra("name").trim().equals("")){
                Toast.makeText(this,"车牌号不能为空",Toast.LENGTH_SHORT).show();
            }else{
                new RefreshCarInfoTask(1,data.getStringExtra("name")).execute();
            }
        }

    }

    class RefreshCarInfoTask extends AsyncTask<String,String,String>{

        int type;
        String carNumber;

        KProgressHUD progressHUD;
        RefreshCarInfoTask(int type,String carNumber){
            this.type=type;
            this.carNumber=carNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD= KProgressHUD.create(CarManagerActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("加载中...")
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
            if(type==1){
                CarInfo carInfo=new CarInfo(0,0,carNumber,0);
                WSConnector.getInstance().createCar(carInfo);
            }
                carInfos=WSConnector.getInstance().getCarByUserId();
            } catch (WSException e) {
                return e.getErrorMsg();
            } catch (UnsupportedEncodingException e) {
                return e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s!=null){
                Toast.makeText(CarManagerActivity.this,s,Toast.LENGTH_SHORT).show();
            }else{
                if(type==1){
                  if(carInfoAdapter!=null){
                      carInfoAdapter.setCarInfos(carInfos);
                      carInfoAdapter.notifyDataSetChanged();
                  }
                }else{
                    initListView();
                }

            }
        }
    }
}
