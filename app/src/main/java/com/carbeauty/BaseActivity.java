package com.carbeauty;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/16.
 */

public class BaseActivity extends FragmentActivity {
    protected ActionBar mActionbar;
    protected Button    rightBtn;
    protected Button    leftBtn;
    protected TextView  titleLabel;
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected ExecutorService executorService= Executors.newCachedThreadPool();




    protected boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.baseheader);

        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);

        leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);

        titleLabel=(TextView) mActionbar.getCustomView().findViewById(R.id.titleTxt);




        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        return true;
    }
    protected void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
