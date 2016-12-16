package com.carbeauty.auto;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carbeauty.BaseActivity;
import com.carbeauty.R;

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Administrator on 2016/12/16.
 */

public class PayActivity extends BaseActivity {
    NumberPicker picker;
    TextView timeLabel;
    TextView couponLabel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pay);
        initCustomActionBar();
        titleLabel.setText("自助洗服务");
        RelativeLayout relayout= (RelativeLayout) findViewById(R.id.relayout);

        timeLabel= (TextView) findViewById(R.id.textView53);
        couponLabel= (TextView) findViewById(R.id.textView52);

        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerNumber();
            }
        });




    }
    public void showPickerNumber() {
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(1, 20, 1);//数字范围
        picker.setSelectedItem(10);
        picker.setLabel("分钟");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                timeLabel.setText(option+"分钟");
            }

        });
        picker.show();
    }

}
