package com.carbeauty.good;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.manager.CarManagerActivity;
import com.carbeauty.order.HeaderActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsType;

/**
 * Created by Administrator on 2016/3/22.
 */
public class GoodActivity extends FragmentActivity {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    int shopId;
    List<GoodsType> goodsTypes;
    ActionBar mActionbar;
    Button cartButton;
    int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goods);
        initCustomActionBar();

        type=getIntent().getIntExtra("Type",-1);


        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);

         /*
            进入购物车界面
         */
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartManager.MyCartClass> myCartClasses=CartManager.getInstance().getMyCartClassList(GoodActivity.this);

                if(myCartClasses==null
                        ||myCartClasses.size()<=0){
                    Toast.makeText(GoodActivity.this,"您的购物车还没有任何商品哦",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(GoodActivity.this,GoodOrderActivity.class);
                    intent.putExtra("Title","我的购物车");
                    startActivity(intent);
                }
            }
        });
        new GetGoodsTypeTask().executeOnExecutor(Executors.newCachedThreadPool());

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //CartManager.getInstance().clearCartList();
    }

    private void initSlidTab( List<GoodInfo> goodInfos){

        int currentLab=0;

        for (int i=0;i<goodsTypes.size();i++) {
            if(goodsTypes.get(i).getId()==type){
                currentLab=i;
            }
            mFragments.add(SimpleCardFragment.getInstance(goodsTypes.get(i),goodInfos));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_10 = ViewFindUtils.find(decorView, R.id.tl_10);
        tabLayout_10.setViewPager(vp);
        tabLayout_10.setCurrentTab(currentLab);

    }
    class GetGoodsTypeTask extends AsyncTask<String,String,String>{
        List<GoodInfo> goodInfos;

        @Override
        protected String doInBackground(String... params) {
            try {
                goodsTypes= WSConnector.getInstance().getGoodsType();
                goodInfos= WSConnector.getInstance().getGoodsList(shopId);

            } catch (WSException e) {
               return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                initSlidTab(goodInfos);

            }else {
                Toast.makeText(GoodActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return goodsTypes.get(position).getName();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
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
                onBackPressed();
            }
        });
        String title=getIntent().getExtras().getString("Title");
        titleTxt.setText(title);



        return true;
    }


}
