package com.carbeauty.good;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsType;


@SuppressLint("ValidFragment")
public class SimpleCardFragment extends Fragment implements GoodActivity.GoodsInfoListenser {
    GridView goodGridView;
    PullRefreshLayout  swipeRefreshLayout;
    GoodsType goodsType;
    GoodActivity goodActivity;
    public static SimpleCardFragment getInstance(GoodsType goodsType) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.goodsType=goodsType;
        return sf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        goodActivity= (GoodActivity) context;
        goodActivity.setListenser(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        goodGridView= (GridView) v.findViewById(R.id.goodGridView);
        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetGoodTask().execute();
            }
        });

        new GetGoodTask().execute();
        return v;
    }

    @Override
    public void onCallback(List<GoodInfo> goodInfos) {

    }

    class GetGoodTask extends AsyncTask<String,String,String> {
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
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
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
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.grid_gooditem, from, to);
        goodGridView.setAdapter(sim_adapter);
        goodGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "ok" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}