package com.carbeauty.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.carbeauty.R;
import com.pay.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/28.
 */

public class NumberSelectorDialog extends Activity {
    Button btn01,btn02,btn03,btn04,btn05,btn06,btn07,okBtn;
    Button selectButton;
    GridView gridView;
    NumberAdapter  adapter;
    public static int resultCode=1000;
    public static String  KEY_RESULT="key_result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_number_selector);

        btn01= (Button) findViewById(R.id.btn01);
        btn02= (Button) findViewById(R.id.btn02);
        btn03= (Button) findViewById(R.id.btn03);
        btn04= (Button) findViewById(R.id.btn04);
        btn05= (Button) findViewById(R.id.btn05);
        btn06= (Button) findViewById(R.id.btn06);
        btn07= (Button) findViewById(R.id.btn07);

        gridView= (GridView) findViewById(R.id.gridView);
        okBtn= (Button) findViewById(R.id.okBtn);

        ClickListenser listenser=new ClickListenser();
        btn01.setOnClickListener(listenser);
        btn01.setText(NumberUtils.provinces[12]);
        btn02.setSelected(true);
        selectButton=btn02;

        btn02.setOnClickListener(listenser);
        btn03.setOnClickListener(listenser);
        btn04.setOnClickListener(listenser);
        btn05.setOnClickListener(listenser);
        btn06.setOnClickListener(listenser);
        btn07.setOnClickListener(listenser);



        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numberCar=collectorData();
                if(isEmptyOrNull(numberCar)){
                    Toast.makeText(NumberSelectorDialog.this,"请填写完整的车牌号",Toast.LENGTH_SHORT).show();
                }else{
                    getIntent().putExtra(KEY_RESULT,numberCar);

                    setResult(resultCode,getIntent());
                    finish();
                }

            }
        });

        adapter=new NumberAdapter(this, NumberUtils.provinces);
        initCharsData(true);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String charStr=adapter.getLists()[position];
                Log.v("setOnItemClick","charStr:::"+charStr);
                if(selectButton!=null){
                    if(selectButton.getId()==R.id.btn02){
                        String pattern = "^[0-9]$";
                        // 创建 Pattern 对象
                        Pattern r = Pattern.compile(pattern);
                        // 现在创建 matcher 对象
                        Matcher m = r.matcher(charStr);
                        if (!m.matches()){
                            selectButton.setText(charStr);
                        }else{
                            Toast.makeText(NumberSelectorDialog.this,"请选择[A-Z]字符",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else{
                        selectButton.setText(charStr);
                    }
                }

                nextButton();
            }
        });


    }


    private boolean isEmptyOrNull(String val){
        if(val==null||val.equals("")){
            return true;
        }
        return false;
    }

    private String collectorData(){
        if(isEmptyOrNull(btn01.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn02.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn03.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn04.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn05.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn06.getText().toString())){
            return null;
        }
        if(isEmptyOrNull(btn07.getText().toString())){
            return null;
        }


        return btn01.getText().toString()+btn02.getText().toString()+" "+btn03.getText().toString()+
                btn04.getText().toString()+btn05.getText().toString()+btn06.getText().toString()+
                btn07.getText().toString();

    }
    private void initProvicesData(){
        adapter.setLists(NumberUtils.provinces);
        gridView.setNumColumns(6);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        setGridViewHeight();
    }
    private void initCharsData(boolean disableNumber){
        adapter.setLists(NumberUtils.chars);
        adapter.setDisableNumber(disableNumber);
        gridView.setNumColumns(6);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        setGridViewHeight();


    }
    private void setGridViewHeight(){
        if(gridView==null){
            return;
        }

        if(adapter==null){
            return;
        }

        int totalHeight = 0;
        int row=adapter.getCount()%6==0?adapter.getCount()/6:adapter.getCount()/6+1;

        for (int i = 0; i < row; i++) {
            View listItem = adapter.getView(i, null, gridView);
            if(listItem==null){
                return;
            }
            listItem.measure(0, 0);
            Log.v("height","h:"+listItem.getMeasuredHeight());

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;

        gridView.setLayoutParams(params);
    }

    private void radioSelector(View v){

        btn01.setSelected(false);
        btn02.setSelected(false);
        btn03.setSelected(false);
        btn04.setSelected(false);
        btn05.setSelected(false);
        btn06.setSelected(false);
        btn07.setSelected(false);

        if(v.isSelected()){
            v.setSelected(false);
        }else{
            v.setSelected(true);
            selectButton= (Button) v;
        }

    }
    private void nextButton(){
        if(selectButton.getId()==R.id.btn01){
            execClickHandler(btn02);
        }else if(selectButton.getId()==R.id.btn02){
            execClickHandler(btn03);
        }else if(selectButton.getId()==R.id.btn03){
            execClickHandler(btn04);
        }else if(selectButton.getId()==R.id.btn04){
            execClickHandler(btn05);
        }else if(selectButton.getId()==R.id.btn05){
            execClickHandler(btn06);
        }else if(selectButton.getId()==R.id.btn06){
            execClickHandler(btn07);
        }else if(selectButton.getId()==R.id.btn07){
            execClickHandler(btn01);
        }

    }

    private void  execClickHandler(View v){
        radioSelector(v);

        switch (v.getId()){
            case R.id.btn01:
                initProvicesData();
                break;
            case R.id.btn02:
                initCharsData(true);
                break;
            case R.id.btn03:
            case R.id.btn04:
            case R.id.btn05:
            case R.id.btn06:
            case R.id.btn07:
                initCharsData(false);
                break;
        }
    }

    class  ClickListenser implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            execClickHandler(v);

        }
    }
}
