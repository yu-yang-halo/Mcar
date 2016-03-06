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

/**
 * Created by Administrator on 2016/3/6.
 */
public class IndividualFragment extends Fragment {
    ListView individualListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_individual,null);
        individualListView= (ListView) v.findViewById(R.id.individualListView);
        initListView();
        return v;
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

}
