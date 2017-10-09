package com.carbeauty.userlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.admin.NotificationUI;
import com.carbeauty.cache.MessageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


@SuppressLint("ValidFragment")
public class ItemFragment extends Fragment {
    ListView listView;
    TextView tips;
    PullRefreshLayout swipeRefreshLayout;
    int orderType;
    NotificationUI ctx;
    public static ItemFragment getInstance(int orderType) {
        ItemFragment sf = new ItemFragment();
        sf.orderType=orderType;
        return sf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx= (NotificationUI) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_item, null);
        listView= (ListView) v.findViewById(R.id.msglistview);
        tips= (TextView) v.findViewById(R.id.tips);
        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCacheData();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCacheData();
    }




    public void loadCacheData(){
        Stack<MessageManager.JPMessage> jpMessageStack=MessageManager.getInstance().getMessageStack(getActivity());


        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();

        while (!jpMessageStack.isEmpty()){
            MessageManager.JPMessage message=jpMessageStack.pop();
            if(message.getOrderType()!=orderType){
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text",  message.getMsgContent());
            data_list.add(map);
        }


        if(data_list.size()<=0){
            tips.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        }else{
            tips.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }


        String [] from ={"text"};
        int [] to = {R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.adapter_msg, from, to);
        listView.setAdapter(sim_adapter);
        listView.setDividerHeight(1);
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }




}