package com.carbeauty.good;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.fragment.GoodListShowFragment;
import com.carbeauty.order.HeaderActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsOrderListType;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GoodOrderListShowActivity extends HeaderActivity {
    /** 待发货--0  已发货--1  未支付--2  已支付--3(瞬间状态)  取消--4   确认收货--5    **/
    private static final String[] titles=new String[]{"待发货","已发货","未支付","已收货"};
    private static final int[]    indexs=new int[]{0,1,2,5};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private WeakReference<GoodListShowFragment> weakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodslistshow);

        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        new GetGoodOrderListTask(false).executeOnExecutor(Executors.newCachedThreadPool());

    }

    public void setWeakReference(WeakReference<GoodListShowFragment> weakReference) {
        this.weakReference = weakReference;
    }
    public void reloadOrderList(){
        new GetGoodOrderListTask(true).executeOnExecutor(Executors.newCachedThreadPool());
    }

    private void initSlidTab(List<GoodsOrderListType> goodsOrderListTypes, List<GoodInfo> goodInfos){

        int currentLab=0;

        for (int i=0;i<titles.length;i++) {
            mFragments.add(GoodListShowFragment.getInstance(indexs[i],goodsOrderListTypes,goodInfos));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_10 = ViewFindUtils.find(decorView, R.id.tl_10);
        tabLayout_10.setViewPager(vp);
        tabLayout_10.setCurrentTab(currentLab);





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
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    class GetGoodOrderListTask extends AsyncTask<String,String,String>{
        List<GoodsOrderListType> goodsOrderListTypes;
        List<GoodInfo> goodInfos;

        boolean isRefreshYN;
        GetGoodOrderListTask(boolean isRefreshYN){
            this.isRefreshYN=isRefreshYN;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                goodInfos=WSConnector.getInstance().getGoodsList(-2);
                goodsOrderListTypes=WSConnector.getInstance().getGoodsOrderList(Constants.SEARCH_USER,-1,"1900-08-01+11+11+11");
            } catch (WSException e) {
                return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(s==null){
                if(!isRefreshYN){
                    initSlidTab(goodsOrderListTypes,goodInfos);
                }else{
                    weakReference.get().refreshNewData(goodsOrderListTypes,goodInfos);
                }

            }else{
                Toast.makeText(GoodOrderListShowActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
