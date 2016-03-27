package com.carbeauty.good;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;

/**
 * Created by Administrator on 2016/3/22.
 */
public class GoodActivity extends HeaderActivity {
    GridView goodGridView;
    int shopId;
    PullRefreshLayout  swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goods);
        shopId= ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID, 0);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);
        goodGridView= (GridView) findViewById(R.id.goodGridView);
        swipeRefreshLayout= (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetGoodTask().execute();
            }
        });

        new GetGoodTask().execute();
    }

    class GetGoodTask extends AsyncTask<String,String,String>{
        List<GoodInfo> goodInfos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                goodInfos= WSConnector.getInstance().getGoodsList(shopId);
            } catch (WSException e) {
                return e.getErrorMsg();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            swipeRefreshLayout.setRefreshing(false);
            if(s==null){
                initGridView(goodInfos);
            }else {
                Toast.makeText(GoodActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initGridView(List<GoodInfo> goodInfos){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();


        for(GoodInfo goodInfo:goodInfos){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", goodInfo.getName());
            map.put("desc", goodInfo.getDesc());
            map.put("price", goodInfo.getPrice());
            data_list.add(map);
        }
        String [] from ={"name","desc","price"};
        int [] to = {R.id.goodName,R.id.goodDesc,R.id.goodPriceTxt};
        SimpleAdapter sim_adapter = new SimpleAdapter(this,
                data_list, R.layout.grid_gooditem, from, to);
        goodGridView.setAdapter(sim_adapter);
        goodGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(GoodActivity.this,"ok"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
