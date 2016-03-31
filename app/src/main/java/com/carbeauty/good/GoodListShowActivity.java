package com.carbeauty.good;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.adapter.GoodListShowAdapter;
import com.carbeauty.order.HeaderActivity;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsOrderListType;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GoodListShowActivity extends HeaderActivity {
    PullRefreshLayout  swipeRefreshLayout;
    ListView goodlistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodslistshow);

        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);


        swipeRefreshLayout=(PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        goodlistView= (ListView) findViewById(R.id.goodlistView);
        goodlistView.setDividerHeight(20);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetGoodOrderListTask().execute();
            }
        });

        new GetGoodOrderListTask().execute();

    }

    class GetGoodOrderListTask extends AsyncTask<String,String,String>{
        List<GoodsOrderListType> goodsOrderListTypes;
        List<GoodInfo> goodInfos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
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
            swipeRefreshLayout.setRefreshing(false);
            if(s==null){
                GoodListShowAdapter goodListShowAdapter=new GoodListShowAdapter(GoodListShowActivity.this,goodsOrderListTypes,goodInfos);

                goodlistView.setAdapter(goodListShowAdapter);

            }else{
                Toast.makeText(GoodListShowActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
