package com.carbeauty.order;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;
import com.carbeauty.dialog.AcCarSelectDialog;

import java.util.List;

import cn.service.bean.CarInfo;

/**
 * Created by Administrator on 2016/3/22.
 */
public class HeaderActivity extends FragmentActivity {
   protected ActionBar mActionbar;
   protected TextView tvTitle;
   protected Button rightBtn;
   protected Button leftBtn;
    @Override
    protected void onStart() {
        super.onStart();
        this.initCarInfos();

    }

    protected void initCarInfos() {


        List<CarInfo> carInfos = IDataHandler.getInstance().getCarInfos();

        if (carInfos != null && carInfos.size() > 0) {
            CarInfo selCar = null;
            for (CarInfo carInfo : carInfos) {
                if (ContentBox.getValueInt(this, ContentBox.KEY_CAR_ID, -1) == carInfo.getId()) {
                    selCar = carInfo;
                }
            }
            if (selCar == null) {
                selCar = carInfos.get(0);
            }
            rightBtn.setText(selCar.getNumber());
            ContentBox.loadInt(this, ContentBox.KEY_CAR_ID, selCar.getId());
        }else{
            ContentBox.loadInt(this, ContentBox.KEY_CAR_ID, -1);
        }
        if (carInfos != null&&carInfos.size() > 1) {
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HeaderActivity.this, AcCarSelectDialog.class);
                    startActivityForResult(intent, 100);
                }
            });

        }
    }

    protected boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home1);
        tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        tvTitle.setText(getIntent().getStringExtra("Title"));

        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        return true;
    }
}
