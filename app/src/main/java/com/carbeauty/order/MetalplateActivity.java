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
import com.carbeauty.fragment.HomeFragment;
import com.carbeauty.fragment.IndividualFragment;
import com.carbeauty.fragment.MetaFreeFragment;
import com.carbeauty.fragment.MetaPayFragment;
import com.carbeauty.fragment.ShopFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/13.
 */
public class MetalplateActivity extends FragmentActivity {
    private final String[] mTitles = {"自费钣金","保险钣金"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    View mDecorView;
    private CommonTabLayout mTabLayout_1;
    private int[] mIconUnselectIds = {
            R.mipmap.home_btn_home, R.mipmap.home_btn_shop};
    private int[] mIconSelectIds = {
            R.mipmap.home_btn_home_sel, R.mipmap.home_btn_shop_sel};
    ActionBar mActionbar;
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_metalplate);
        initCustomActionBar();
        mFragments.add(new MetaPayFragment());
        mFragments.add(new MetaFreeFragment());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],mIconSelectIds[i],mIconUnselectIds[i]));
        }
        mDecorView = getWindow().getDecorView();

        /** with nothing */
        mTabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tl_2);
        mTabLayout_1.setTabData(mTabEntities, this, R.id.currentPageFragment, mFragments);
        //显示未读红点
        mTabLayout_1.showDot(2);
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
