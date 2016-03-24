package com.carbeauty.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carbeauty.MainActivity;
import com.carbeauty.R;
import com.carbeauty.adapter.CommonAdapter;
import com.carbeauty.cache.IDataHandler;

import com.carbeauty.Constants;

/**
 * Created by Administrator on 2016/3/23.
 */
public class OrderResultActivity extends HeaderActivity{
    RelativeLayout resultLayout;
    TextView resultText;
    ImageView resultImageView;

    int ac_type_val;
    boolean order_is_ok;
    ListView resultListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_orderresult);
        initCustomActionBar();


        rightBtn.setVisibility(View.GONE);
        leftBtn.setVisibility(View.GONE);


        resultLayout= (RelativeLayout) findViewById(R.id.resultLayout);
        resultText= (TextView) findViewById(R.id.resultText);
        resultImageView= (ImageView) findViewById(R.id.resultImage);

        resultListView= (ListView) findViewById(R.id.resultListView);



        Button backHomeBtn= (Button) findViewById(R.id.backHomeBtn);
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        ac_type_val=this.getIntent().getIntExtra(Constants.AC_TYPE,0);
        order_is_ok=this.getIntent().getBooleanExtra(Constants.ORDER_RESULT_IS_OK, false);

        tvTitle.setText("预约详情");
        if(order_is_ok){
            resultText.setText("预约成功\n请等待客服确认");

            if(ac_type_val!=Constants.AC_TYPE_META2){
                CommonAdapter commonAdapter=new CommonAdapter(ac_type_val,this);

                if(ac_type_val==Constants.AC_TYPE_OIL){
                    commonAdapter.setOilInfos(IDataHandler.getInstance().getOilInfoSet());
                }else if(ac_type_val==Constants.AC_TYPE_WASH){
                    commonAdapter.setDecorationInfos(IDataHandler.getInstance().getDecorationInfoSet());
                }else if(ac_type_val==Constants.AC_TYPE_META){
                    commonAdapter.setMetalplateInfos(IDataHandler.getInstance().getMetalplateInfoSet());
                }

                resultListView.setAdapter(commonAdapter);
            }

        }else{
            resultLayout.setBackgroundColor(getResources().getColor(R.color.red));
            resultText.setText("预约失败");
            resultImageView.setVisibility(View.GONE);
        }



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(OrderResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
    }
}
