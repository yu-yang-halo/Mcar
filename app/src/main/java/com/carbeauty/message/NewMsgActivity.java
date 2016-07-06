package com.carbeauty.message;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.cache.MessageManager;
import com.carbeauty.order.HeaderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 2016/7/6.
 */
public class NewMsgActivity extends HeaderActivity {
    ListView msglistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_new_message);

        initCustomActionBar();
        tvTitle.setText("新消息");
        rightBtn.setVisibility(View.GONE);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        msglistview= (ListView) findViewById(R.id.msglistview);

        Stack<MessageManager.JPMessage> jpMessageStack=MessageManager.getInstance().getMessageStack(this);



        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();

        for(int i=0;i<jpMessageStack.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            jpMessageStack.get(i).getMsgContent();
            map.put("text",  jpMessageStack.get(i).getMsgContent());
            data_list.add(map);
        }
        String [] from ={"text"};
        int [] to = {R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(this,
                data_list, R.layout.adapter_msg, from, to);
        msglistview.setAdapter(sim_adapter);
        msglistview.setDividerHeight(1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MessageManager.getInstance().emptyMessageStack(this);
    }
}
