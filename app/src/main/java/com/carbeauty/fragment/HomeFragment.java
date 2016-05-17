package com.carbeauty.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.carbeauty.ImageUtils;
import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.good.GoodActivity;
import com.carbeauty.order.LookShopActivity;
import com.carbeauty.order.MetalplateActivity;
import com.carbeauty.order.WashOilActivity;
import com.carbeauty.web.WebBroswerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carbeauty.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;

public class HomeFragment extends Fragment {
    private ConvenientBanner banner;
    private GridView gridView;
    private ListView listView;
    private MainActivity mainActivity;
    List<Bitmap> localImages;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         mainActivity= (MainActivity) context;
    }

    private int[] icon = { R.drawable.homepage_gridview_8, R.drawable.homepage_gridview_6,
            R.drawable.homepage_gridview_7, R.drawable.homepage_gridview_3,
            R.drawable.homepage_gridview_4,
            R.drawable.homepage_gridview_1,R.drawable.default_icon_serve};
    private String[] iconName;
    PullRefreshLayout swipeRefreshLayout;



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



        initGridView();
        initListView();

        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetBannerTask().execute();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new GetBannerTask().execute();
    }

    private void initGridView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        iconName = new String[]{getString(R.string.hitem0),
                getString(R.string.hitem1),getString(R.string.hitem2),
                getString(R.string.hitem3),getString(R.string.hitem4),
                getString(R.string.hitem5),"自助洗"};

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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int shopId = ContentBox.getValueInt(getActivity(), ContentBox.KEY_SHOP_ID, -1);
                int carId = ContentBox.getValueInt(getActivity(), ContentBox.KEY_CAR_ID, -1);
                if (shopId <= 0) {
                    mainActivity.setSelectPos(1);
                    Toast.makeText(getActivity(), "请先选择店铺", Toast.LENGTH_SHORT).show();
                } else {

                    if (carId <= 0) {
                        Toast.makeText(getActivity(), "为了不影响您的订单提交，请添加车牌信息", Toast.LENGTH_SHORT).show();
                    }

                    if (position == 0 || position == 1) {
                        Intent intent = new Intent(getActivity(), WashOilActivity.class);
                        if (position == 0) {
                            intent.putExtra(Constants.AC_TYPE, Constants.AC_TYPE_WASH);
                        } else if (position == 1) {
                            intent.putExtra(Constants.AC_TYPE, Constants.AC_TYPE_OIL);
                        }
                        intent.putExtra("Title", iconName[position]);
                        startActivity(intent);
                    } else if (position == 2) {
                        toMetalplate(position);
                    } else if (position == 3) {
                        toLookShop(position);
                    } else if (position == 5) {
                        toGoods(position);
                    } else if (position == 6) {
                        Toast.makeText(getActivity(), "即将上线，敬请期待~", Toast.LENGTH_SHORT).show();
                    }else if(position==4){
                        toWeb();
                    }


                }

            }
        });
    }
    private void toWeb(){
        Intent intent=new Intent(getActivity(),WebBroswerActivity.class);
        intent.putExtra("URL", "http://j.autohome.com.cn/chexian-calculation.html");
        intent.putExtra("Title","车险直销");
        getActivity().startActivity(intent);
    }
    private void toMetalplate(int position){
        Intent intent=new Intent(getActivity(), MetalplateActivity.class);
        intent.putExtra("Title", iconName[position]);
        startActivity(intent);
    }
    private void toLookShop(int position){
        Intent intent=new Intent(getActivity(), LookShopActivity.class);
        intent.putExtra("Title", iconName[position]);
        startActivity(intent);
    }
    private void toGoods(int position){
        Intent intent=new Intent(getActivity(), GoodActivity.class);
        intent.putExtra("Title", iconName[position]);
        startActivity(intent);
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
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.setSelectPos(2);
            }
        });
    }




    private void initBanner(final List<BannerInfoType> bannerInfoTypes){
        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                 .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                 .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                Intent intent=new Intent(getActivity(),WebBroswerActivity.class);
                intent.putExtra("URL", bannerInfoTypes.get(position).getSrc());
                intent.putExtra("Title","广告详情");
                getActivity().startActivity(intent);
            }
        });
    }

    class GetBannerTask extends AsyncTask<String,String,String>{
        List<BannerInfoType> bannerInfoTypes;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
               bannerInfoTypes=WSConnector.getInstance().getBannerList(3);
               localImages=new ArrayList<Bitmap>();
               for (BannerInfoType bannerInfoType:bannerInfoTypes){
                   //String url="http://d.hiphotos.baidu.com/video/pic/item/ac6eddc451da81cb942f93755566d016082431b8.jpg";
                   String url=WSConnector.getBannerURL(bannerInfoType.getImgName());
                   Bitmap bm=ImageUtils.convertNetToBitmap(url);
                   if(bm!=null){
                       localImages.add(bm);
                   }

               }

            } catch (WSException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeRefreshLayout.setRefreshing(false);
            initBanner(bannerInfoTypes);
        }
    }


    public class LocalImageHolderView implements Holder<Bitmap> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, final int position, Bitmap bm) {
            imageView.setImageBitmap(bm);
        }
    }



}