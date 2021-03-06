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

import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.request.animation.ViewAnimation;
import com.carbeauty.CommonUtils;
import com.carbeauty.ImageUtils;
import com.carbeauty.MyApplication;
import com.carbeauty.R;
import com.carbeauty.adapter.BitmapCache;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.web.WebBroswerActivity;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;
import cn.service.bean.GoodInfo;
import view.extend.UIColorSelector;
import view.extend.UILayoutSelector;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodLookActivity extends CommonActivity {

    WebView gDesc;
    EditText gCount;
    TextView gPrice;
    ConvenientBanner banner;
    Button btnAdd;
    Button btnRemove;


    Bitmap bm;



    Button arrowBtn;



    List<String> localImages;

    RelativeLayout relayout2;
    ScrollView scrollView;
    RequestQueue mQueue;
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodslook);
        mQueue=Volley.newRequestQueue(this);
        MyApplication myApplication= (MyApplication) getApplicationContext();
        imageLoader=new ImageLoader(mQueue, myApplication.getBitmapCache());



        initCustomActionBar();
        titleTxt.setText(name);

        gDesc= (WebView) findViewById(R.id.gDesc);
        banner= (ConvenientBanner) findViewById(R.id.convenientBanner);
        gCount= (EditText) findViewById(R.id.gCount);
        gPrice= (TextView) findViewById(R.id.gPrice);

        btnAdd= (Button) findViewById(R.id.btnAdd);
        btnRemove= (Button) findViewById(R.id.btnRemove);

        relayout2= (RelativeLayout) findViewById(R.id.relayout2);
        arrowBtn=(Button)findViewById(R.id.arrowBtn);
        scrollView= (ScrollView) findViewById(R.id.scrollView);







        gDesc.getSettings().setDefaultTextEncodingName("UTF-8") ;
        gDesc.loadData(desc,"text/html; charset=UTF-8", null);


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
                if(!check()){
                    return;
                }

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
                if(!check()){
                    return;
                }
                addToCart();
                toCart();
            }
        });



        relayout2.setTag(0);




        String[] srcList=src.split(",");

        localImages=new ArrayList<String>();
        for (String srcPath:srcList){
            String imageUri= WSConnector.getGoodsURL(shopId + "", srcPath);
            System.out.println("imageUri "+imageUri);
            localImages.add(imageUri);
        }

        initGoodBanner(localImages);

        relayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number;
                try {
                    number = Integer.parseInt(gCount.getText().toString());
                } catch (NumberFormatException e) {
                    number = 0;
                }
                Intent intent = new Intent(GoodLookActivity.this, GoodWebShowActivity.class);
                intent.putExtras(getIntent().getExtras());
                intent.putExtra("count", number);
                intent.putExtra("imageURL", localImages.get(0));
                startActivity(intent);
            }
        });


        RelativeLayout sizeLayoutContainer= (RelativeLayout) findViewById(R.id.sizeLayoutContainer);
        RelativeLayout colorLayoutContainer= (RelativeLayout) findViewById(R.id.colorLayoutContainer);

        UILayoutSelector sizeLayout= (UILayoutSelector) findViewById(R.id.sizeLayout);
        UIColorSelector colorLayout= (UIColorSelector) findViewById(R.id.colorLayout);

        if(tags==null){
            sizeLayoutContainer.setVisibility(View.GONE);
        }else{
            sizeLayoutContainer.setVisibility(View.VISIBLE);
            final String[] arrs=tags.split("\\|");
            int len=0;
            if(arrs!=null&&arrs.length>0){
                len=arrs.length;
            }
            int rows=len%2==0?(len/2):(len/2+1);
            sizeLayout.initSelector(arrs);
            ViewGroup.LayoutParams params=sizeLayoutContainer.getLayoutParams();
            params.height=sizeLayout.CELL_HEIGHT*rows;
            sizeLayoutContainer.setLayoutParams(params);

            sizeLayout.setOnSelectedListerser(new UILayoutSelector.OnSelectedListerser() {
                @Override
                public void onSelected(int pos) {
                    goodInfo.setTags(arrs[pos]);
                }
            });
        }
        if(colors==null){
            colorLayoutContainer.setVisibility(View.GONE);
        }else{
            colorLayoutContainer.setVisibility(View.VISIBLE);

            final String[] arrs2=colors.split("\\|");
            colorLayout.initSelector(arrs2);
            colorLayout.setOnSelectedListerser(new UIColorSelector.OnSelectedListerser() {
                @Override
                public void onSelected(int pos) {
                    goodInfo.setColors(arrs2[pos]);
                }
            });
        }








    }

    private boolean check(){
        if(tags!=null){
            if(goodInfo.getTags()==null){
                showMessage("请选择尺寸");
                return false;
            }
        }

        if(colors!=null){
            if(goodInfo.getColors()==null){
                showMessage("请选择颜色");
                return false;
            }
        }

        return true;
    }
    protected void addToCart(){
        String loginName = WSConnector.getInstance().getUserMap().get("loginName");

        String count = gCount.getText().toString();

        if (CommonUtils.isNumeric(count)) {
            boolean isAddSuccess = CartManager.getInstance().addToCart(id, Integer.parseInt(count),localImages.get(0), goodInfo);
            CartManager.getInstance().cacheMyCartClassToDisk(this);
            if (isAddSuccess) {
                ViewAnimator.animate(cartButton).bounce().duration(1500).start();
            }
        } else {
            Toast.makeText(GoodLookActivity.this, "数量格式错误", Toast.LENGTH_SHORT).show();
        }


    }
    protected void toCart(){
        List<CartManager.MyCartClass> myCartClasses=CartManager.getInstance().getMyCartClassList(GoodLookActivity.this);

        if (myCartClasses==null||myCartClasses.size() <= 0) {
            Toast.makeText(GoodLookActivity.this, "您的购物车还没有任何商品哦", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(GoodLookActivity.this, GoodOrderActivity.class);
            intent.putExtra("Title", "我的购物车");
            startActivity(intent);

        }
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
            imageView.setAdjustViewBounds(true);
//            imageView.setMaxWidth(CommonUtils.getScreenWidth(context));
//            imageView.setMaxHeight(CommonUtils.getScreenWidth(context) * 5);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, final int position, String imageURL) {

            ImageLoader.ImageListener listener=ImageLoader.getImageListener(imageView
                    , 0,0);

            imageLoader.get(imageURL, listener,300,300);

        }
    }





}
