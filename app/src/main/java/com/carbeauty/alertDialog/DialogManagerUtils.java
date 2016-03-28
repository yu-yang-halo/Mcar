package com.carbeauty.alertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carbeauty.R;


/**
 * Created by Administrator on 2016/3/28.
 */
public class DialogManagerUtils {
    public static void showMessage(Context ctx,String title,String message,DialogInterface.OnClickListener listener){

        new  AlertDialog.Builder(ctx)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", listener)
                        .show();

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
