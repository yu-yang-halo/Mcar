package com.carbeauty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.web.WebBroswerActivity;

import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;
import cn.service.bean.MetalplateInfo;

/**
 * Created by Administrator on 2016/3/13.
 */
public class MetaPayFragment extends Fragment {
    TextView itemNums;
    TextView itemTotalPrice;
    Button itemBtn;
    ConvenientBanner convenientBanner;
    int shopId=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fr_metapay, null);
        convenientBanner= (ConvenientBanner) v.findViewById(R.id.convenientBanner);
        itemNums= (TextView) v.findViewById(R.id.itemNums);
        itemTotalPrice= (TextView) v.findViewById(R.id.itemTotalPrice);
        itemBtn= (Button) v.findViewById(R.id.itemBtn);

        shopId=ContentBox.getValueInt(getActivity(),ContentBox.KEY_SHOP_ID,-1);
        new GetMetalplateInfoTask().execute();
        return v;
    }

    class GetMetalplateInfoTask extends AsyncTask<String,String,String>{
        List<MetalplateInfo> metalplateInfos;
        @Override
        protected String doInBackground(String... params) {
            try {
                 metalplateInfos=WSConnector.getInstance().getMetalplateList(shopId);
            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            initConvenientBanner();
        }
    }

    private void initConvenientBanner(){
        List<Integer> ss=new ArrayList<Integer>();
        ss.add(1);
        ss.add(2);
        ss.add(3);
        ss.add(4);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalMetaHolderView>() {
                    @Override
                    public LocalMetaHolderView createHolder() {
                        return new LocalMetaHolderView();
                    }
                }, ss)
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

    }
    public class LocalMetaHolderView implements Holder<Integer> {
        View rootView;
        @Override
        public View createView(Context context) {
            rootView=LayoutInflater.from(context).inflate(R.layout.root,null);

            return rootView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            LinearLayout linearLayout= (LinearLayout) rootView;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            if (data==1){
                 View item0=LayoutInflater.from(context).inflate(R.layout.fr_meta_item0,null);
                 linearLayout.addView(item0,param);
             }else  if (data==2){
                 View item1=LayoutInflater.from(context).inflate(R.layout.fr_meta_item1,null);

                 linearLayout.addView(item1,param);
             }else  if (data==3){
                View item2=LayoutInflater.from(context).inflate(R.layout.fr_meta_item0,null);

                linearLayout.addView(item2,param);
            }else  if (data==4){
                View item3=LayoutInflater.from(context).inflate(R.layout.fr_meta_item1,null);

                linearLayout.addView(item3,param);
            }
        }
    }

}
