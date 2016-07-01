package com.carbeauty.good;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.carbeauty.CommonUtils;
import com.carbeauty.R;
import com.carbeauty.cache.CartManager;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/5/30.
 */
public class GoodWebShowActivity extends CommonActivity {
    WebView webview;
    int number;
    String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodwebshow);
        initCustomActionBar();
        titleTxt.setText(name);
        number=getIntent().getIntExtra("count",1);
        imageURL=getIntent().getStringExtra("imageURL");

        webview= (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;

            }
        });
        System.out.println("href " + href);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl(href);

    }

    protected void addToCart(){
        String loginName = WSConnector.getInstance().getUserMap().get("loginName");

        boolean isAddSuccess = CartManager.getInstance().addToCart(id, number, imageURL, goodInfo);
        CartManager.getInstance().cacheMyCartClassToDisk(this);
        if (isAddSuccess) {
            ViewAnimator.animate(cartButton).bounce().duration(1500).start();
        }

    }
    protected void toCart(){
        List<CartManager.MyCartClass> myCartClasses=CartManager.getInstance().getMyCartClassList(GoodWebShowActivity.this);

        if (myCartClasses==null||myCartClasses.size() <= 0) {
            Toast.makeText(GoodWebShowActivity.this, "您的购物车还没有任何商品哦", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(GoodWebShowActivity.this, GoodOrderActivity.class);
            intent.putExtra("Title", "我的购物车");
            startActivity(intent);

        }
    }

}
