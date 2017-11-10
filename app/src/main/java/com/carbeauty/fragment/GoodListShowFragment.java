package com.carbeauty.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.adapter.GoodListShowAdapter;
import com.carbeauty.good.GoodOrderListShowActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsOrderListType;

/**
 * Created by Administrator on 2016/7/16.
 */
public class GoodListShowFragment extends Fragment {

    ListView goodOrderlistView;


    GoodListShowAdapter goodListShowAdapter;
    PullRefreshLayout swipeRefreshLayout;
    List<GoodsOrderListType> goodsOrderListTypes;
    List<GoodInfo> goodInfos;
    GoodOrderListShowActivity activity;
    WeakReference<GoodListShowFragment> weakReference;

    /** type::: 未发货--0  已发货--1  未支付--2  已支付--3 已取消--4  已收货--5**/
    int type;

    public static GoodListShowFragment getInstance(int type,List<GoodsOrderListType> goodsOrderListTypes,List<GoodInfo> goodInfos) {
        GoodListShowFragment goodListShowFragment = new GoodListShowFragment();

        goodListShowFragment.goodsOrderListTypes=filterGoodsOrderData(type,goodsOrderListTypes);
        goodListShowFragment.type=type;
        goodListShowFragment.goodInfos=goodInfos;
        return goodListShowFragment;
    }

    private static List<GoodsOrderListType>  filterGoodsOrderData(int type,
                                                                  List<GoodsOrderListType> goodsOrderListTypes){

        List<GoodsOrderListType> list=new ArrayList<GoodsOrderListType>();
        for (GoodsOrderListType orderListType:goodsOrderListTypes){

            if (orderListType.getState()==type){
                list.add(orderListType);
            }
        }

        Map<String,GoodsOrderListType> cacheMap=new HashMap<String,GoodsOrderListType>();

        for (GoodsOrderListType orderListType:list){
            if(orderListType.getTradeNo()==null||"".equals(orderListType.getTradeNo())){
                continue;
            }
            GoodsOrderListType existObj=cacheMap.get(orderListType.getTradeNo());


           if(existObj==null){
               cacheMap.put(orderListType.getTradeNo(),orderListType);
           }else{
               /**
                * 组合数据
                */
               existObj.setGoodsInfo(existObj.getGoodsInfo()+","+orderListType.getGoodsInfo());
               existObj.setPrice(existObj.getPrice()+orderListType.getPrice());
               cacheMap.put(existObj.getTradeNo(),existObj);
               System.out.println("组合数据....");
           }
        }
        Collection<GoodsOrderListType> collections=cacheMap.values();
        if (collections instanceof List){
            list = (List)collections;
        }else{
            list = new ArrayList(collections);
        }
        return list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (GoodOrderListShowActivity) context;

        weakReference=new WeakReference<GoodListShowFragment>(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_myorder,null);

        goodOrderlistView= (ListView) view.findViewById(R.id.listView4);




        goodListShowAdapter=new GoodListShowAdapter(getActivity(),goodsOrderListTypes,goodInfos,this);

        goodOrderlistView.setAdapter(goodListShowAdapter);

        swipeRefreshLayout= (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startRefreshing();
            }
        });

        return view;
    }
    public void  startRefreshing(){
        activity.setWeakReference(weakReference);
        activity.reloadOrderList();
    }

    public void refreshNewData(List<GoodsOrderListType> goodsOrderListTypes, List<GoodInfo> goodInfos){

        goodListShowAdapter.setGoodsOrderListTypes(filterGoodsOrderData(type,goodsOrderListTypes));
        goodListShowAdapter.setGoodInfos(goodInfos);
        goodListShowAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
