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
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.adapter.GoodLookAdapter;
import com.carbeauty.adapter.MyOrderAdapter;
import com.carbeauty.order.MyOrderActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.service.bean.GoodInfo;
import cn.service.bean.GoodsType;

/**
 * Created by Administrator on 2016/7/16.
 */
public class MyOrderFragment extends Fragment {
    List<MyOrderActivity.CommonOrderBean> commonOrderBeenList;
    /** type  "待付款"--0,"已付款"--1,"待确认"--2,"已完成"--3,"已退款"--4  **/
    int type;
    ListView orderList;
    PullRefreshLayout  swipeRefreshLayout;
    MyOrderActivity activity;
    MyOrderAdapter orderAdapter;
    WeakReference<MyOrderFragment> selfweakReference;
    public static MyOrderFragment getInstance(int type,List<MyOrderActivity.CommonOrderBean> commonOrderBeans) {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        myOrderFragment.commonOrderBeenList=filterAdapterData(type,commonOrderBeans);
        myOrderFragment.type=type;


        return myOrderFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (MyOrderActivity) context;
        selfweakReference=new WeakReference<MyOrderFragment>(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_myorder,null);

        orderList= (ListView) view.findViewById(R.id.listView4);
        orderAdapter=new MyOrderAdapter(getActivity(),commonOrderBeenList,type,this);

        orderList.setAdapter(orderAdapter);


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
        activity.setWeakReference(selfweakReference);
        activity.reloadOrderList();
    }


    public void refreshNewData(List<MyOrderActivity.CommonOrderBean> commonOrderBeans){
        orderAdapter.setCommonOrderBeans(filterAdapterData(type,commonOrderBeans));
        orderAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    public static  List<MyOrderActivity.CommonOrderBean> filterAdapterData(int type,List<MyOrderActivity.CommonOrderBean> commonOrderBeans){
        List<MyOrderActivity.CommonOrderBean> list=new ArrayList<MyOrderActivity.CommonOrderBean>();
        if(type==0){
            for (MyOrderActivity.CommonOrderBean bean : commonOrderBeans){
                if(bean.getState()==Constants.STATE_ORDER_CANCEL){
                    continue;
                }
                if(bean.getPayState()== Constants.PAY_STATE_UNFINISHED
                        &&bean.getItemType()!= MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){
                    list.add(bean);
                }
            }
        }else if(type==1){
            for (MyOrderActivity.CommonOrderBean bean : commonOrderBeans){
                if(bean.getState()==Constants.STATE_ORDER_CANCEL){
                    continue;
                }
                if(bean.getPayState()== Constants.PAY_STATE_FINISHED
                        &&bean.getState()!=Constants.STATE_ORDER_FINISHED){
                    list.add(bean);
                }
            }
        }else if(type==2){
            for (MyOrderActivity.CommonOrderBean bean : commonOrderBeans){
                if(bean.getState()==Constants.STATE_ORDER_CANCEL){
                    continue;
                }
                if(bean.getItemType()== MyOrderActivity.CommonOrderBean.ITEM_TYPE_META){
                    list.add(bean);
                }
            }
        }else if(type==3){
            for (MyOrderActivity.CommonOrderBean bean : commonOrderBeans){
                if(bean.getState()==Constants.STATE_ORDER_CANCEL){
                    continue;
                }
                if(bean.getPayState()== Constants.PAY_STATE_FINISHED
                        &&bean.getState()==Constants.STATE_ORDER_FINISHED){
                    list.add(bean);
                }
            }
        }else if(type==4){
            for (MyOrderActivity.CommonOrderBean bean : commonOrderBeans){
                if(bean.getPayState()== Constants.PAY_STATE_FINISHED
                        &&bean.getState()==Constants.STATE_ORDER_CANCEL){
                    list.add(bean);
                }
            }
        }


        return list;


    }
}
