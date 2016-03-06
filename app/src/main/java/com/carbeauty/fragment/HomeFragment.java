package com.carbeauty.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.carbeauty.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private ConvenientBanner banner;
    private GridView gridView;
    private ListView listView;

    private int[] icon = { R.drawable.homepage_gridview_8, R.drawable.homepage_gridview_6,
            R.drawable.homepage_gridview_7, R.drawable.homepage_gridview_3,
            R.drawable.homepage_gridview_4,
            R.drawable.homepage_gridview_1};
    private String[] iconName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_home, null);
        banner= (ConvenientBanner) v.findViewById(R.id.convenientBanner);
        gridView = (GridView) v.findViewById(R.id.gridView);
        listView= (ListView) v.findViewById(R.id.listView);


        initBanner();
        initGridView();
        initListView();
        return v;
    }

    private void initGridView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        iconName = new String[]{getString(R.string.hitem0),
                getString(R.string.hitem1),getString(R.string.hitem2),
                getString(R.string.hitem3),getString(R.string.hitem4),
                getString(R.string.hitem5)};

        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.item, from, to);
        gridView.setAdapter(sim_adapter);
    }
    private void initListView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] msgs = new String[]{getString(R.string.litem0),getString(R.string.litem1)};
        int[] msgIcons=new int[]{R.mipmap.home_icon_hongbao,R.mipmap.home_icon_recommend};

        for(int i=0;i<msgIcons.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", msgIcons[i]);
            map.put("text", msgs[i]);
            data_list.add(map);
        }
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(getActivity(),
                data_list, R.layout.item0, from, to);
        listView.setAdapter(sim_adapter);
        listView.setDividerHeight(0);
    }

    private void initBanner(){
        List<Integer> localImages=new ArrayList<Integer>();
        localImages.add(getResId("ad",R.drawable.class));
        localImages.add(getResId("ad",R.drawable.class));
        localImages.add(getResId("ad", R.drawable.class));
        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                 .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                 .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public  int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

}