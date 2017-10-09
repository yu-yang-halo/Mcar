package com.carbeauty.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.BaseActivity;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.UserAddressManager;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.cache.MessageManager;
import com.carbeauty.good.GoodActivity;
import com.carbeauty.good.GoodOrderActivity;
import com.carbeauty.good.GoodOrderListShowActivity;
import com.carbeauty.good.MyAddressActivity;
import com.carbeauty.manager.CarManagerActivity;
import com.carbeauty.message.NewMsgActivity;
import com.carbeauty.order.CouponActivity;
import com.carbeauty.order.MyOrderActivity;
import com.carbeauty.userlogic.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;
import cn.service.bean.UserInfo;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class AdminUI extends BaseActivity {
    GridView individualListView;
    TextView accountName;
    PullRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_admin);

        ContentBox.loadInt(this, ContentBox.KEY_SHOP_ID, -1);

        individualListView= (GridView) findViewById(R.id.individualListView);
        accountName= (TextView) findViewById(R.id.userNameText);

        Button exitBtn= (Button) findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminUI.this, LoginActivity.class);
                startActivity(intent);
               finish();
            }
        });

        initListView();

        swipeRefreshLayout= (PullRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserInfo();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserInfo();
    }

    private void initListView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        final String[] individuals = new String[]{"采购商品","订单查看","收货地址","通知中心","购物车","金额统计"};
        int[] individualIcons=new int[]{R.mipmap.icon_caigou,R.mipmap.icon_order
                ,R.mipmap.icon_address, R.mipmap.icon_notification,R.mipmap.icon_cart,R.mipmap.icon_money};

        for(int i=0;i<individualIcons.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", individualIcons[i]);
            map.put("text", individuals[i]);
            data_list.add(map);
        }
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(this,
                data_list, R.layout.item02, from, to);
        individualListView.setAdapter(sim_adapter);



        int totalHeight = 0;
        int row=sim_adapter.getCount()%2==0?sim_adapter.getCount()/2:(sim_adapter.getCount()/2+1);

        View viewItem = sim_adapter.getView(0, null, individualListView);//这个很重要，那个展开的item的measureHeight比其他的大
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight()*row;
        ViewGroup.LayoutParams params = individualListView.getLayoutParams();
        params.height = totalHeight;
        individualListView.setLayoutParams(params);



        individualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AdminUI.this, MyOrderActivity.class);
                if(position==0){
                    intent=new Intent(AdminUI.this, GoodActivity.class);
                    intent.putExtra("Title", individuals[position]);
                    intent.putExtra("shopId", -1);
                    startActivity(intent);
                }else if(position==1){
                    intent=new Intent(AdminUI.this, GoodOrderListShowActivity.class);
                    intent.putExtra("Title","我的商品订单");
                }else if (position==2){
                    intent=new Intent(AdminUI.this, MyAddressActivity.class);
                    intent.putExtra("Title","收货地址");
                }else if(position==3){
                    intent = new Intent(AdminUI.this, NotificationUI.class);
                }else if(position==4){
                    List<CartManager.MyCartClass> myCartClasses=CartManager.getInstance().getMyCartClassList(AdminUI.this);
                    if(myCartClasses==null
                            ||myCartClasses.size()<=0){
                        Toast.makeText(AdminUI.this,"您的购物车还没有任何商品哦",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        intent = new Intent(AdminUI.this, GoodOrderActivity.class);
                        intent.putExtra("Title", "我的购物车");
                    }
                }else if(position==5){

                    intent = new Intent(AdminUI.this, PriceUI.class);
                    intent.putExtra("Title", "金额统计");

                }
                startActivity(intent);
            }
        });
    }

    private void loadUserInfo(){
        swipeRefreshLayout.setRefreshing(true);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = null;
                List<CarInfo> carInfos = null;

                try {
                    userInfo= WSConnector.getInstance().getUserInfoById();
                    carInfos=WSConnector.getInstance().getCarByUserId();
                    System.out.println(userInfo+" \n "+carInfos);

                } catch (WSException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                final UserInfo finalUserInfo = userInfo;
                final List<CarInfo> finalCarInfos = carInfos;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if(finalUserInfo !=null){
                            accountName.setText(finalUserInfo.getLoginName());
                            UserAddressManager.cacheUserInfoToLocal(AdminUI.this, finalUserInfo);
                        }

                        if(finalCarInfos !=null&& finalCarInfos.size()>0){
                            if(ContentBox.getValueInt(AdminUI.this, ContentBox.KEY_CAR_ID, -1)<=0){
                                ContentBox.loadInt(AdminUI.this, ContentBox.KEY_CAR_ID, finalCarInfos.get(0).getId());
                            }
                            IDataHandler.getInstance().setCarInfos(finalCarInfos);


                        } else {
                            ContentBox.loadInt(AdminUI.this, ContentBox.KEY_CAR_ID, -1);

                        }

                    }
                });
            }
        });
    }


}
