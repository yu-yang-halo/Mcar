package com.carbeauty.web;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import cn.service.WSConnector;
import cn.service.WSException;

/**
 * Created by Administrator on 2016/3/10.
 */
public class WebBroswerPromotionActivity extends Activity {
    WebView webView;
    ActionBar mActionbar;
    TextView tvTitle;
    KProgressHUD  progressHUD;
    Button promotionBtn;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_web_promotion);
        initCustomActionBar();

        webView= (WebView) findViewById(R.id.webView);

        promotionBtn= (Button) findViewById(R.id.imageView16);

        id=getIntent().getIntExtra("promotionId",-1);


        String url=getIntent().getStringExtra("URL");
        //Toast.makeText(this, getHttpURL(url), Toast.LENGTH_SHORT).show();


        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebViewClient viewClient=new WebViewClient();
        viewClient.shouldOverrideUrlLoading(webView, url);
        webView.setWebViewClient(viewClient);
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
                }
            }



        });



        webView.setWebViewClient(viewClient);
        webView.loadUrl(getHttpURL(url));



        promotionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCouponTask(id).execute();
            }
        });

    }

    class GetCouponTask extends AsyncTask<String,String,String> {
        int promotionId;
        KProgressHUD progressHUD;
        GetCouponTask(int promotionId){
            this.promotionId=promotionId;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(WebBroswerPromotionActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("领取中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                WSConnector.getInstance().updCoupon(promotionId);
            } catch (WSException e) {
                return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressHUD.dismiss();
            if(s==null){
                Toast.makeText(WebBroswerPromotionActivity.this, "购物券领取成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WebBroswerPromotionActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String getHttpURL(String url){
        String prefs="http://";
        if(url==null||url.trim().equals("")){
            return "http://www.baidu.com";
        }
        if(url.length()<7||!url.substring(0,7).equals(prefs)){
            return prefs+url;
        }
        return url;
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

        Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        return true;
    }
}
