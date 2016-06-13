package com.carbeauty.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.carbeauty.R;

import java.util.List;

import cn.service.WSConnector;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ImageAdapter extends BaseAdapter {
    List<String> paths;
    Context ctx;
    RequestQueue mQueue;
    public ImageAdapter(Context ctx,List<String> paths){
        this.paths=paths;
        this.ctx=ctx;
        mQueue= Volley.newRequestQueue(ctx);
    }
    @Override
    public int getCount() {
        if(paths!=null){
            return paths.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(paths!=null){
            return paths.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView item_image;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.view_image,null);
            item_image= (ImageView) convertView.findViewById(R.id.item_image);
            int screenWidth = getScreenWidth(ctx);
            ViewGroup.LayoutParams lp = item_image.getLayoutParams();
            lp.width = screenWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            item_image.setLayoutParams(lp);

            item_image.setMaxWidth(screenWidth);
            item_image.setMaxHeight(screenWidth * 5);


            convertView.setTag(item_image);
        }

        item_image= (ImageView) convertView.getTag();





        ImageLoader imageLoader=new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(item_image
                , 0, 0);


        imageLoader.get(paths.get(position), listener);

        return convertView;
    }
    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        Point p=new Point();

        display.getSize(p);

        return p.x;
    }
    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        Point p=new Point();

        display.getSize(p);
        return p.y;
    }
}
