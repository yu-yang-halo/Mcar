package com.carbeauty.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.adapter.ShopInfoAdapter;
import com.carbeauty.cache.ContentBox;

import org.w3c.dom.Text;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ShopInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ShopFragment extends Fragment {
    ListView shoplistView;
    TextView textView;
    PullRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_shop, null);
        shoplistView= (ListView) v.findViewById(R.id.shoplistView);
        textView= (TextView) v.findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ShopInfosTask().execute();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ShopInfosTask().execute();

    }

    class ShopInfosTask extends AsyncTask<String,String,String>{
        List<ShopInfo> shopInfos;
        UserInfo userInfo;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                shopInfos=WSConnector.getInstance().getShopList();
                userInfo=WSConnector.getInstance().getUserInfoById();
                System.err.println(shopInfos);


            } catch (WSException e) {
               return e.getErrorMsg();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            swipeRefreshLayout.setRefreshing(false);
            if(s==null){
                textView.setVisibility(View.GONE);
                ShopInfoAdapter shopInfoAdapter=new ShopInfoAdapter(shopInfos,getActivity());
                shopInfoAdapter.setSelectShopId(userInfo.getShopId());

                shoplistView.setAdapter(shopInfoAdapter);
                ContentBox.loadInt(getActivity(),ContentBox.KEY_SHOP_ID,userInfo.getShopId());

            }else {
                textView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }

        }
    }


}
