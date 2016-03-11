package com.carbeauty.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carbeauty.R;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/3/9.
 */
public class IpConfigDialog extends Activity {
    EditText editText;
    Button okBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carinfo_dialog);
        setTitle("IP配置");

        editText= (EditText) findViewById(R.id.editText);
        editText.setHint("例如：192.168.1.1");
        okBtn= (Button) findViewById(R.id.okBtn);
        cancelBtn= (Button) findViewById(R.id.cancelBtn);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equals("")){
                    Toast.makeText(IpConfigDialog.this, "ip不能为空", Toast.LENGTH_SHORT).show();
                }else{
                     String ip=editText.getText().toString();
                     WSConnector.getInstance(ip,"9000",false);
                     Toast.makeText(IpConfigDialog.this, "初始化IP成功", Toast.LENGTH_SHORT).show();
                     finish();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
