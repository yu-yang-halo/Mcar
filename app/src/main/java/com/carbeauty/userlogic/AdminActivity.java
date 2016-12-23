package com.carbeauty.userlogic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.carbeauty.BaseActivity;
import com.carbeauty.R;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.fragment.SimpleCardFragment;
import com.carbeauty.good.GoodActivity;
import com.carbeauty.order.HeaderActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class AdminActivity extends BaseActivity implements ICallData{
    private ArrayList<ItemFragment> mFragments = new ArrayList<>();
    String[] titles=new String[]{"养护","换油","漆","自助洗"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_admin);
        initCustomActionBar();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        titleLabel.setText("通知中心");

        initSlidTab();

    }
    public void refreshData(){

        for (ItemFragment fragment:mFragments){
            fragment.loadCacheData();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initSlidTab(){

        int currentLab=0;

        for (int i=0;i<titles.length;i++) {

            mFragments.add(ItemFragment.getInstance(i+1));
        }

        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp1);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_11 = ViewFindUtils.find(decorView, R.id.tl_11);
        tabLayout_11.setViewPager(vp);
        tabLayout_11.setCurrentTab(currentLab);

    }

    @Override
    public void onDataRefresh() {
        refreshData();
        Log.e("onDataRefresh","onDataRefresh...");
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

}
