package com.carbeauty.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;
import com.carbeauty.manager.CarManagerActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2016/3/6.
 */
public class IndividualFragment extends Fragment {
    ListView individualListView;
    TextView accountName;
    TextView plateNumber;
    Button btn_clk;
    PullRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_individual,null);
        individualListView= (ListView) v.findViewById(R.id.individualListView);
        accountName= (TextView) v.findViewById(R.id.accountName);
        plateNumber= (TextView) v.findViewById(R.id.plateNumber);
        btn_clk= (Button) v.findViewById(R.id.btn_clk);

        btn_clk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CarManagerActivity.class);
                startActivity(intent);
            }
        });
        initListView();

        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadUserInfoTask().execute();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadUserInfoTask().execute();
    }

    private void initListView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] individuals = new String[]{getString(R.string.individual0),getString(R.string.individual1),
                getString(R.string.individual2),getString(R.string.individual3)};
        int[] individualIcons=new int[]{R.drawable.my_icon_input,R.drawable.my_icon_set
                ,R.drawable.my_icon_zixun,R.drawable.my_icon_message};

        for(int i=0;i<individualIcons.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", individualIcons[i]);
            map.put("text", individuals[i]);
            data_list.add(map);
        }
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.item01, from, to);
        individualListView.setAdapter(sim_adapter);
        individualListView.setDividerHeight(1);
    }

    class LoadUserInfoTask extends AsyncTask<String,String,String> {
        UserInfo userInfo;
        List<CarInfo> carInfos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                userInfo=WSConnector.getInstance().getUserInfoById();
                carInfos=WSConnector.getInstance().getCarByUserId();
                System.out.println(userInfo+" \n "+carInfos);

            } catch (WSException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeRefreshLayout.setRefreshing(false);
            if(userInfo!=null){
                accountName.setText(userInfo.getLoginName());
            }

            if(carInfos!=null&&carInfos.size()>0){
                if(carInfos.size()>1){
                    plateNumber.setText(carInfos.get(0).getNumber()+" 更多");
                }else{
                    plateNumber.setText(carInfos.get(0).getNumber()+"");
                }

            }else{
                plateNumber.setText("请添加车牌信息！");
            }

        }
    }

}
