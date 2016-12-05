package com.carbeauty.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.quicksidebardemo.CarSelectorActivity;
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

        editText.setKeyListener(null);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent it=new Intent(AcCarInfoDialog.this,NumberSelectorDialog.class);
                    startActivityForResult(it,0);
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(AcCarInfoDialog.this,NumberSelectorDialog.class);
                startActivityForResult(it,0);
            }
        });

        editText2.setKeyListener(null);
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent it=new Intent(AcCarInfoDialog.this,CarSelectorActivity.class);
                    startActivityForResult(it,1);
                }
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(AcCarInfoDialog.this,CarSelectorActivity.class);
                startActivityForResult(it,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==NumberSelectorDialog.resultCode){
            String result=data.getStringExtra(NumberSelectorDialog.KEY_RESULT);
            Log.v("numberCar","result "+result);
            editText.setText(result);
        }else if(resultCode==CarSelectorActivity.RESULT_CODE){
            String result=data.getStringExtra(CarSelectorActivity.KEY_RESULT);
            Log.v("CarSelectorActivity","result "+result);
            editText2.setText(result);
        }
    }
}
