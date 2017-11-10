package com.carbeauty.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.R;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.ExpressInfo;

/**
 * Created by Administrator on 2017/11/4 0004.
 */

public class ExpressActivity extends HeaderActivity {
    private int orderId;
    private TextView expressNameLabel;
    private TextView expressNumLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_express);
        initCustomActionBar();
        tvTitle.setText("物流信息");

        expressNumLabel= (TextView) findViewById(R.id.expressNumLabel);
        expressNameLabel= (TextView) findViewById(R.id.expressNameLabel);
        orderId=getIntent().getIntExtra("orderId",-1);
        rightBtn.setVisibility(View.GONE);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ExpressInfo expressInfo = null;
                try {
                    expressInfo=WSConnector.getInstance().getExpressById(orderId);
                } catch (WSException e) {

                }


                final ExpressInfo finalExpressInfo = expressInfo;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(finalExpressInfo !=null
                                &&finalExpressInfo.getExpressName()!=null
                                &&finalExpressInfo.getExpressNum()!=null){
                            expressNameLabel.setText(finalExpressInfo.getExpressName());
                            expressNumLabel.setText(finalExpressInfo.getExpressNum());
                        }else{
                            expressNameLabel.setText("暂无物流名称信息");
                            expressNumLabel.setText("暂无物流编号信息");
                        }
                    }
                });
            }
        });
    }
}
