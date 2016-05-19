package com.carbeauty.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carbeauty.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AcCarInfoDialog extends Activity {
    EditText editText;
    Button okBtn;
    Button cancelBtn;
    EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carinfo_dialog);
        setTitle("添加车牌");

        editText= (EditText) findViewById(R.id.editText);
        editText2= (EditText) findViewById(R.id.editText2);
        okBtn= (Button) findViewById(R.id.okBtn);
        cancelBtn= (Button) findViewById(R.id.cancelBtn);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equals("")){
                    Toast.makeText(AcCarInfoDialog.this, "车牌号不能为空", Toast.LENGTH_SHORT).show();
                }else if(editText2.getText().toString().trim().equals("")){
                    Toast.makeText(AcCarInfoDialog.this, "车型号不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    getIntent().putExtra("name",editText.getText().toString());
                    getIntent().putExtra("model",editText2.getText().toString());
                    setResult(1,getIntent());
                    finish();
                }

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
