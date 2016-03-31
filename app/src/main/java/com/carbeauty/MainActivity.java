package com.carbeauty;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.fragment.HomeFragment;
import com.carbeauty.fragment.IndividualFragment;
import com.carbeauty.fragment.LocationUpdateListenser;
import com.carbeauty.fragment.PromotionFragment;
import com.carbeauty.fragment.ShopFragment;
import com.carbeauty.userlogic.RegisterActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements LocationUpdateListenser {
	private Context mContext = this;
	private ArrayList<Fragment> mFragments = new ArrayList<>();
	private String[] mTitles = {"首页", "门店", "活动", "我的"};
	private String[] mTitles2 = {"首页", "附近门店", "活动", "我的"};
	private int[] mIconUnselectIds = {
			R.mipmap.home_btn_home, R.mipmap.home_btn_shop,
			R.mipmap.home_btn_active, R.mipmap.home_btn_my};
	private int[] mIconSelectIds = {
			R.mipmap.home_btn_home_sel, R.mipmap.home_btn_shop_sel,
			R.mipmap.home_btn_active_sel, R.mipmap.home_btn_my_sel};
	private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
	private View mDecorView;
	private ViewPager mViewPager;
	private CommonTabLayout mTabLayout_1;
    private ActionBar mActionbar;
	private TextView tvTitle;
	private ImageView logoImageView;
	Button leftBtn;
	BDLocation bdLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		MyApplication application= (MyApplication) getApplication();
		bdLocation=application.getMyLocation();


		initCustomActionBar();

		mFragments.add(new HomeFragment());
		mFragments.add(new ShopFragment());
		mFragments.add(new PromotionFragment());
		mFragments.add(new IndividualFragment());

		for (int i = 0; i < mTitles.length; i++) {
			mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
		}
		mDecorView = getWindow().getDecorView();

		/** with nothing */
		mTabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tl_1);
		mTabLayout_1.setTabData(mTabEntities, this, R.id.currentPageFragment, mFragments);

		mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
				setSelectPos(position);


			}

			@Override
			public void onTabReselect(int position) {
				//Toast.makeText(getApplicationContext(),"onTabReselect position:"+position,Toast.LENGTH_SHORT).show();

			}
		});

	}
	public void setSelectPos(int position){
		mTabLayout_1.setCurrentTab(position);
		tvTitle.setText(mTitles2[position]);
		if(position==0){
			logoImageView.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.GONE);
			leftBtn.setVisibility(View.VISIBLE);
		}else{
			logoImageView.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);
			leftBtn.setVisibility(View.GONE);
		}
	}
	private boolean initCustomActionBar() {
		mActionbar = getActionBar();
		if (mActionbar == null) {
			return false;
		}
		mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionbar.setDisplayShowCustomEnabled(true);
		mActionbar.setCustomView(R.layout.header_home0);
		tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
		logoImageView = (ImageView) mActionbar.getCustomView().findViewById(R.id.logoImageView);

		Button registerBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
		leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
		registerBtn.setVisibility(View.GONE);

		leftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelectPos(1);

			}
		});

		leftBtn.setText(bdLocation.getCity());

		return true;
	}



	@Override
	public BDLocation getLocation() {

		return bdLocation;
	}
}
