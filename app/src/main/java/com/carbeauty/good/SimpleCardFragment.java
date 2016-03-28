package com.carbeauty.good;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.carbeauty.adapter.GoodsAdapter;
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
public class SimpleCardFragment extends Fragment {
    GridView goodGridView;
    PullRefreshLayout  swipeRefreshLayout;
    GoodsType goodsType;
    List<GoodInfo> goodInfos;
    GoodActivity goodActivity;
    public static SimpleCardFragment getInstance(GoodsType goodsType, List<GoodInfo> goodInfos) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.goodsType=goodsType;
        sf.filterMyGoodInfos(goodsType,goodInfos);
        return sf;
    }

    private void filterMyGoodInfos(GoodsType goodsType, List<GoodInfo> goodInfosParam){
        this.goodInfos=new ArrayList<GoodInfo>();

        for (GoodInfo goodInfo:goodInfosParam){
            if(goodInfo.getType()!=goodsType.getId()){
                continue;
            }
            this.goodInfos.add(goodInfo);
        }

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

            }
        });

        initGridView();
        return v;
    }






    private void initGridView(){
        GoodsAdapter goodsAdapter=new GoodsAdapter(getActivity(),goodInfos);

        goodGridView.setAdapter(goodsAdapter);
        goodGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "ok" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}