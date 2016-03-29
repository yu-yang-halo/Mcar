package com.carbeauty.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;
import com.carbeauty.alertDialog.DialogManagerUtils;

import java.util.List;

import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.CarInfo;

/**
 * Created by Administrator on 2016/3/9.
 */
public class CarInfoAdapter extends BaseAdapter {
    List<CarInfo> carInfos;
    Context ctx;
    public CarInfoAdapter(List<CarInfo> carInfos,Context ctx){
        this.carInfos=carInfos;
        this.ctx=ctx;
    }

    public void setCarInfos(List<CarInfo> carInfos) {
        this.carInfos = carInfos;
    }

    @Override
    public int getCount() {
        if(carInfos!=null){
            return carInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (carInfos==null){
            return null;
        }
        return carInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.item2,null);
        }
        TextView titleView= (TextView) convertView.findViewById(R.id.title);
        Button delCarBtn= (Button) convertView.findViewById(R.id.delCarBtn);
        titleView.setText(carInfos.get(position).getNumber());

        delCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManagerUtils.showMessageAndCancel(ctx, "提示", "是否删除该车牌号?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DelCarTask(carInfos.get(position).getId(),position).execute();
                    }
                });
            }
        });

        return convertView;
    }
    class DelCarTask extends AsyncTask<String,String,String>{
        int id;
        int pos;
        public DelCarTask(int id,int pos){
            this.id=id;
            this.pos=pos;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                WSConnector.getInstance().delCar(id);
            } catch (WSException e) {
                return e.getErrorMsg();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s==null){
                carInfos.remove(pos);
                notifyDataSetChanged();
            }else{
                Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
