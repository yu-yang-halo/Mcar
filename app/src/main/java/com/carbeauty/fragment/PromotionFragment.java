package com.carbeauty.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.carbeauty.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/3/6.
 */
public class PromotionFragment extends Fragment {
    ListView promlistView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_promotion,null);
        promlistView= (ListView) v.findViewById(R.id.promlistView);
        initPromotionListView();
        return v;
    }
    private void initPromotionListView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] titles = new String[]{getString(R.string.protitle0),getString(R.string.protitle1)};
        String[] joins = new String[]{getString(R.string.projoin0),getString(R.string.projoin1)};

        int[] proIcons=new int[]{R.drawable.active_01,R.drawable.active_02};

        for(int i=0;i<proIcons.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", proIcons[i]);
            map.put("title", titles[i]);
            map.put("join", joins[i]);
            data_list.add(map);
        }
        String [] from ={"image","title","join"};
        int [] to = {R.id.image,R.id.title,R.id.joinNumbers};
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.item1, from, to);
        promlistView.setAdapter(sim_adapter);
        promlistView.setDividerHeight(0);
    }

}
