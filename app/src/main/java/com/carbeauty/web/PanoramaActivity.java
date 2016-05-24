package com.carbeauty.web;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.kaopiz.kprogresshud.KProgressHUD;

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
    public static  final  String KEY_SHOPID="key_shopId";
    ConvenientBanner banner;
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
        banner= (ConvenientBanner)findViewById(R.id.convenientBanner);
        panorama=getIntent().getStringExtra(KEY_PANORAMA);
        int shopId=getIntent().getIntExtra(KEY_SHOPID,0);

        for (String fileName : panorama.split(",")){
            String webPath=WSConnector.getPanoramaURL(shopId + "", fileName);
            Log.v("URL", "URL:::" + webPath);
            localImagePaths.add(webPath);
        }

        rightBtn.setVisibility(View.GONE);

        new GetBannerTask2().execute();
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (mActionbar.isShowing()) {
                        mActionbar.hide();
                    } else {
                        mActionbar.show();
                    }
                }
            }
        });



        mActionbar.hide();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation=newConfig.orientation;
        Log.e("newConfig", newConfig.toString());
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            banner.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params=banner.getLayoutParams();

                    params.height= ViewGroup.LayoutParams.MATCH_PARENT;
                    params.width= ViewGroup.LayoutParams.MATCH_PARENT;

                    banner.setLayoutParams(params);
                }
            });
            mActionbar.hide();
        }else{
            banner.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params=banner.getLayoutParams();

                    params.height= DensityUtil.dip2px(PanoramaActivity.this,200);
                    params.width= ViewGroup.LayoutParams.MATCH_PARENT;

                    banner.setLayoutParams(params);
                }
            });
            mActionbar.show();
        }

    }

    public class LocalImageHolderView implements Holder<Bitmap> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, final int position, Bitmap bm) {
            imageView.setImageBitmap(bm);
        }
    }

    class GetBannerTask2 extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressHUD=KProgressHUD.create(PanoramaActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("加载中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
               for (String webPath:localImagePaths){

                    Bitmap bm= ImageUtils.convertNetToBitmap(webPath);
                    if(bm!=null){
                        localImageBitmaps.add(bm);
                    }

                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressHUD.dismiss();
            if(localImageBitmaps==null||localImageBitmaps.size()<=0){
                return;
            }
            banner.setPages(
                    new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, localImageBitmaps)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        }
    }


}
