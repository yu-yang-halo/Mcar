package com.carbeauty.good;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.UserAddressManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.order.HeaderActivity;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/5/6.
 */
public class MyAddressActivity extends HeaderActivity {
    EditText nametxt;
    EditText phoneTxt;
    EditText addressTxt;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_myaddress);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        nametxt= (EditText) findViewById(R.id.nametxt);
        phoneTxt= (EditText) findViewById(R.id.phoneTxt);
        addressTxt= (EditText) findViewById(R.id.addressTxt);
        textView= (TextView) findViewById(R.id.textView57);


        nametxt.setText(ContentBox.getValueString(this, UserAddressManager.KEY_NAME, ""));
        phoneTxt.setText(ContentBox.getValueString(this, UserAddressManager.KEY_PHONE, ""));
        addressTxt.setText(ContentBox.getValueString(this, UserAddressManager.KEY_ADDRESS, ""));

        int shopId=ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID,Integer.MIN_VALUE);
        ShopInfo mShopInfo=IDataHandler.getInstance().getShopInfo(shopId);

        if(mShopInfo!=null){

            textView.setText("服务店铺:"+mShopInfo.getName()+"\n"+mShopInfo.getDesc());

        }



    }

    @Override
    public void onBackPressed() {
        final String receivingInfo=UserAddressManager.cacheAddressToLocal(MyAddressActivity.this,addressTxt.getText().toString()
                ,nametxt.getText().toString(),phoneTxt.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    UserInfo userInfo=WSConnector.getInstance().getUserInfoById();
                    userInfo.setReceivingInfo(receivingInfo);
                    WSConnector.getInstance().updUser(userInfo);
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        finish();
    }
}
