package com.carbeauty.good;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.carbeauty.adapter.GoodLookAdapter;
import com.carbeauty.cache.CartManager;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;
import com.carbeauty.order.OrderResultActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;

/**
 * Created by Administrator on 2016/3/29.
 */
public class GoodOrderActivity extends HeaderActivity {
    ListView goodslistview;
    EditText nametxt;
    EditText phoneTxt;
    EditText addressTxt;

    CheckBox checkAll;
    TextView totalPrice;
    Button createOrderButton;
    GoodLookAdapter goodLookAdapter;


    String address;
    String name;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_goodsorder);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        goodslistview= (ListView) findViewById(R.id.goodslistview);
        nametxt= (EditText) findViewById(R.id.nametxt);
        phoneTxt= (EditText) findViewById(R.id.phoneTxt);
        addressTxt= (EditText) findViewById(R.id.addressTxt);


        nametxt.setText(ContentBox.getValueString(GoodOrderActivity.this, "GOA_NAME", ""));
        phoneTxt.setText(ContentBox.getValueString(GoodOrderActivity.this, "GOA_PHONE", ""));
        addressTxt.setText(ContentBox.getValueString(GoodOrderActivity.this, "GOA_ADDRESS", ""));




        checkAll= (CheckBox) findViewById(R.id.checkBox3);
        totalPrice= (TextView) findViewById(R.id.textView13);

        createOrderButton= (Button) findViewById(R.id.button2);



        List<CartManager.MyCartClass> myCartClassList=CartManager.getInstance().getMyCartClassList();

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

                address=addressTxt.getText().toString();
                name=nametxt.getText().toString();
                phone=phoneTxt.getText().toString();

                if(goodLookAdapter.getCommitData().size()==0){
                    Toast.makeText(GoodOrderActivity.this,"请选择购买的商品",Toast.LENGTH_SHORT).show();
                }else if(name.trim().equals("")||address.trim().equals("")||phone.trim().equals("")){
                    Toast.makeText(GoodOrderActivity.this,"地址/姓名/手机号不能为空",Toast.LENGTH_SHORT).show();
                }else{

                    ContentBox.loadString(GoodOrderActivity.this,"GOA_NAME",name);
                    ContentBox.loadString(GoodOrderActivity.this,"GOA_PHONE",phone);
                    ContentBox.loadString(GoodOrderActivity.this,"GOA_ADDRESS",address);

                    new CommitOrderTask(goodLookAdapter.getCommitData()).execute();
                }


            }
        });


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
}
