package com.carbeauty.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.carbeauty.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AcCarInfoDialog extends Activity {
    EditText editText;
    Button okBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carinfo_dialog);
        setTitle("添加车牌号");

        editText= (EditText) findViewById(R.id.editText);
        okBtn= (Button) findViewById(R.id.okBtn);
        cancelBtn= (Button) findViewById(R.id.cancelBtn);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().putExtra("name",editText.getText().toString());
                setResult(1,getIntent());
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });

    }
}
