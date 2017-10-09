package com.carbeauty.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.adapter.PromotionAdapter;
import java.util.ArrayList;
import java.util.List;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.PromotionInfoType;

/**
 * Created by Administrator on 2016/3/6.
 */
public class PromotionFragment extends BaseFragment {
    ListView promlistView;
    PullRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_promotion,null);
        promlistView= (ListView) v.findViewById(R.id.promlistView);
        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPromotionInfo();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPromotionInfo();
    }

    private void loadPromotionInfo(){
        swipeRefreshLayout.setRefreshing(true);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                 List<PromotionInfoType> promotionInfoTypes = null;
                 List<Bitmap> bitmaps= new ArrayList<Bitmap>();
                boolean successYN=false;
                try {
                    promotionInfoTypes = WSConnector.getInstance().getPromotionList(5);

                    for (PromotionInfoType promotionInfoType:promotionInfoTypes){

                        String url= WSConnector.getPromotionURL(promotionInfoType.getImgName());
                        bitmaps.add(ImageUtils.convertNetToBitmap(url));

                    }
                    successYN=true;
                } catch (WSException e) {

                }
                final boolean finalSuccessYN = successYN;
                final List<PromotionInfoType> finalPromotionInfoTypes = promotionInfoTypes;
                final List<Bitmap> finalBitmaps = bitmaps;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if(finalSuccessYN){
                            PromotionAdapter promotionAdapter=new PromotionAdapter(finalPromotionInfoTypes,getActivity(),finalBitmaps);
                            promlistView.setAdapter(promotionAdapter);
                            promlistView.setDividerHeight(2);
                        }else {
                            Toast.makeText(getActivity(),"优惠券加载失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

}
