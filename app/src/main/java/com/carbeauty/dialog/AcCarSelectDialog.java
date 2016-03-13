package com.carbeauty.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.cache.IDataHandler;

import java.util.List;

import cn.service.bean.CarInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class AcCarSelectDialog extends Activity {
    Spinner carspinner;
    Button okBtn;
    Button cancelBtn;
    int carId=-1;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carselect_dialog);
        setTitle("选择车牌号");

        carspinner= (Spinner) findViewById(R.id.spinner);
        okBtn= (Button) findViewById(R.id.okBtn);
        cancelBtn= (Button) findViewById(R.id.cancelBtn);
        final List<CarInfo> carInfos=IDataHandler.getInstance().getCarInfos();
        final  int LEN=carInfos.size();
        String[] mItems =new String[LEN];
        carId=ContentBox.getValueInt(this,ContentBox.KEY_CAR_ID,-1);

        int selpos=0;
        for (int i=0;i<carInfos.size();i++){
            mItems[i]=carInfos.get(i).getNumber();
            if (carId==carInfos.get(i).getId()){
                selpos=i;
            }
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carspinner.setAdapter(adapter);
        carspinner.setSelection(selpos);
        carspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                carId=carInfos.get(pos).getId();
                number=carInfos.get(pos).getNumber();



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().putExtra("number",number);

                setResult(carId, getIntent());
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(-2);
                finish();
            }
        });

    }
}
