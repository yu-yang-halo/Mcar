package com.carbeauty.fragment;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.adapter.PromotionAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.PromotionInfoType;

/**
 * Created by Administrator on 2016/3/6.
 */
public class PromotionFragment extends Fragment {
    ListView promlistView;
    PullRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_promotion,null);
        promlistView= (ListView) v.findViewById(R.id.promlistView);
        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PromotionInfoTask().execute();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new PromotionInfoTask().execute();
    }

    class PromotionInfoTask extends AsyncTask<String,String,String>{
        List<PromotionInfoType> promotionInfoTypes;
        List<Bitmap> bitmaps= new ArrayList<Bitmap>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                promotionInfoTypes = WSConnector.getInstance().getPromotionList(5);

                for (PromotionInfoType promotionInfoType:promotionInfoTypes){

                   //String url= WSConnector.getPromotionURL(promotionInfoType.getImgName());
                    String url="http://d.hiphotos.baidu.com/video/pic/item/ac6eddc451da81cb942f93755566d016082431b8.jpg";

                    bitmaps.add(ImageUtils.convertNetToBitmap(url));

                }


            } catch (WSException e) {
                return e.getErrorMsg();
            }
            System.out.println(promotionInfoTypes);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            swipeRefreshLayout.setRefreshing(false);
            if(s==null){
                PromotionAdapter promotionAdapter=new PromotionAdapter(promotionInfoTypes,getActivity(),bitmaps);
                promlistView.setAdapter(promotionAdapter);
                promlistView.setDividerHeight(2);
            }else {
                Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
            }
        }
    }



}
