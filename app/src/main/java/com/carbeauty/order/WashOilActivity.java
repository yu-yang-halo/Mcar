package com.carbeauty.order;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.TimeUtils;
import com.carbeauty.adapter.DecorationAdapter;
import com.carbeauty.adapter.MyHandlerCallback;
import com.carbeauty.adapter.OilInfoAdapter;
import com.carbeauty.adapter.OrderTimeAdapter;
import com.carbeauty.alertDialog.WindowUtils;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.carbeauty.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.DecorationInfo;
import cn.service.bean.OilInfo;
import cn.service.bean.OrderStateType;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Administrator on 2016/3/10.
 */
public class WashOilActivity extends HeaderActivity {


    private int ac_type_value=0;


    private int shopId;
    private GridView gridView;
    private ListView listView;
    TextView itemTotalPrice;
    TextView itemNums;
    Button bOrderBtn;



    DecorationAdapter decorationAdapter;
    OilInfoAdapter oilInfoAdapter;

    SegmentedGroup timeSegmentGroup;

    private int oldPostion=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_washoil);


        timeSegmentGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        timeSegmentGroup.check(R.id.button21);
        timeSegmentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.button21) {
                    ContentBox.loadInt(WashOilActivity.this, ContentBox.KEY_WAHT_DAY, 0);
                    new GetDataList(0).execute();
                } else if (checkedId == R.id.button22) {
                    ContentBox.loadInt(WashOilActivity.this, ContentBox.KEY_WAHT_DAY, 1);
                    new GetDataList(1).execute();
                }
            }
        });


        ac_type_value=getIntent().getIntExtra(Constants.AC_TYPE,Constants.AC_TYPE_WASH);
        initCustomActionBar();

        shopId= ContentBox.getValueInt(this,ContentBox.KEY_SHOP_ID, 0);

        gridView= (GridView) findViewById(R.id.gridView2);
        listView= (ListView) findViewById(R.id.listView2);

        itemTotalPrice= (TextView) findViewById(R.id.itemTotalPrice);
        itemNums= (TextView) findViewById(R.id.itemNums);
        bOrderBtn= (Button) findViewById(R.id.bOrderBtn);

        bOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selTime=ContentBox.getValueString(WashOilActivity.this, ContentBox.KEY_ORDER_TIME,null);
                if(selTime==null){
                    Toast.makeText(WashOilActivity.this,"请选择预定时间",Toast.LENGTH_SHORT).show();
                }else if(ac_type_value==Constants.AC_TYPE_WASH){
                    if(decorationAdapter.getDecoCollections().size()==0){
                        Toast.makeText(WashOilActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }else{
                        //commit
                        Intent intent=new Intent(WashOilActivity.this,OrderReokActivity.class);
                        intent.putExtra("Title",getIntent().getStringExtra("Title"));
                        intent.putExtra(Constants.AC_TYPE,ac_type_value);
                        IDataHandler.getInstance().setDecorationInfoSet(decorationAdapter.getDecoCollections());
                        startActivity(intent);
                    }
                }else if(ac_type_value==Constants.AC_TYPE_OIL){
                    if(oilInfoAdapter.getOilCollections().size()==0){
                        Toast.makeText(WashOilActivity.this,"请选择保养项目",Toast.LENGTH_SHORT).show();
                    }else{
                        //commit
                        Intent intent=new Intent(WashOilActivity.this,OrderReokActivity.class);
                        intent.putExtra(Constants.AC_TYPE, ac_type_value);
                        intent.putExtra("Title",getIntent().getStringExtra("Title"));
                        IDataHandler.getInstance().setOilInfoSet(oilInfoAdapter.getOilCollections());
                        startActivity(intent);

                    }
                }

            }
        });

        ContentBox.loadInt(WashOilActivity.this, ContentBox.KEY_WAHT_DAY, 0);
        new GetDataList(-1).execute();

    }

    private void initGridView(List<OrderStateType> orderStateTypes){
        if(orderStateTypes==null){
            return;
        }

        OrderTimeAdapter orderTimeAdapter=new OrderTimeAdapter(orderStateTypes,this);

        gridView.setAdapter(orderTimeAdapter);
        int row=orderStateTypes.size()%6==0?orderStateTypes.size()/6:orderStateTypes.size()/6+1;
        //System.out.println("height:" + row*WindowUtils.dip2px(this,30));

        ViewGroup.LayoutParams layoutParams=gridView.getLayoutParams();

        layoutParams.height=row*WindowUtils.dip2px(this,30);

        gridView.setLayoutParams(layoutParams);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData(){




        float total=0;
        int data=0;

        if(ac_type_value==Constants.AC_TYPE_WASH){
            if(decorationAdapter==null){
                return;
            }
            List<DecorationInfo> decorationInfoSet=decorationAdapter.getDecoCollections();
            for (DecorationInfo decorationInfo:decorationInfoSet){
                total+=decorationInfo.getPrice();
                data++;
            }
            decorationAdapter.notifyDataSetChanged();
        }else if(ac_type_value==Constants.AC_TYPE_OIL){
            if(oilInfoAdapter==null){
                return;
            }
            List<OilInfo> oilInfoSet=oilInfoAdapter.getOilCollections();

            for (OilInfo oilInfo:oilInfoSet){
                total+=oilInfo.getPrice();
                data++;
            }
            oilInfoAdapter.notifyDataSetChanged();
        }
        itemNums.setText(data+"");
        itemTotalPrice.setText(total+"元");

    }
    private void initDecorationListView(final List<DecorationInfo> decorationInfos){
        decorationAdapter=new DecorationAdapter(decorationInfos,this);
        decorationAdapter.setMyHandlerCallback(new MyHandlerCallback() {
            @Override
            public void clickAfter() {
                initData();
            }
        });
        listView.setAdapter(decorationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DecorationInfo data = decorationInfos.get(position);
                if (oldPostion == position) {
                    if (data.isExpand())  {
                        oldPostion = -1;
                    }
                    data.setIsExpand(!data.isExpand());
                }else{
                    oldPostion = position;
                    data.setIsExpand(true);
                }

                int totalHeight = 0;
                for(int i=0;i<decorationAdapter.getCount();i++) {
                    View viewItem = decorationAdapter.getView(i, null, listView);//这个很重要，那个展开的item的measureHeight比其他的大
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalHeight
                        + (listView.getDividerHeight() * (listView.getCount() - 1));
                listView.setLayoutParams(params);
                decorationAdapter.notifyDataSetChanged();
            }
        });
    }
    private void initOilInfoListView(final List<OilInfo> oilInfos){
        oilInfoAdapter=new OilInfoAdapter(oilInfos,this);
        oilInfoAdapter.setMyHandlerCallback(new MyHandlerCallback() {
            @Override
            public void clickAfter() {
                initData();
            }
        });
        listView.setAdapter(oilInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OilInfo data = oilInfos.get(position);
                if (oldPostion == position) {
                    if (data.isExpand()) {
                        oldPostion = -1;
                    }
                    data.setIsExpand(!data.isExpand());
                } else {
                    oldPostion = position;
                    data.setIsExpand(true);
                }

                int totalHeight = 0;
                for (int i = 0; i < oilInfoAdapter.getCount(); i++) {
                    View viewItem = oilInfoAdapter.getView(i, null, listView);//这个很重要，那个展开的item的measureHeight比其他的大
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalHeight
                        + (listView.getDividerHeight() * (listView.getCount() - 1));
                listView.setLayoutParams(params);
                oilInfoAdapter.notifyDataSetChanged();
            }
        });
    }



    class GetDataList extends AsyncTask<String,String,String>{
        List<OrderStateType> orderStateTypes;
        List<OilInfo> oilInfos;
        List<DecorationInfo> decorationInfos;
        int incr;
        GetDataList(int incr){
            this.incr=incr;
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                if(ac_type_value==Constants.AC_TYPE_OIL) {
                    orderStateTypes = WSConnector.getInstance().getDayOrderStateList(Constants.SEARCH_TYPE_OIL, shopId, incr);
                }else  if(ac_type_value==Constants.AC_TYPE_WASH){
                    orderStateTypes=WSConnector.getInstance().getDayOrderStateList(Constants.SEARCH_TYPE_DECO, shopId, incr);
                }

                if(incr<0){
                    if(ac_type_value==Constants.AC_TYPE_OIL){

                        oilInfos=WSConnector.getInstance().getOilList(shopId);

                    }else  if(ac_type_value==Constants.AC_TYPE_WASH){

                        decorationInfos=WSConnector.getInstance().getDecorationList(shopId);
                    }
                }


            } catch (WSException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            initGridView(orderStateTypes);
            if(incr<0){
                if(ac_type_value==Constants.AC_TYPE_WASH){
                    initDecorationListView(decorationInfos);
                }else  if(ac_type_value==Constants.AC_TYPE_OIL){
                    initOilInfoListView(oilInfos);
                }
            }


        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100&&resultCode>0){
            ContentBox.loadInt(this,ContentBox.KEY_CAR_ID,resultCode);
            rightBtn.setText(data.getStringExtra("number"));
        }
    }
}
