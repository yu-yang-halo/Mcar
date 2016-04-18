package com.carbeauty;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.carbeauty.adapter.CityAdapter;
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
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CityInfo;

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
	Button rightBtn;
	BDLocation bdLocation;
	List<CityInfo> cityInfoList;
	CityInfo currentCity;
	private IShowModeListenser iShowModeListenser;

	public void setiShowModeListenser(IShowModeListenser iShowModeListenser) {
		this.iShowModeListenser = iShowModeListenser;
	}

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

		new GetCityTasks().execute();

	}
	public void setSelectPos(int position){
		mTabLayout_1.setCurrentTab(position);
		tvTitle.setText(mTitles2[position]);
		if(position==0){
			logoImageView.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.GONE);
			leftBtn.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.GONE);
		}else if(position==1){

			logoImageView.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.VISIBLE);
		}else{
			logoImageView.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
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

		rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
		leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
		rightBtn.setVisibility(View.GONE);

		leftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//setSelectPos(1);
				Intent intent = new Intent(MainActivity.this, CityActivity.class);

				startActivityForResult(intent, 1314);

			}
		});


		rightBtn.setText("地图模式");

		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.isSelected()){
					v.setSelected(false);
					rightBtn.setText("地图模式");
					ViewAnimator.animate(rightBtn).flipVertical()
							.duration(1000).start();

					iShowModeListenser.onShowMode(0);
				}else{
					v.setSelected(true);
					rightBtn.setText("列表模式");
					ViewAnimator.animate(rightBtn).flipVertical()
							.duration(1000).start();

					iShowModeListenser.onShowMode(1);

				}



			}
		});

		leftBtn.setText(bdLocation.getCity());

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1314&&resultCode==1){
            String cityName=data.getStringExtra("cityName");
			if(cityName.contains("市")){
				leftBtn.setText(cityName);
			}else{
				leftBtn.setText(cityName+"市");
			}

			setSelectPos(1);
		}
	}

	@Override
	public BDLocation getLocation() {

		return bdLocation;
	}

	class GetCityTasks extends AsyncTask<String,String,String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				cityInfoList= WSConnector.getInstance().getCityList();
			} catch (WSException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			if(s==null&&cityInfoList!=null){
				for(CityInfo cityInfo:cityInfoList){
					if(cityInfo.getName().equals(bdLocation.getCity())
							||bdLocation.getCity().equals(cityInfo.getName() + "市")){
						currentCity=cityInfo;
						currentCity.setName(bdLocation.getCity());
						break;
					}

				}
				if(currentCity!=null){
					leftBtn.setText(currentCity.getName());
					ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,currentCity.getCityId());
				}

			}
		}
	}

	public interface IShowModeListenser{
		public void onShowMode(int mode);
	}
}
