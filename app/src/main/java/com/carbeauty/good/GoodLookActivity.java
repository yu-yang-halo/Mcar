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

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.request.animation.ViewAnimation;
import com.carbeauty.CommonUtils;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.web.WebBroswerActivity;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodLookActivity extends FragmentActivity {
    ActionBar mActionbar;

    TextView gDesc;
    EditText gCount;
    TextView gPrice;
    ConvenientBanner banner;
    Button btnAdd;
    Button btnRemove;
    Button btnAddCart;

    Button cartButton;

    int id;
    int shopId;
    String src;
    String href;

    Bitmap bm;
    GoodInfo goodInfo;
    String name;
    WebView webview;
    Button arrowBtn;


    ImageLoader imageLoader;

    List<String> localImages;

    RelativeLayout relayout2;
    Bitmap[] cacheBitmaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodslook);
        name=getIntent().getExtras().getString("name");
        float price=getIntent().getExtras().getFloat("price");
        id=getIntent().getExtras().getInt("id");
        shopId=getIntent().getExtras().getInt("shopId");
        src=getIntent().getExtras().getString("src");
        href=getIntent().getExtras().getString("href");
        String desc=getIntent().getExtras().getString("desc");

        initCustomActionBar();

        gDesc= (TextView) findViewById(R.id.gDesc);
        banner= (ConvenientBanner) findViewById(R.id.convenientBanner);
        gCount= (EditText) findViewById(R.id.gCount);
        gPrice= (TextView) findViewById(R.id.gPrice);

        btnAdd= (Button) findViewById(R.id.btnAdd);
        btnRemove= (Button) findViewById(R.id.btnRemove);
        btnAddCart= (Button) findViewById(R.id.btnAddCart);
        relayout2= (RelativeLayout) findViewById(R.id.relayout2);
        arrowBtn=(Button)findViewById(R.id.arrowBtn);


        imageLoader = ImageLoader.getInstance();





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

        goodInfo=new GoodInfo(id,name,desc,0,0,price,src,shopId,0,href);



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
                } catch (NumberFormatException e) {
                    number = 2;
                }
                number--;

                if (number <= 0) {
                    number = 1;
                }
                gCount.setText(number + "");
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
                    boolean isAddSuccess = CartManager.getInstance().addToCart(id, Integer.parseInt(count), bm, goodInfo);

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
                if (CartManager.getInstance().getMyCartClassList().size() <= 0) {
                    Toast.makeText(GoodLookActivity.this, "您的购物车还没有任何商品哦", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(GoodLookActivity.this, GoodOrderActivity.class);
                    intent.putExtra("Title", "我的购物车");
                    startActivity(intent);

                }
            }
        });





        relayout2.setTag(0);


        relayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == 0) {
                    webview.setVisibility(View.VISIBLE);
                    v.setTag(1);
                    ViewAnimator.animate(arrowBtn).rotation(90).duration(300).start();
                } else {
                    v.setTag(0);
                    webview.setVisibility(View.GONE);
                    ViewAnimator.animate(arrowBtn).rotation(0).duration(300).start();
                }
            }
        });

        String[] srcList=src.split(",");
        cacheBitmaps=new Bitmap[srcList.length];

        localImages=new ArrayList<String>();
        for (String srcPath:srcList){
            String imageUri= WSConnector.getGoodsURL(shopId + "", srcPath);
            System.out.println("imageUri "+imageUri);
            localImages.add(imageUri);
        }

        initGoodBanner(localImages);


    }



    private void initGoodBanner(List<String> localImages){
        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

    }

    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, final int position, String imageURL) {

            if(cacheBitmaps[position]==null){
                imageView.setImageResource(R.drawable.icon_default);
                new ImageTask(imageURL,imageView,position).executeOnExecutor(Executors.newCachedThreadPool());
            }else{
                imageView.setImageBitmap(cacheBitmaps[position]);
            }

        }
    }

    class ImageTask extends  AsyncTask<String,String,Bitmap>{
        private String imageURL;
        private ImageView imageView;
        int position;
        ImageTask(String imageURL,ImageView imageView,int position){
            this.imageURL=imageURL;
            this.imageView=imageView;
            this.position=position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return  ImageUtils.convertNetToBitmap(imageURL,200,200);
        }
        @Override
        protected void onPostExecute(Bitmap s) {
              if(s!=null){
                  imageView.setImageBitmap(s);
                  cacheBitmaps[position]=s;
              }
            if(position==0){
                bm=s;
            }
        }
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

}
