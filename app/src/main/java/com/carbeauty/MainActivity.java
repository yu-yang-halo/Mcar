package com.carbeauty;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.carbeauty.alertDialog.DialogManagerUtils;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CityInfo;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

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
	CityInfo myShopOwnerCity;
	UserInfo userInfo;
	List<ShopInfo> shopInfos;
	private IShowModeListenser iShowModeListenser;

	public void setiShowModeListenser(IShowModeListenser iShowModeListenser) {
		this.iShowModeListenser = iShowModeListenser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplication());
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

			}
		});

		loadData();

	}
	public void setSelectPos(int position){

		if(position==3){
			mActionbar.hide();
		}else{
			mActionbar.show();
		}

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
		}else if(position==2){
			logoImageView.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
		}else{
			logoImageView.setVisibility(View.GONE);
			tvTitle.setVisibility(View.GONE);
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
		}


	}
	public static void disableShowHideAnimation(ActionBar actionBar) {
		try
		{
			actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(actionBar, false);
		}
		catch (Exception exception)
		{
			try {
				Field mActionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");
				mActionBarField.setAccessible(true);
				Object icsActionBar = mActionBarField.get(actionBar);
				Field mShowHideAnimationEnabledField = icsActionBar.getClass().getDeclaredField("mShowHideAnimationEnabled");
				mShowHideAnimationEnabledField.setAccessible(true);
				mShowHideAnimationEnabledField.set(icsActionBar,false);
				Field mCurrentShowAnimField = icsActionBar.getClass().getDeclaredField("mCurrentShowAnim");
				mCurrentShowAnimField.setAccessible(true);
				mCurrentShowAnimField.set(icsActionBar,null);
			}catch (Exception e){
				//....
			}
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
				if (v.isSelected()) {
					v.setSelected(false);
					rightBtn.setText("地图模式");
					ViewAnimator.animate(rightBtn).flipVertical()
							.duration(1000).start();

					iShowModeListenser.onShowMode(0);
				} else {
					v.setSelected(true);
					rightBtn.setText("列表模式");
					ViewAnimator.animate(rightBtn).flipVertical()
							.duration(1000).start();

					iShowModeListenser.onShowMode(1);

				}


			}
		});

		if(bdLocation!=null&&bdLocation.getCity()!=null){
			leftBtn.setText(bdLocation.getCity());
		}


		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1314&&resultCode==1){
            String cityName=data.getStringExtra("cityName");
			leftBtn.setText(cityName);
			setSelectPos(1);
		}
	}

	@Override
	public BDLocation getLocation() {

		return bdLocation;
	}
	private void loadData(){
		MyApplication myApplication= (MyApplication) getApplicationContext();
		final Handler uiHandler=myApplication.getUiHandler();
		myApplication.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				boolean successYN=false;
				try {
					cityInfoList= WSConnector.getInstance().getCityList();
					shopInfos=WSConnector.getInstance().getShopList();
					userInfo=WSConnector.getInstance().getUserInfoById();
					successYN=true;
				} catch (WSException e) {
					e.printStackTrace();
				}
				final boolean finalSuccessYN = successYN;
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						if(userInfo!=null){
							ContentBox.loadInt(MainActivity.this,ContentBox.KEY_USER_TYPE,userInfo.getType());
						}
						int cityId=-1;
						int shopId=-1;
						if(finalSuccessYN &&cityInfoList!=null){

							for (ShopInfo info: shopInfos){
								if(info.getShopId()==userInfo.getShopId()){
									cityId=info.getCityId();
									shopId=info.getShopId();
									break;
								}
							}

							for(CityInfo cityInfo:cityInfoList){
								if(cityInfo.getCityId()==cityId){
									myShopOwnerCity=cityInfo;
									myShopOwnerCity.setName(cityInfo.getName());
									break;
								}
							}

							if(myShopOwnerCity!=null&&bdLocation!=null&&bdLocation.getCity()!=null&&bdLocation.getCity().contains(myShopOwnerCity.getName())){
								leftBtn.setText(myShopOwnerCity.getName());
								ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,myShopOwnerCity.getCityId());
								ContentBox.loadInt(MainActivity.this,ContentBox.KEY_SHOP_ID,shopId);
							}else{

					/*
					    两种情况：1.所选店铺的城市与当前城市不匹配
					             2.没有选择店铺,自动切换到目标城市
					 */

								final CityInfo currentCity=findLocalCityFromServer();
								if(currentCity!=null){
									if(myShopOwnerCity!=null){
										leftBtn.setText(myShopOwnerCity.getName());
										ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,myShopOwnerCity.getCityId());
									}
									DialogManagerUtils.showMessageAndCancel(MainActivity.this, "您目前的位置是" + bdLocation.getCity(), "是否切换到该城市", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											leftBtn.setText(currentCity.getName());
											ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,currentCity.getCityId());
											ContentBox.loadInt(MainActivity.this,ContentBox.KEY_SHOP_ID,-1);
										}
									});
								}else{
									if(myShopOwnerCity==null){
										Toast.makeText(MainActivity.this,"对不起,您所在的城市还没有门店",Toast.LENGTH_SHORT).show();
										leftBtn.setText("选择城市");
										ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,-1);
										ContentBox.loadInt(MainActivity.this,ContentBox.KEY_SHOP_ID,-1);
									}else{
										Toast.makeText(MainActivity.this,"对不起,您所在的城市还没有门店,自动切回之前店铺所在城市",Toast.LENGTH_SHORT).show();
										leftBtn.setText(myShopOwnerCity.getName());
										ContentBox.loadInt(MainActivity.this,ContentBox.KEY_CITY_ID,myShopOwnerCity.getCityId());
									}
								}
							}

						}
					}
				});

			}
		});
	}


	private CityInfo findLocalCityFromServer(){
		if(bdLocation==null||bdLocation.getCity()==null){
			return null;
		}
		CityInfo findLocalCityFromServer = null;
		for (CityInfo cityInfo:cityInfoList){
			if(bdLocation.getCity().contains(cityInfo.getName())){
				findLocalCityFromServer=cityInfo;
				break;
			}

		}

		return findLocalCityFromServer;


	}

	public interface IShowModeListenser{
		public void onShowMode(int mode);
	}

	long oldTime=System.currentTimeMillis();

	@Override
	public void onBackPressed() {

		long newTime=System.currentTimeMillis();
		if(newTime-oldTime>800){
			Toast.makeText(this,"再按一下返回键退出客乐养车坊",Toast.LENGTH_SHORT).show();
			oldTime=newTime;
		}else{
			finish();
		}

	}

}
