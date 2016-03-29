package com.carbeauty.web;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.carbeauty.R;
import com.carbeauty.order.HeaderActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PanoramaActivity extends HeaderActivity {
    WebView webView;
    KProgressHUD progressHUD;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_panorama);
        initCustomActionBar();
        url=getIntent().getStringExtra("URL");
        Log.v("URL","URL:::"+url);

        rightBtn.setVisibility(View.GONE);

        webView= (WebView) findViewById(R.id.webView);

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        progressHUD= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("加载中...")
                .setAnimationSpeed(3)
                .setDimAmount(0.3f)
                .show();

        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {
                    progressHUD.dismiss();

                    //String url="http://himg2.huanqiu.com/attachment2010/2016/0325/20160325075958692.jpg";
                    webView.loadUrl("javascript:initImageURL('"+url+"')");
                }
            }


        });



        webView.loadUrl("file:///android_asset/panorama/demo.html");
    }
}
