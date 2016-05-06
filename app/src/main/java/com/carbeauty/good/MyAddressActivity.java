package com.carbeauty.good;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.HeaderActivity;

/**
 * Created by Administrator on 2016/5/6.
 */
public class MyAddressActivity extends HeaderActivity {
    EditText nametxt;
    EditText phoneTxt;
    EditText addressTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_myaddress);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentBox.loadString(MyAddressActivity.this,"GOA_NAME",nametxt.getText().toString());
                ContentBox.loadString(MyAddressActivity.this,"GOA_PHONE",phoneTxt.getText().toString());
                ContentBox.loadString(MyAddressActivity.this,"GOA_ADDRESS",addressTxt.getText().toString());
                finish();
            }
        });

        nametxt= (EditText) findViewById(R.id.nametxt);
        phoneTxt= (EditText) findViewById(R.id.phoneTxt);
        addressTxt= (EditText) findViewById(R.id.addressTxt);


        nametxt.setText(ContentBox.getValueString(this, "GOA_NAME", ""));
        phoneTxt.setText(ContentBox.getValueString(this, "GOA_PHONE", ""));
        addressTxt.setText(ContentBox.getValueString(this, "GOA_ADDRESS", ""));


    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
