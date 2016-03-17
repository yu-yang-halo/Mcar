package com.carbeauty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.order.OrderReokActivity;
import com.carbeauty.web.WebBroswerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.service.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.BannerInfoType;
import cn.service.bean.MetalplateInfo;

/**
 * Created by Administrator on 2016/3/13.
 */
public class MetaPayFragment extends Fragment {
    TextView itemNums;
    TextView itemTotalPrice;
    Button itemBtn;
    ConvenientBanner convenientBanner;
    int shopId=-1;

    private Button btne1;
    private Button btnf1;
    private Button btnc1;
    private Button btnd1;
    private Button btnj1;
    private Button btne2;
    private Button btnf2;
    private Button btnc2;
    private Button btnd2;
    private Button btnj2;
    private Button btnaa;
    private Button btna1;
    private Button btna2;
    private Button btnbb;
    private Button btnb1;
    private Button btnb2;
    private Button btnii;
    private Button btnhh;
    private Button btngg;
    private LinearLayout layoutk1;
    private LinearLayout layoutqq;
    private LinearLayout layoutk2;
    private TextView textk1;
    private TextView textQQ;
    private TextView textk2;
    MyClickListenser myClickListenser;
    private Map<Integer,Boolean> selectMap=new HashMap<Integer,Boolean>();
    private Map<Integer,Integer> dataMap  =new HashMap<Integer,Integer>();
    List<MetalplateInfo> metalplateInfos;

