package com.carbeauty;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/29.
 */
public class CommonUtils {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public static boolean isNumeric2(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public static int[] getStringHeightWidth(TextView tv, String text){
        Rect bounds = new Rect();
        TextPaint paint=tv.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width();
        int height=bounds.height();
        Log.v("width height","w:"+width+" h:"+height);
        return new int[]{width,height};
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
