package com.carbeauty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/3/19.
 */
public class ImageUtils {
    private static final int IO_BUFFER_SIZE=1024;

    public static  Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = getScaleSize(opts,w,h);
        // 加载到内存
        opts.inJustDecodeBounds = false;

        Bitmap bm= BitmapFactory.decodeFile(path, opts);


        return bm;
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

        Log.v("inSampleSize", "inSampleSize is " + inSampleSize + " width:" + width + " height:" + height);

        return inSampleSize;
    }

    public static Bitmap loadImageFromUrl(String url) {
        URL m;
        InputStream i = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream out =null;
        byte[] data=null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
            bis = new BufferedInputStream(i,1024 * 8);
            out = new ByteArrayOutputStream();
            int len=0; byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            data = out.toByteArray();
            out.close();
            bis.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data==null){
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    private  static  Bitmap scaleNetImageToBitmap(String urlString,int w,int h){
        /**
         * 根据url下载图片在指定的文件
         *
         * @param urlStr
         * @param file
         * @return
         */
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap =null;

        try
        {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());


            BitmapFactory.Options opts = new BitmapFactory.Options();

            if(w!=0&&h!=0){
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, opts);
                opts.inSampleSize =getScaleSize(opts,w,h);
            }
            opts.inJustDecodeBounds = false;

            try{
                // this gives error for larger images taken by camera
                is.reset();
                Log.v("reset", "scale normal  ->"+urlString);
            } catch(IOException e){
                is.close();
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inSampleSize = 1;
                conn = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(conn.getInputStream());
                Log.v("reset", "scale special   "+opts.inSampleSize+" ->"+urlString);
            }
            bitmap = BitmapFactory.decodeStream(is, null, opts);

            Log.v("bitmap","init bitmap "+bitmap);

            conn.disconnect();
            return bitmap;

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
            }

            try
            {
                if (fos != null)
                    fos.close();
            } catch (IOException e)
            {
            }
        }

        return null;

    }

    public static  Bitmap convertNetToBitmap(String urlString,int w,int h){
        return scaleNetImageToBitmap(urlString,w,h);
    }
    public static  Bitmap convertNetToBitmap(String urlString){
        return scaleNetImageToBitmap(urlString, 0, 0);
    }
    public static byte[] imageToByteArray(String imgPath) {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(imgPath));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int size = 0;
            byte[] temp = new byte[1024];
            while((size = in.read(temp))!=-1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public   static Bitmap convertToBitmap(String path){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为true只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = 1;
        // 加载到内存
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }

    public   static Bitmap convertToBitmap(String path,int sampleSize){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = sampleSize;
        // 加载到内存
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }


    /**
     * 将base64转换成bitmap图片 　　
     *
     * @param string
     *            base64字符串 　　
     * @return bitmap
     **/
    public static Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static String bitmaptoString(Bitmap bm) {
        // 将Bitmap转换成字符串类型
        String base64ImageString=Base64.encodeToString(bitmap2Bytes(bm), Base64.DEFAULT);

        System.out.println("bm image base64 length=" + base64ImageString.length());
        return  Base64.encodeToString(bitmap2Bytes(bm),Base64.DEFAULT);
    }

    public static String bitmaptoString(String path) {
        byte[] imageBytes=imageToByteArray(path);
        // 将Bitmap转换成字符串类型
        String base64ImageString=Base64.encodeToString(imageBytes, Base64.DEFAULT);

        System.out.println("path image base64 length=" + base64ImageString.length());
        return  Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }


    public static byte[] bitmap2Bytes(Bitmap bm) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
         return baos.toByteArray();
    }



    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }
}
