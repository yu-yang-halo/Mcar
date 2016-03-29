package com.carbeauty.good;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.adapter.GoodLookAdapter;
import com.carbeauty.cache.CartManager;
import com.carbeauty.order.HeaderActivity;

import java.util.List;

import cn.service.WSConnector;

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
                Toast.makeText(GoodOrderActivity.this,goodLookAdapter.getCommitData().toString(),
                        Toast.LENGTH_SHORT).show();

                //WSConnector.getInstance().createGoodsOrder()
            }
        });


    }

    class O
}
