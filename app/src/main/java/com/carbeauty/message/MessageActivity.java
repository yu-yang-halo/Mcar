package com.carbeauty.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.carbeauty.R;
import com.carbeauty.order.HeaderActivity;

/**
 * Created by Administrator on 2016/4/15.
 */
public class MessageActivity extends HeaderActivity{
    TextView messageTxt;
    public static final String MESSAGE_CONTENT="message_content";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_message);

        initCustomActionBar();
        tvTitle.setText("通知消息");
        rightBtn.setVisibility(View.GONE);

        String message=getIntent().getStringExtra(MESSAGE_CONTENT);



        messageTxt= (TextView) findViewById(R.id.messageTxt);
        messageTxt.setText(message);


    }

}
