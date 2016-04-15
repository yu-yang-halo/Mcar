package com.carbeauty.alertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2016/3/28.
 */
public class DialogManagerUtils {
    private static final int LONG_TIME=3000;
    public static void showMessage(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", listener)
                        .show();

    }
    public static void showMessageAndCancel(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .show();

    }
    public static void showMessageAndCancel2(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("查看", listener)
                .setNegativeButton("取消", null)
                .show();

    }

    public static void showMyToast(Context ctx,String message) {
        final Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_alert, null);
        TextView messageTxt= (TextView) view.findViewById(R.id.messageTxt);

        messageTxt.setText(message);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);


        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, LONG_TIME);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, LONG_TIME * 1);//30s 半分钟



    }


    public static void showToast(Context ctx,String message){
        Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_alert,null);
        TextView messageTxt= (TextView) view.findViewById(R.id.messageTxt);
        messageTxt.setText(message);
        toast.setView(view);
        toast.show();
    }
}
