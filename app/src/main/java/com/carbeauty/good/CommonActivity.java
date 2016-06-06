package com.carbeauty.good;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;

import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CommonActivity extends FragmentActivity {
    ActionBar mActionbar;
    Button cartButton;
    protected Button btnAddCart;
    protected Button btnBuy;
    protected TextView titleTxt;

    protected String name;
    protected float price;
    protected int id;
    protected int shopId;

    protected String src;
    protected String href;
    protected String desc;

    protected GoodInfo goodInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void  initBundleData(){
        name=getIntent().getExtras().getString("name");
        price=getIntent().getExtras().getFloat("price");
        id=getIntent().getExtras().getInt("id");
        shopId=getIntent().getExtras().getInt("shopId");
        src=getIntent().getExtras().getString("src");
        href=getIntent().getExtras().getString("href");
        desc=getIntent().getExtras().getString("desc");


        goodInfo=new GoodInfo(id,name,desc,0,0,price,src,shopId,0,href,false);


    }
    protected void initViewEvent(){
        btnAddCart= (Button) findViewById(R.id.btnAddCart);
        btnBuy=(Button)findViewById(R.id.btnBuy);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        /*
            进入购物车界面
         */
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCart();
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
                toCart();
            }
        });
    }
    protected void addToCart(){

    }
    protected void toCart(){

    }
    protected boolean initCustomActionBar() {
        initBundleData();

        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.goodheader);

        cartButton=(Button) mActionbar.getCustomView().findViewById(R.id.cartButton);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);

        titleTxt=(TextView) mActionbar.getCustomView().findViewById(R.id.titleTxt);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initViewEvent();


        return true;
    }
}
