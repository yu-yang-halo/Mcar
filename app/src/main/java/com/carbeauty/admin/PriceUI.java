package com.carbeauty.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.carbeauty.BaseActivity;
import com.carbeauty.R;
import com.carbeauty.time.TimeUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.service.WSConnector;
import cn.service.WSException;

/**
 * Created by Administrator on 2017/10/7 0007.
 */

public class PriceUI extends BaseActivity implements View.OnClickListener{

    private String year;
    private String month;
    private String day;
    private TextView lb_startshow,lb_endshow,lb_price;
    private Button btn_start,btn_end,queryBtn;

    private int type=0;//0 开始时间  1 结束时间
    private String startTime,serverStartTime;
    private String endTime,serverEndTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_price);
        initCustomActionBar();
        lb_startshow= (TextView) findViewById(R.id.lb_start_show);
        lb_endshow= (TextView) findViewById(R.id.lb_end_show);
        lb_price= (TextView) findViewById(R.id.lb_price);

        btn_start= (Button) findViewById(R.id.btn_start);
        btn_end= (Button) findViewById(R.id.btn_end);
        queryBtn= (Button) findViewById(R.id.queryBtn);

        String title=getIntent().getStringExtra("Title");
        titleLabel.setText(title);


        btn_start.setOnClickListener(this);
        btn_end.setOnClickListener(this);
        queryBtn.setOnClickListener(this);

    }



    private  void selectReservationTime(){
        int[] thisYearMonthDay= TimeUtils.getThisYearMonthDay();

        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setRange(thisYearMonthDay[0], thisYearMonthDay[0]+1);//年份范围

        if (type==0){
            picker.setTitleText("设置起始时间");
        }else{
            picker.setTitleText("设置结束时间");
        }
        picker.setSelectedItem(thisYearMonthDay[0], thisYearMonthDay[1], thisYearMonthDay[2]);

        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(final String year1, final String month1, final String day1) {
                year=year1;
                month=month1;
                day=day1;
                selectHourMinTime();
            }
        });
        picker.show();

    }

    private void selectHourMinTime(){
        String title="开始时间";
        if(type==1){
            title="结束时间";
        }

        int[] hourMinutes=TimeUtils.getThisHourMinute();
        //默认选中当前时间
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_OF_DAY);
        picker.setTitleText(title);

        if(hourMinutes[1]>=30){
            picker.setSelectedItem(hourMinutes[0]+1,0);
        }else{
            picker.setSelectedItem(hourMinutes[0],30);
        }


        picker.setTopLineVisible(true);

        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {



                if(type==0){

                    startTime = TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd HH:mm:ss");
                    serverStartTime=TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd+HH+mm+ss");
                    if(TimeUtils.compareToAMoreThanB(startTime,endTime)){
                        showToast("开始时间不得大于结束时间");
                        startTime=null;
                        lb_startshow.setText("");

                    }else{
                        lb_startshow.setText(startTime);
                    }

                }else{
                    endTime = TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd HH:mm:ss");
                    serverEndTime=TimeUtils.createDateFormat(year, month, day, hour, minute, "yyyy-MM-dd+HH+mm+ss");
                    if(TimeUtils.compareToAMoreThanB(startTime,endTime)){
                        showToast("结束时间不得小于开始时间");
                        endTime=null;
                        lb_endshow.setText("");

                    }else{
                        lb_endshow.setText(endTime);
                    }
                }



            }
        });

        picker.show();
    }
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void cancelTime(){
        if(type==0){
            lb_startshow.setText("");
            startTime=null;
        }else{
            lb_endshow.setText("");
            endTime=null;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                type=0;
                selectReservationTime();
                break;
            case R.id.btn_end:
                type=1;
                selectReservationTime();
                break;
            case R.id.queryBtn:
                if(startTime!=null&&endTime!=null){
                    queryMoney();
                }else {
                    showToast("开始时间或者结束时间不能为空");
                }
                break;
        }
    }


    private void queryMoney(){

        final KProgressHUD progressHUD=KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("查询金额中...")
                .setAnimationSpeed(1)
                .setDimAmount(0.3f)
                .show();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                float price=-1;
                try {
                    price= WSConnector.getInstance().getTotalPriceByShopId(-1,serverStartTime,serverEndTime);
                } catch (WSException e) {
                    e.printStackTrace();
                }
                final float finalPrice = price;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        lb_price.setText("总金额(元)\n\n"+String.valueOf(finalPrice));
                        progressHUD.dismiss();
                    }
                });
            }
        });
    }
}
