package com.carbeauty.good;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.request.animation.ViewAnimation;
import com.carbeauty.CommonUtils;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.github.florent37.viewanimator.ViewAnimator;

import cn.service.WSConnector;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodLookActivity extends FragmentActivity {
    ActionBar mActionbar;

    TextView gDesc;
    EditText gCount;
    TextView gPrice;
    ImageView gImageView;
    Button btnAdd;
    Button btnRemove;
    Button btnAddCart;

    Button cartButton;

    int id;
    int shopId;
    String src;

    Bitmap bm;
    GoodInfo goodInfo;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodslook);
        name=getIntent().getExtras().getString("name");
        float price=getIntent().getExtras().getFloat("price");
        id=getIntent().getExtras().getInt("id");
        shopId=getIntent().getExtras().getInt("shopId");
        src=getIntent().getExtras().getString("src");
        String desc=getIntent().getExtras().getString("desc");

        initCustomActionBar();

        gDesc= (TextView) findViewById(R.id.gDesc);
        gImageView= (ImageView) findViewById(R.id.gImageView);
        gCount= (EditText) findViewById(R.id.gCount);
        gPrice= (TextView) findViewById(R.id.gPrice);

        btnAdd= (Button) findViewById(R.id.btnAdd);
        btnRemove= (Button) findViewById(R.id.btnRemove);
        btnAddCart= (Button) findViewById(R.id.btnAddCart);




        goodInfo=new GoodInfo(id,name,desc,0,0,price,src,shopId,0);



        gDesc.setText(desc);
        gCount.setText("1");
        gPrice.setText(price + "元");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number;
                try {
                    number = Integer.parseInt(gCount.getText().toString());
                } catch (NumberFormatException e) {
                    number = 0;
                }


                number++;

                if (number >= 100) {
                    number = 100;
                }

                gCount.setText(number + "");

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number;
                try {
                    number = Integer.parseInt(gCount.getText().toString());
                }catch (NumberFormatException e){
                    number=2;
                }
                number--;

                if(number<=0){
                    number=1;
                }
                gCount.setText(number+"");
            }
        });

        gCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!CommonUtils.isNumeric(s.toString())) {
                    Toast.makeText(GoodLookActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String loginName = WSConnector.getInstance().getUserMap().get("loginName");

                String count = gCount.getText().toString();

                if (CommonUtils.isNumeric(count)) {
                    boolean isAddSuccess = CartManager.getInstance().addToCart(id, Integer.parseInt(count), bm,goodInfo);

                    if (isAddSuccess) {
                        ViewAnimator.animate(cartButton).bounce().duration(1500).start();
                    }
                } else {
                    Toast.makeText(GoodLookActivity.this, "数量格式错误", Toast.LENGTH_SHORT).show();
                }


            }
        });

        /*
            进入购物车界面
         */
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CartManager.getInstance().getMyCartClassList().size()<=0){
                    Toast.makeText(GoodLookActivity.this,"您的购物车还没有任何商品哦",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(GoodLookActivity.this,GoodOrderActivity.class);
                    intent.putExtra("Title","我的购物车");
                    startActivity(intent);

                }
            }
        });


        new NetBitmapToImageViewTask(gImageView).execute();
    }
    protected boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.goodheader);

        cartButton=(Button) mActionbar.getCustomView().findViewById(R.id.cartButton);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);

        TextView titleTxt=(TextView) mActionbar.getCustomView().findViewById(R.id.titleTxt);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleTxt.setText(name);



        return true;
    }

    class NetBitmapToImageViewTask extends AsyncTask<String,String,String> {
        ImageView goodImageView;
        public NetBitmapToImageViewTask(ImageView goodImageView){
            this.goodImageView=goodImageView;
        }
        @Override
        protected String doInBackground(String... params) {
            String url= WSConnector.getGoodsURL(shopId + "", src);
            bm= ImageUtils.convertNetToBitmap(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(bm!=null){
                goodImageView.setImageBitmap(bm);
            }
        }
    }
}
