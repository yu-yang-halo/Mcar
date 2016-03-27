package com.carbeauty.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.adapter.CouponAdapter;
import com.carbeauty.cache.ContentBox;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CouponInfo;

/**
 * Created by Administrator on 2016/3/26.
 */
public class CouponActivity extends HeaderActivity {
    PullRefreshLayout swipeRefreshLayout;
    ListView couponlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_coupon);
        initCustomActionBar();

        rightBtn.setVisibility(View.GONE);


        swipeRefreshLayout=(PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        couponlistView= (ListView) findViewById(R.id.couponlistView);
        couponlistView.setDividerHeight(50);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetCouponsTask().execute();
            }
        });

        new GetCouponsTask().execute();

    }


    class GetCouponsTask extends AsyncTask<String,String,String>{
        List<CouponInfo> couponInfos;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                couponInfos=WSConnector.getInstance().getCouponList(-1);
            } catch (WSException e) {
                return  e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            swipeRefreshLayout.setRefreshing(false);
            if(s==null){
                CouponAdapter couponAdapter=new CouponAdapter(CouponActivity.this,couponInfos);
                couponlistView.setAdapter(couponAdapter);
            }else{
                Toast.makeText(CouponActivity.this,s,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
