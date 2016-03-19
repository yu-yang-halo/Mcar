package com.carbeauty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/19.
 */
public class ImageUtils {
    private static final int IO_BUFFER_SIZE=1024;

    public static  Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = getScaleSize(opts,w,h);
        // 加载到内存
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {



        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }


    private static int getScaleSize(BitmapFactory.Options opts ,int w, int h){
        int width = opts.outWidth;
        int height = opts.outHeight;

        int inSampleSize = 1;
        if (height > h || width > w) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRate = Math.round((float) height / (float) h);
            final int widthRate = Math.round((float) width / (float) w);
            inSampleSize = heightRate < widthRate ? heightRate : widthRate;
        }

        return inSampleSize;
    }
    private  static Bitmap convertToBitmap(String path){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = 1;
        // 加载到内存
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }

}
