package com.carbeauty.web;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.carbeauty.DensityUtil;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.order.HeaderActivity;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PanoramaActivity extends HeaderActivity {
    public static  final  String KEY_PANORAMA="key_panorama";
    public static  final  String KEY_TITLE="Title";
    public static  final  String KEY_ADDRESS="address";
    public static  final  String KEY_ICON="icon_name";
    public static  final  String KEY_SHOPID="key_shopId";
    WebView webView;
    KProgressHUD progressHUD;
    String panorama;
    List<String> localImagePaths=new ArrayList<String>();
    List<Bitmap> localImageBitmaps=new ArrayList<Bitmap>();
    int orientation=Configuration.ORIENTATION_LANDSCAPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_panorama);
        initCustomActionBar();
        webView= (WebView) findViewById(R.id.webView);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebViewClient viewClient=new WebViewClient();

        webView.setWebViewClient(viewClient);
        progressHUD= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("加载中...")
                .setAnimationSpeed(3)
                .setDimAmount(0.3f)
                .show();


        panorama=getIntent().getStringExtra(KEY_PANORAMA);
        int shopId=getIntent().getIntExtra(KEY_SHOPID,0);

        for (String fileName : panorama.split(",")){
            String webPath=WSConnector.getPanoramaURL(shopId + "", fileName);

            localImagePaths.add(webPath);
        }

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        Point screenPoint=new Point();

        wm.getDefaultDisplay().getSize(screenPoint);


        int statusBarHeight =getStatusHeight(this);


        final int webHeight=screenPoint.y-mActionbar.getCustomView().getLayoutParams().height-statusBarHeight;
        System.out.println("height "+DensityUtil.px2dip(PanoramaActivity.this,statusBarHeight));


        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {
                    progressHUD.dismiss();
                    if (localImagePaths != null && localImagePaths.size() > 0) {
                        Gson gson = new Gson();
                        String imageArrJSON = gson.toJson(localImagePaths);
                        Log.v("URL", "imageArrJSON URL:::" + imageArrJSON);
                        webView.loadUrl("javascript:loadImages(" + imageArrJSON + "," + DensityUtil.px2dip(PanoramaActivity.this, webHeight) + ")");
                    }


                }
            }


        });


        webView.setWebViewClient(viewClient);
        webView.loadUrl("file:///android_asset/album/index.html");



        rightBtn.setVisibility(View.GONE);



    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
