package com.carbeauty.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/6/6.
 */
public class WashOilDetailActivity extends HeaderActivity{
    ListView imageListView;
    List<String> imagePaths=new ArrayList<String>();
    Button btnAddItem;
    int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_washoil_detail);
        initCustomActionBar();
        rightBtn.setVisibility(View.GONE);
        String src=getIntent().getStringExtra("src");
        int id=getIntent().getIntExtra("id", -1);
        String desc=getIntent().getStringExtra("desc");
        requestCode=getIntent().getIntExtra("requestCode",-1);

        if(requestCode==WashOilActivity.REQUEST_CODE_DECO){
            for (String imageName:src.split(",")){
                imagePaths.add(WSConnector.getDecoItemURL(id, imageName));
            }

        }else if(requestCode==WashOilActivity.REQUEST_CODE_OIL){
            for (String imageName:src.split(",")){
                imagePaths.add(WSConnector.getOilItemURL(id, imageName));
            }

        }


        imageListView= (ListView) findViewById(R.id.imageListView);
        btnAddItem= (Button) findViewById(R.id.btnAddItem);
        TextView descTxt= (TextView) findViewById(R.id.textView6);
        descTxt.setText(desc);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(requestCode,getIntent());
                finish();
            }
        });

        ImageAdapter imageAdapter=new ImageAdapter(this,imagePaths);

        imageListView.setAdapter(imageAdapter);

    }
}
