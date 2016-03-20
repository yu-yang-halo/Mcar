package com.carbeauty;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.carbeauty.adapter.CityAdapter;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CityInfo;

/**
 * Created by Administrator on 2016/3/20.
 */
public class CityActivity extends Activity {
    private ListView cityListView;
    List<CityInfo> cityInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_citylist);
        cityListView= (ListView) findViewById(R.id.cityListView);

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
                CityAdapter cityAdapter=new CityAdapter(cityInfoList,CityActivity.this);
                cityListView.setAdapter(cityAdapter);
            }else {
                Toast.makeText(CityActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
