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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.UserAddressManager;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.cache.MessageManager;
import com.carbeauty.good.GoodOrderListShowActivity;
import com.carbeauty.good.GoodOrderActivity;
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
 * Created by Administrator on 2016/3/6.
 */
public class IndividualFragment extends BaseFragment {
    ListView individualListView;
    TextView accountName;
    RelativeLayout layoutCoupon;
    RelativeLayout layoutCar;
    PullRefreshLayout swipeRefreshLayout;

    TextView numberTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fr_individual,null);
        individualListView= (ListView) v.findViewById(R.id.individualListView);
        accountName= (TextView) v.findViewById(R.id.userNameText);
        layoutCoupon= (RelativeLayout) v.findViewById(R.id.layoutCoupon);
        layoutCar= (RelativeLayout) v.findViewById(R.id.layoutCar);
        numberTxt= (TextView) v.findViewById(R.id.textView50);

        Button exitBtn= (Button) v.findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        layoutCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CouponActivity.class);
                intent.putExtra("Title","我的优惠券");
                startActivity(intent);
            }
        });

        layoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CarManagerActivity.class);
                startActivity(intent);
            }
        });
        initListView();

        swipeRefreshLayout= (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserInfo();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserInfo();
    }

    private void initListView(){
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        String[] individuals = new String[]{getString(R.string.individual0),getString(R.string.individual1),getString(R.string.individual4),"我的购物车","消息"};
        int[] individualIcons=new int[]{R.drawable.my_icon_input,R.drawable.my_icon_set
                ,R.drawable.my_icon_address,
                 R.mipmap.icon_cart_item,R.drawable.my_icon_message};

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
        individualListView.setDividerHeight(2);


        int totalHeight = 0;
        int row=sim_adapter.getCount();

        View viewItem = sim_adapter.getView(0, null, individualListView);//这个很重要，那个展开的item的measureHeight比其他的大
        viewItem.measure(0, 0);
        totalHeight = viewItem.getMeasuredHeight()*row+(row-1)*individualListView.getDividerHeight();
        ViewGroup.LayoutParams params = individualListView.getLayoutParams();
        params.height = totalHeight;
        individualListView.setLayoutParams(params);



        individualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), MyOrderActivity.class);
                if(position==0){
                    intent.putExtra(Constants.AC_TYPE,Constants.AC_TYPE_ORDER_AFTER);
                    intent.putExtra("Title","我的订单");
                }else if(position==1){
                    intent=new Intent(getActivity(), GoodOrderListShowActivity.class);
                    intent.putExtra("Title","我的商品订单");
                }else if (position==2){
                    intent=new Intent(getActivity(), MyAddressActivity.class);
                    intent.putExtra("Title","收货地址");
                }else if(position==3){
                    List<CartManager.MyCartClass> myCartClasses=CartManager.getInstance().getMyCartClassList(getActivity());

                    if(myCartClasses==null
                            ||myCartClasses.size()<=0){
                        Toast.makeText(getActivity(),"您的购物车还没有任何商品哦",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        intent = new Intent(getActivity(), GoodOrderActivity.class);
                        intent.putExtra("Title", "我的购物车");
                    }


                }else if(position==4){
                    Stack<MessageManager.JPMessage> stack=MessageManager.getInstance().getMessageStack(getActivity());

                    if(stack==null||stack.size()<=0){
                        Toast.makeText(getActivity(),"暂时还没有最新消息",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = new Intent(getActivity(), NewMsgActivity.class);
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
                    userInfo=WSConnector.getInstance().getUserInfoById();
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
                            UserAddressManager.cacheUserInfoToLocal(getActivity(), finalUserInfo);
                        }

                        if(finalCarInfos !=null&& finalCarInfos.size()>0){
                            if(ContentBox.getValueInt(getActivity(), ContentBox.KEY_CAR_ID, -1)<=0){
                                ContentBox.loadInt(getActivity(), ContentBox.KEY_CAR_ID, finalCarInfos.get(0).getId());
                            }
                            IDataHandler.getInstance().setCarInfos(finalCarInfos);

                            if(finalCarInfos.size()>1){
                                numberTxt.setText(finalCarInfos.get(0).getNumber()+"..");
                            }else{
                                numberTxt.setText(finalCarInfos.get(0).getNumber());
                            }

                        } else {
                            ContentBox.loadInt(getActivity(), ContentBox.KEY_CAR_ID, -1);
                            numberTxt.setText("请添加车牌");
                        }

                    }
                });
            }
        });
    }
    

}
