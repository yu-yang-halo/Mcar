package com.carbeauty.good;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsType;

/**
 * Created by Administrator on 2016/3/22.
 */
public class GoodActivity extends HeaderActivity {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    int shopId;
    List<GoodsType> goodsTypes;

    private GoodsInfoListenser listenser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goods);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);
        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);
        new GetGoodsTypeTask().execute();

    }



    private void initSlidTab(){

        for (GoodsType goodsType : goodsTypes) {
            mFragments.add(SimpleCardFragment.getInstance(goodsType));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_10 = ViewFindUtils.find(decorView, R.id.tl_10);
        tabLayout_10.setViewPager(vp);
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
                initSlidTab();
                listenser.onCallback(goodInfos);
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

    public void setListenser(GoodsInfoListenser listenser) {
        this.listenser = listenser;
    }

    public interface GoodsInfoListenser{
        public void onCallback(List<GoodInfo> goodInfos);
    }
}