    List<MetalplateInfo> selmetalplateInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fr_metapay, null);
        convenientBanner= (ConvenientBanner) v.findViewById(R.id.convenientBanner);
        itemNums= (TextView) v.findViewById(R.id.itemNums);
        itemTotalPrice= (TextView) v.findViewById(R.id.itemTotalPrice);
        itemBtn= (Button) v.findViewById(R.id.bOrderBtn);


        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selmetalplateInfos==null||selmetalplateInfos.size()==0){
                    Toast.makeText(getActivity(),"请选择项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(getActivity(),OrderReokActivity.class);
                intent.putExtra("Title",getActivity().getIntent().getStringExtra("Title"));
                intent.putExtra(Constants.AC_TYPE, Constants.AC_TYPE_META);
                IDataHandler.getInstance().setMetalplateInfoSet(selmetalplateInfos);
                startActivity(intent);

            }
        });

        shopId=ContentBox.getValueInt(getActivity(),ContentBox.KEY_SHOP_ID,-1);
        myClickListenser=new MyClickListenser();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        initMapData();
        caculateTotalPrice();
        new GetMetalplateInfoTask().execute();

    }

    private void initMapData(){
        selectMap.put(R.id.btne1,false);
        selectMap.put(R.id.btnf1,false);
        selectMap.put(R.id.btnc1,false);
        selectMap.put(R.id.btnd1,false);
        selectMap.put(R.id.btnj1,false);

        selectMap.put(R.id.btne2,false);
        selectMap.put(R.id.btnf2,false);
        selectMap.put(R.id.btnc2,false);
        selectMap.put(R.id.btnd2,false);
        selectMap.put(R.id.btnj2,false);

        selectMap.put(R.id.btnaa,false);
        selectMap.put(R.id.btna1,false);
        selectMap.put(R.id.btna2,false);

        selectMap.put(R.id.btnbb,false);
        selectMap.put(R.id.btnb1,false);
        selectMap.put(R.id.btnb2,false);

        selectMap.put(R.id.btnhh,false);
        selectMap.put(R.id.btnii,false);
        selectMap.put(R.id.btngg,false);

        dataMap.put(R.id.layoutk1,0);
        dataMap.put(R.id.layoutk2,0);
        dataMap.put(R.id.layoutqq,0);

    }


    private void caculateTotalPrice(){
        selmetalplateInfos=new ArrayList<MetalplateInfo>();
        float selectMapPrice=0;
        float dataMapPrice=0;
        int ci=0;
        int cj=0;
        for(Map.Entry<Integer,Boolean> entry: selectMap.entrySet()) {
             if(entry.getValue()==true){
                 selectMapPrice+=findPrice(entry.getKey(),1);
                 ci++;
             }
        }

        for(Map.Entry<Integer,Integer> entry: dataMap.entrySet()) {
             if(entry.getValue()>0){
                 dataMapPrice+=findPrice(entry.getKey(),entry.getValue())*entry.getValue();
                 cj++;
             }
        }

        itemNums.setText(""+(ci+cj));
        itemTotalPrice.setText(""+(selectMapPrice+dataMapPrice));


    }

    private float findPrice(int resid,int count){
        String numberName="";
        switch(resid){
            case R.id.btne1:
                numberName="E1";
                break;
            case R.id.btnf1:
                numberName="F1";
                break;
            case R.id.btnc1:
                numberName="C1";
                break;
            case R.id.btnd1:
                numberName="D1";
                break;
            case R.id.btnj1:
                numberName="J1";
                break;
            case R.id.btne2:
                numberName="E2";
                break;
            case R.id.btnf2:
                numberName="F2";
                break;
            case R.id.btnc2:
                numberName="C2";
                break;
            case R.id.btnd2:
                numberName="D2";
                break;
            case R.id.btnj2:
                numberName="J2";
                break;
            case R.id.btnaa:
                numberName="A";
                break;
            case R.id.btna1:
                numberName="A1";
                break;
            case R.id.btna2:
                numberName="A2";
                break;
            case R.id.btnbb:
                numberName="B";
                break;
            case R.id.btnb2:
                numberName="B2";
                break;
            case R.id.btnb1:
                numberName="B1";
                break;
            case R.id.btnhh:
                numberName="H";
                break;
            case R.id.btnii:
                numberName="I";
                break;
            case R.id.btngg:
                numberName="G";
                break;
            case R.id.layoutk1:
                numberName="K1";
                break;
            case R.id.layoutk2:
                numberName="K2";
                break;
            case R.id.layoutqq:
                numberName="Q";
                break;
        }

        float price=0;
        for (MetalplateInfo metalplateInfo :metalplateInfos){
            if(metalplateInfo.getNumber().equals(numberName)){
                metalplateInfo.setCount(count);
                selmetalplateInfos.add(metalplateInfo);
                price=metalplateInfo.getPrice();
                break;
            }
        }

        return price;

    }


    class MyClickListenser implements View.OnClickListener{

        @Override
        public void onClick(View v) {

              if(v.getId()==R.id.layoutk1){
                  int num = dataMap.get(v.getId());
                  num++;
                  if (num > 1) {
                      num = 0;
                  }
                  textk1.setText("" + num);
                  dataMap.put(v.getId(), num);
              }else if(v.getId()==R.id.layoutk2){
                  int num=dataMap.get(v.getId());
                  num++;
                  if(num>1){
                      num=0;
                  }
                  textk2.setText(""+num);
                  dataMap.put(v.getId(), num);
              }else if(v.getId()==R.id.layoutqq){
                  int num=dataMap.get(v.getId());
                  num++;
                  if(num>4){
                      num=0;
                  }
                  textQQ.setText(""+num);
                  dataMap.put(v.getId(), num);
              }else{
                  if(v.isSelected()){
                      v.setSelected(false);
                      selectMap.put(v.getId(),false);
                  }else{
                      v.setSelected(true);
                      selectMap.put(v.getId(), true);
                  }
              }




              caculateTotalPrice();

        }
    }

    class GetMetalplateInfoTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                 metalplateInfos=WSConnector.getInstance().getMetalplateList(shopId);
            } catch (WSException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            initConvenientBanner();
        }
    }

    private void initConvenientBanner(){
        List<Integer> ss=new ArrayList<Integer>();
        ss.add(1);
        ss.add(2);
        ss.add(3);
        ss.add(4);
        ss.add(5);
        ss.add(6);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalMetaHolderView>() {
                    @Override
                    public LocalMetaHolderView createHolder() {
                        return new LocalMetaHolderView();
                    }
                }, ss)
                .setPageIndicator(new int[]{R.mipmap.cicle0, R.mipmap.cicle1})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);


    }


    public class LocalMetaHolderView implements Holder<Integer> {
        View rootView;
        @Override
        public View createView(Context context) {
            rootView=LayoutInflater.from(context).inflate(R.layout.root,null);

            return rootView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            LinearLayout linearLayout= (LinearLayout) rootView;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            if (data==1){
                 View item0=LayoutInflater.from(context).inflate(R.layout.fr_meta_item0,null);

                 btne1= (Button) item0.findViewById(R.id.btne1);
                 btnf1= (Button) item0.findViewById(R.id.btnf1);
                 btnc1= (Button) item0.findViewById(R.id.btnc1);
                 btnd1= (Button) item0.findViewById(R.id.btnd1);
                 btnj1= (Button) item0.findViewById(R.id.btnj1);

                 btne1.setSelected((boolean)selectMap.get(R.id.btne1));
                 btnf1.setSelected((boolean)selectMap.get(R.id.btnf1));
                 btnc1.setSelected((boolean)selectMap.get(R.id.btnc1));
                 btnd1.setSelected((boolean)selectMap.get(R.id.btnd1));
                 btnj1.setSelected((boolean)selectMap.get(R.id.btnj1));

                btne1.setOnClickListener(myClickListenser);
                btnf1.setOnClickListener(myClickListenser);
                btnc1.setOnClickListener(myClickListenser);
                btnd1.setOnClickListener(myClickListenser);
                btnj1.setOnClickListener(myClickListenser);


                 linearLayout.addView(item0,param);
             }else  if (data==2){
                 View item1=LayoutInflater.from(context).inflate(R.layout.fr_meta_item1,null);

                btnf2= (Button) item1.findViewById(R.id.btnf2);
                btne2= (Button) item1.findViewById(R.id.btne2);
                btnc2= (Button) item1.findViewById(R.id.btnc2);
                btnd2= (Button) item1.findViewById(R.id.btnd2);
                btnj2= (Button) item1.findViewById(R.id.btnj2);


                btnf2.setSelected((boolean)selectMap.get(R.id.btnf2));
                btne2.setSelected((boolean)selectMap.get(R.id.btne2));
                btnc2.setSelected((boolean)selectMap.get(R.id.btnc2));
                btnd2.setSelected((boolean)selectMap.get(R.id.btnd2));
                btnj2.setSelected((boolean)selectMap.get(R.id.btnj2));


                btne2.setOnClickListener(myClickListenser);
                btnf2.setOnClickListener(myClickListenser);
                btnc2.setOnClickListener(myClickListenser);
                btnd2.setOnClickListener(myClickListenser);
                btnj2.setOnClickListener(myClickListenser);

                linearLayout.addView(item1,param);
             }else  if (data==3){
                View item2=LayoutInflater.from(context).inflate(R.layout.fr_meta_item2,null);

                btnaa= (Button) item2.findViewById(R.id.btnaa);
                btna1= (Button) item2.findViewById(R.id.btna1);
                btna2= (Button) item2.findViewById(R.id.btna2);

                btnaa.setSelected((boolean)selectMap.get(R.id.btnaa));
                btna1.setSelected((boolean)selectMap.get(R.id.btna1));
                btna2.setSelected((boolean)selectMap.get(R.id.btna2));

                btnaa.setOnClickListener(myClickListenser);
                btna1.setOnClickListener(myClickListenser);
                btna2.setOnClickListener(myClickListenser);

                linearLayout.addView(item2,param);
            }else  if (data==4){
                View item3=LayoutInflater.from(context).inflate(R.layout.fr_meta_item3,null);

                btnbb= (Button) item3.findViewById(R.id.btnbb);
                btnb1= (Button) item3.findViewById(R.id.btnb1);
                btnb2= (Button) item3.findViewById(R.id.btnb2);

                btnbb.setSelected((boolean)selectMap.get(R.id.btnbb));
                btnb1.setSelected((boolean)selectMap.get(R.id.btnb1));
                btnb2.setSelected((boolean)selectMap.get(R.id.btnb2));

                btnbb.setOnClickListener(myClickListenser);
                btnb1.setOnClickListener(myClickListenser);
                btnb2.setOnClickListener(myClickListenser);

                linearLayout.addView(item3,param);
            }else  if (data==5){
                View item4=LayoutInflater.from(context).inflate(R.layout.fr_meta_item4,null);

                btnii= (Button) item4.findViewById(R.id.btnii);
                btnhh= (Button) item4.findViewById(R.id.btnhh);
                btngg= (Button) item4.findViewById(R.id.btngg);

                btnii.setSelected((boolean)selectMap.get(R.id.btnii));
                btnhh.setSelected((boolean)selectMap.get(R.id.btnhh));
                btngg.setSelected((boolean)selectMap.get(R.id.btngg));

                btnii.setOnClickListener(myClickListenser);
                btnhh.setOnClickListener(myClickListenser);
                btngg.setOnClickListener(myClickListenser);

                linearLayout.addView(item4,param);
            }else  if (data==6){
                View item5=LayoutInflater.from(context).inflate(R.layout.fr_meta_item5,null);

                layoutk1= (LinearLayout) item5.findViewById(R.id.layoutk1);
                layoutk2= (LinearLayout) item5.findViewById(R.id.layoutk2);
                layoutqq= (LinearLayout) item5.findViewById(R.id.layoutqq);




                textk1= (TextView) item5.findViewById(R.id.textk1);
                textk2= (TextView) item5.findViewById(R.id.textk2);
                textQQ= (TextView) item5.findViewById(R.id.textQQ);

                int k1value=dataMap.get(R.id.layoutk1);
                int k2value=dataMap.get(R.id.layoutk2);
                int qqvalue=dataMap.get(R.id.layoutqq);

                textk1.setText(""+k1value);
                textk2.setText(""+k2value);
                textQQ.setText(""+qqvalue);

                layoutk1.setOnClickListener(myClickListenser);
                layoutk2.setOnClickListener(myClickListenser);
                layoutqq.setOnClickListener(myClickListenser);

                linearLayout.addView(item5,param);
            }
        }
    }

}
