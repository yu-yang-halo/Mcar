package com.carbeauty;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.carbeauty.adapter.CityAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CityInfo;

/**
 * Created by Administrator on 2016/3/20.
 */
public class CityActivity extends HeaderActivity {
    private ListView cityListView;
    List<CityInfo> cityInfoList;
    CityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_citylist);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);
        tvTitle.setText("选择城市");

        cityListView= (ListView) findViewById(R.id.cityListView);


        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ContentBox.loadInt(CityActivity.this,
                        ContentBox.KEY_CITY_ID, cityInfoList.get(position).getCityId());

                cityAdapter.notifyDataSetChanged();
                getIntent().putExtra("cityName",cityInfoList.get(position).getName());
                setResult(1, getIntent());

                finish();


            }
        });

        new GetCityTasks().execute();
    }
    class GetCityTasks extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                cityInfoList=WSConnector.getInstance().getCityList();
            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                cityAdapter=new CityAdapter(cityInfoList,CityActivity.this);
                cityListView.setAdapter(cityAdapter);

            }else {
                Toast.makeText(CityActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
