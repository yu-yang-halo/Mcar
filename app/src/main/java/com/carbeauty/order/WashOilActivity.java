package com.carbeauty.order;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import cn.service.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.OilInfo;
import cn.service.bean.OrderStateType;

/**
 * Created by Administrator on 2016/3/10.
 */
public class WashOilActivity extends Activity {
    public final static String AC_TYPE="activity_task_type";
    public final static int AC_TYPE_WASH=1000;
    public final static int AC_TYPE_OIL=1001;
    private int ac_type_value=0;

    private ActionBar mActionbar;
    private TextView tvTitle;
    private int shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_washoil);
        ac_type_value=getIntent().getIntExtra(AC_TYPE,AC_TYPE_WASH);
        initCustomActionBar();

        shopId= ContentBox.getValueInt(this, "shopId", 0);

        GridView gridView2= (GridView) findViewById(R.id.gridView2);


        new GetDataList().execute();

    }


    class GetDataList extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd+HH+mm+ss");
            Date date=new Date();
            String time=sdf.format(date);
            try {
                List<OrderStateType> orderStateTypes=WSConnector.getInstance().getDayOrderStateList(Constants.SEARCH_TYPE_DECO,shopId,"2016-01-18+7+00+00");
                List<OilInfo> oilInfos=WSConnector.getInstance().getOilList(shopId);
            } catch (WSException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return null;
        }
    }




    private boolean initCustomActionBar() {
        mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.header_home1);
        tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.tv_tbb_title);
        tvTitle.setText(getIntent().getStringExtra("Title"));

        Button rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        Button leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        return true;
    }
}
