package com.carbeauty.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.carbeauty.DensityUtil;
import com.carbeauty.ImageUtils;
import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.adapter.BitmapCache;
import com.carbeauty.adapter.HotGoodsAdapter;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.good.GoodActivity;
import com.carbeauty.good.GoodLookActivity;
import com.carbeauty.order.LookShopActivity;
import com.carbeauty.order.MetalplateActivity;
import com.carbeauty.order.WashOilActivity;
import com.carbeauty.web.WebBroswerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.carbeauty.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;
import cn.service.bean.GoodInfo;

public class HomeFragment extends Fragment {
    private ConvenientBanner banner;
    private GridView gridView;
    private ListView listView;
    private GridView hotgridView;
    private MainActivity mainActivity;
    List<String> localImages;
    RequestQueue mQueue;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
        Log.v("Home","onAttach...."+mainActivity);
        mQueue= Volley.newRequestQueue(context);
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
        Log.v("Home","onCreate....");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("Home","onCreateView....");
        View v = inflater.inflate(R.layout.fr_home, null);
        banner= (ConvenientBanner) v.findViewById(R.id.convenientBanner);


        gridView = (GridView) v.findViewById(R.id.gridView);
        listView= (ListView) v.findViewById(R.id.listView);
        hotgridView= (GridView) v.findViewById(R.id.hotgridView);


        initGridView();
        initListView();

        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendAsyncNetRequest();
            }
        });


        sendAsyncNetRequest();
        return v;
    }

    private void sendAsyncNetRequest(){

        new GetBannerTask().executeOnExecutor(Executors.newCachedThreadPool());

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



        int totalHeight = 0;
        if(sim_adapter.getCount()>0){

            int row=sim_adapter.getCount()%4==0?sim_adapter.getCount()/4:(sim_adapter.getCount()/4+1);


            View viewItem = sim_adapter.getView(0, null, gridView);//这个很重要，那个展开的item的measureHeight比其他的大
            viewItem.measure(0, 0);
            totalHeight = viewItem.getMeasuredHeight()*row;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
       // sim_adapter.notifyDataSetChanged();


    }

    private void toWeb(){
        Intent intent=new Intent(getActivity(),WebBroswerActivity.class);
        intent.putExtra("URL", WSConnector.getCheXianURL());
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
                if(position==0){
                    mainActivity.setSelectPos(2);
                }

            }
        });


        int totalHeight = 0;
        for (int i = 0; i < sim_adapter.getCount(); i++) {
            View viewItem = sim_adapter.getView(i, null, listView);//这个很重要，那个展开的item的measureHeight比其他的大
            viewItem.measure(0, 0);
            totalHeight += viewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);


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

    private void initHotGridView(final List<GoodInfo> hotGoodInfos){

        HotGoodsAdapter hotGoodsAdapter=new HotGoodsAdapter(getActivity(),hotGoodInfos);

        hotgridView.setAdapter(hotGoodsAdapter);

        hotgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), GoodActivity.class);
                intent.putExtra("Title", iconName[5]);
                intent.putExtra("Type", hotGoodInfos.get(position).getType());
                startActivity(intent);


            }
        });

        int totalHeight = 0;
        if(hotGoodsAdapter.getCount()>0){

            int row=hotGoodsAdapter.getCount()%2==0?hotGoodsAdapter.getCount()/2:(hotGoodsAdapter.getCount()/2+1);

            View viewItem = hotGoodsAdapter.getView(0, null, hotgridView);//这个很重要，那个展开的item的measureHeight比其他的大
            viewItem.measure(0, 0);
            totalHeight = viewItem.getMeasuredHeight()*row;
        }

        ViewGroup.LayoutParams params = hotgridView.getLayoutParams();
        params.height = totalHeight;
        hotgridView.setLayoutParams(params);

    }

    class GetGoodTopTask extends AsyncTask<String,String,String>{
        List<GoodInfo> goodInfos;
        List<GoodInfo> hotGoodInfos=new ArrayList<GoodInfo>();
        @Override
        protected String doInBackground(String... params) {
            try {
                goodInfos=WSConnector.getInstance().getGoodsList(-2);
                if(goodInfos!=null&&goodInfos.size()>0){
                    for (GoodInfo goodInfo:goodInfos){
                        if(goodInfo.isTop()){
                            hotGoodInfos.add(goodInfo);
                        }
                    }
                }


            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            initHotGridView(hotGoodInfos);
        }
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
               localImages=new ArrayList<String>();
               for (BannerInfoType bannerInfoType:bannerInfoTypes){
                   String url=WSConnector.getBannerURL(bannerInfoType.getImgName());
                   localImages.add(url);

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
            banner.startTurning(3000);
            new GetGoodTopTask().executeOnExecutor(Executors.newCachedThreadPool());
        }
    }


    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, final int position, String imageURL) {
            ImageLoader imageLoader=new ImageLoader(mQueue, new BitmapCache());
            ImageLoader.ImageListener listener=ImageLoader.getImageListener(imageView
                    , 0, 0);

            imageLoader.get(imageURL, listener);
        }
    }



}