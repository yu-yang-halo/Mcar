package com.carbeauty.good;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.Constants;
import com.carbeauty.R;
import com.carbeauty.UserAddressManager;
import com.carbeauty.adapter.GoodLookAdapter;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;
import com.carbeauty.order.OrderResultActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Iterator;
import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodOrderActivity extends FragmentActivity {
    ListView goodslistview;

    ActionBar mActionbar;

    CheckBox checkAll;
    TextView totalPrice;
    Button createOrderButton;
    GoodLookAdapter goodLookAdapter;
    Button rightBtn;

    String address;
    String name;
    String phone;

    TextView addressDescription;
    List<CartManager.MyCartClass> myCartClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodsorder);
        initCustomActionBar();


        goodslistview= (ListView) findViewById(R.id.goodslistview);



        checkAll= (CheckBox) findViewById(R.id.checkBox3);
        totalPrice= (TextView) findViewById(R.id.textView13);

        createOrderButton= (Button) findViewById(R.id.button2);

        addressDescription= (TextView) findViewById(R.id.textView4);

        myCartClassList=CartManager.getInstance().getMyCartClassList(this);

        goodLookAdapter=new GoodLookAdapter(this,myCartClassList);

        goodLookAdapter.setTotalPriceTxt(totalPrice);

        goodslistview.setAdapter(goodLookAdapter);

        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                goodLookAdapter.selectAll(isChecked);

            }
        });

        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (address.equals("") || name.equals("") || phone.equals("")) {
                    Toast.makeText(GoodOrderActivity.this, "请添加我的地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (goodLookAdapter.getCommitData()==null||goodLookAdapter.getCommitData().size() == 0) {
                    Toast.makeText(GoodOrderActivity.this, "请选择购买的商品", Toast.LENGTH_SHORT).show();
                } else if (name.trim().equals("") || address.trim().equals("") || phone.trim().equals("")) {
                    Toast.makeText(GoodOrderActivity.this, "地址/姓名/手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    new CommitOrderTask(goodLookAdapter.getCommitData()).execute();
                }


            }
        });
        addressDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddress();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        address=ContentBox.getValueString(GoodOrderActivity.this, UserAddressManager.KEY_ADDRESS, "");
        name=ContentBox.getValueString(GoodOrderActivity.this,  UserAddressManager.KEY_NAME, "");
        phone=ContentBox.getValueString(GoodOrderActivity.this,  UserAddressManager.KEY_PHONE, "");
        if(!address.equals("")&&!name.equals("")&&!phone.equals("")){
            addressDescription.setText("地址:"+address+"\n收货人姓名:"+name+" 联系电话:"+phone);
        }

    }

    class CommitOrderTask extends AsyncTask<String,String,String>{
        List<GoodLookAdapter.CommitDataBean> commitDataBeanList;
        KProgressHUD progressHUD;
        CommitOrderTask(List<GoodLookAdapter.CommitDataBean> commitDataBeanList){
            this.commitDataBeanList=commitDataBeanList;
        }

        @Override
        protected void onPreExecute() {
            progressHUD= KProgressHUD.create(GoodOrderActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("订单提交中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {

            for (GoodLookAdapter.CommitDataBean dataBean:commitDataBeanList){
                try {
                    WSConnector.getInstance().createGoodsOrder(dataBean.getData()
                            ,dataBean.getShopId()
                            ,dataBean.getTotalPrice(),address,name,phone);
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            boolean orderIsOk=false;
            progressHUD.dismiss();
            if(s==null){
                orderIsOk=true;

                Iterator<CartManager.MyCartClass> iterator=myCartClassList.iterator();
                while (iterator.hasNext()){
                    CartManager.MyCartClass myCartClass=iterator.next();
                    if(myCartClass.isCheckYN()){
                        iterator.remove();
                    }
                }

                CartManager.getInstance().cacheMyCartClassToDisk(GoodOrderActivity.this);
            }else{
                Toast.makeText(GoodOrderActivity.this,s,Toast.LENGTH_SHORT).show();
            }
            Intent intent=new Intent(GoodOrderActivity.this,OrderResultActivity.class);

            intent.putExtra(Constants.AC_TYPE,Constants.AC_TYPE_GOOD);
            intent.putExtra(Constants.ORDER_RESULT_IS_OK,orderIsOk);
            intent.putExtra("Title","");
            startActivity(intent);
            finish();
        }
    }

    protected boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home2);

        Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);

        TextView titleTxt=(TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        titleTxt.setText(getIntent().getStringExtra("Title"));

        leftBtn.setText("返回");
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightBtn.setText("我的地址");
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddress();
            }
        });





        return true;
    }
    private void toAddress(){
        Intent intent = new Intent(GoodOrderActivity.this, MyAddressActivity.class);
        intent.putExtra("Title", "我的地址");
        startActivity(intent);
    }
}
