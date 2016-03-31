package com.carbeauty.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.camera.VideoInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CameraListType;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ShopVideoFragment extends Fragment{
    ListView cameraListView;
    int shopId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fr_shopvideo, null);
        cameraListView= (ListView) v.findViewById(R.id.cameraListView);

        shopId=ContentBox.getValueInt(getActivity(),ContentBox.KEY_SHOP_ID,-1);


        new GetCameraTask().execute();

        /*
            Context context, List<? extends Map<String, ?>> data,
            @LayoutRes int resource, String[] from, @IdRes int[] to
         */





        return v;
    }
    class GetCameraTask extends AsyncTask<String,String,String>{
        List<CameraListType> cameraListTypes;
        @Override
        protected String doInBackground(String... params) {
            try {
               cameraListTypes=WSConnector.getInstance().getCameraList(shopId);
            } catch (WSException e) {
               return e.getErrorMsg();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                List<Map<String,String>> data=new ArrayList<Map<String,String>>();
//                for (CameraListType cameraListType:cameraListTypes){
//                    Map<String,String> map=new HashMap<String,String>();
//                    map.put("name",cameraListType.getName());
//                    map.put("uid",cameraListType.getUid());
//
//                    data.add(map);
//                }
                for (int i=0;i<4;i++){
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("name","视频"+i);


                    data.add(map);
                }

                String from[]={"name"};
                int to[]={R.id.nameTextView};

                SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity()
                        ,data
                        ,R.layout.item_camera
                        ,from,to);

                cameraListView.setAdapter(simpleAdapter);

                cameraListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity()
//                                ,cameraListTypes.get(position).toString()
//                                ,Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getActivity(), VideoInfoActivity.class);
                        intent.putExtra("Title","门店视频");
                        getActivity().startActivity(intent);
                    }
                });
            }
        }
    }

}
