package com.carbeauty.order;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.TabEntity;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.fragment.MetaFreeFragment;
import com.carbeauty.fragment.MetaPayFragment;
import com.carbeauty.fragment.ShopMapFragment;
import com.carbeauty.fragment.ShopVideoFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/13.
 */
public class LookShopActivity extends HeaderActivity {
    private final String[] mTitles = {"门店全景","施工直播"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    View mDecorView;
    private CommonTabLayout mTabLayout_1;
    private int[] mIconUnselectIds = {
            R.mipmap.home_btn_home, R.mipmap.home_btn_shop};
    private int[] mIconSelectIds = {
            R.mipmap.home_btn_home_sel, R.mipmap.home_btn_shop_sel};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_lookshop);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);


        mFragments.add(new ShopMapFragment());
        mFragments.add(new ShopVideoFragment());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],mIconSelectIds[i],mIconUnselectIds[i]));
        }
        mDecorView = getWindow().getDecorView();

        /** with nothing */
        mTabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tl_2);
        mTabLayout_1.setTabData(mTabEntities, this, R.id.currentPageFragment, mFragments);
        //显示未读红点
        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout_1.setCurrentTab(position);


            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }
}
