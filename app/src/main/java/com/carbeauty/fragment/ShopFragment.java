package com.carbeauty.fragment;

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

import com.carbeauty.R;
import com.carbeauty.adapter.ShopInfoAdapter;

import org.w3c.dom.Text;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ShopInfo;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ShopFragment extends Fragment {
    ListView shoplistView;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_shop, null);
        shoplistView= (ListView) v.findViewById(R.id.shoplistView);
        textView= (TextView) v.findViewById(R.id.textView);
        textView.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        new ShopInfosTask().execute();
    }

    class ShopInfosTask extends AsyncTask<String,String,String>{
        List<ShopInfo> shopInfos;
        @Override
        protected String doInBackground(String... params) {
            try {
                shopInfos=WSConnector.getInstance().getShopList();
                System.err.println(shopInfos);


            } catch (WSException e) {
               return e.getErrorMsg();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                textView.setVisibility(View.GONE);
                ShopInfoAdapter shopInfoAdapter=new ShopInfoAdapter(shopInfos,getActivity());
                shoplistView.setAdapter(shopInfoAdapter);

            }else {
                textView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }

        }
    }


}
