package com.carbeauty.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.ViewFindUtils;
import com.carbeauty.adapter.MyOrderAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.fragment.MyOrderFragment;
import com.carbeauty.fragment.SimpleCardFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.DecoOrderInfo;
import cn.service.bean.GoodInfo;
import cn.service.bean.MetaOrderInfo;
import cn.service.bean.OilOrderInfo;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MyOrderActivity extends HeaderActivity {
    int ac_type_value;
    int shopId;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static final String[] titles=new String[]{"待付款","已付款","待确认","已完成","已退款"};
    private WeakReference<MyOrderFragment> weakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_myorder);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        ac_type_value=getIntent().getIntExtra(Constants.AC_TYPE, 0);
        shopId= ContentBox.getValueInt(this, ContentBox.KEY_SHOP_ID, 0);


        new GetOrderListTask(false).execute();

    }

    public void reloadOrderList(){
        new GetOrderListTask(true).execute();
    }

    public void setWeakReference(WeakReference<MyOrderFragment> weakReference) {
        this.weakReference = weakReference;
    }

    private void initSlidTab(List<CommonOrderBean> commonOrderBeans){

        int currentLab=0;

        for (int i=0;i<titles.length;i++) {
            mFragments.add(MyOrderFragment.getInstance(i,commonOrderBeans));
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


    class GetOrderListTask extends AsyncTask<String,String,String>{

        List<CommonOrderBean> commonOrderBeans=new ArrayList<>();
        boolean isRefreshData;
        GetOrderListTask(boolean isRefreshData){
            this.isRefreshData=isRefreshData;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<MetaOrderInfo>  metaOrderInfos=WSConnector.getInstance().getMetaOrderList(Constants.SEARCH_USER,0,0,0,
                        "1900-08-01+11+11+11",-1);
                List<OilOrderInfo>  oilOrderInfos=WSConnector.getInstance().getOilOrderList(Constants.SEARCH_USER, 0, 0, 0,
                        "1900-08-01+11+11+11", -1);
                List<DecoOrderInfo>  decoOrderInfos=WSConnector.getInstance().getDecoOrderList(Constants.SEARCH_USER, 0, 0, 0,
                        "1900-08-01+11+11+11", -1);

                if(metaOrderInfos!=null&&metaOrderInfos.size()>0){
                    for (MetaOrderInfo metaOrderInfo:metaOrderInfos){
                        String desc="";
                        if(metaOrderInfo.getMetaOrderNumbers()!=null&&metaOrderInfo.getMetaOrderNumbers().size()>0){
                            desc="数量:"+metaOrderInfo.getMetaOrderNumbers().size();
                        }else if(metaOrderInfo.getMetaOrderImgs()!=null){
                            desc="上传的图片数:"+metaOrderInfo.getMetaOrderImgs().size();
                        }

                        CommonOrderBean commonOrderBean=new CommonOrderBean(metaOrderInfo.getId(),
                                CommonOrderBean.ITEM_TYPE_META,metaOrderInfo.getPrice(),
                                metaOrderInfo.getState(),
                                metaOrderInfo.getPayState(),metaOrderInfo.getCreateTime(),
                                "钣金喷漆",
                                 desc);
                        commonOrderBean.setTradeNo(metaOrderInfo.getOut_trade_no());

                        commonOrderBeans.add(commonOrderBean);
                    }
                }

                if(oilOrderInfos!=null&&oilOrderInfos.size()>0){
                    for (OilOrderInfo oilOrderInfo:oilOrderInfos){

                        String desc="";
                        if(oilOrderInfo.getOilOrderNumber()!=null){
                            desc="数量:"+oilOrderInfo.getOilOrderNumber().size();
                        }
                        CommonOrderBean commonOrderBean=new CommonOrderBean(oilOrderInfo.getId(),
                                CommonOrderBean.ITEM_TYPE_OIL,oilOrderInfo.getPrice(),
                                oilOrderInfo.getState(),
                                oilOrderInfo.getPayState(),oilOrderInfo.getCreateTime(),
                                "换油保养",
                                desc);
                        commonOrderBean.setTradeNo(oilOrderInfo.getOut_trade_no());
                        commonOrderBeans.add(commonOrderBean);
                    }
                }

                if(decoOrderInfos!=null&&decoOrderInfos.size()>0){
                    for (DecoOrderInfo decoOrderInfo:decoOrderInfos){
                        String desc="";
                        if(decoOrderInfo.getDecoOrderNumbers()!=null){
                            desc="数量:"+decoOrderInfo.getDecoOrderNumbers().size();
                        }
                        CommonOrderBean commonOrderBean=new CommonOrderBean(decoOrderInfo.getId(),
                                CommonOrderBean.ITEM_TYPE_DECO,decoOrderInfo.getPrice(),
                                decoOrderInfo.getState(),
                                decoOrderInfo.getPayState(),decoOrderInfo.getCreateTime(),
                                "汽车美容",
                                desc);
                        commonOrderBean.setTradeNo(decoOrderInfo.getOut_trade_no());
                        commonOrderBeans.add(commonOrderBean);
                    }
                }


            } catch (WSException e) {
               return e.getErrorMsg();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(s==null){
                if(!isRefreshData){
                    initSlidTab(commonOrderBeans);
                }else{
                    weakReference.get().refreshNewData(commonOrderBeans);
                }

            }
        }
    }
    public  class CommonOrderBean{
        public static final int ITEM_TYPE_META=10;
        public static final int ITEM_TYPE_OIL=11;
        public static final int ITEM_TYPE_DECO=12;

        int id;
        float price;
        int state;
        int payState;
        String createTime;
        String title;
        String desc;
        int itemType;
        String tradeNo;

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getPayState() {
            return payState;
        }

        public void setPayState(int payState) {
            this.payState = payState;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public CommonOrderBean(int id,int itemType,float price, int state, int payState, String createTime, String title, String desc) {
            this.id = id;
            this.price = price;
            this.state = state;
            this.payState = payState;
            this.createTime = createTime;
            this.title = title;
            this.desc = desc;
            this.itemType=itemType;
        }
    }
}
