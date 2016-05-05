package com.carbeauty.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.carbeauty.ImageUtils;
import com.carbeauty.R;
import com.carbeauty.cache.ContentBox;
import com.carbeauty.order.OrderResultActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.carbeauty.Constants;
import cn.service.WSConnector;
import cn.service.WSException;
import cn.service.bean.MetaOrderInfo;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Administrator on 2016/3/13.
 */
public class MetaFreeFragment extends Fragment {
    private  Button btn0;
    private  Button btn1;

    private static final int TAKE_PHOTO_WITH_DATA0=1002;
    private static final int TAKE_PHOTO_WITH_DATA1=1003;
    ImageView upimage0;
    ImageView upimage1;
    ImageView upimage2;
    ImageView upimage3;

    ImageView upimage00;
    ImageView upimage11;
    ImageView upimage22;
    ImageView upimage33;

    List<Bitmap> typeBitmaps0;
    List<Bitmap> typeBitmaps1;

    Button commitBtn;

    private int shopId;
    private int carId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fr_metafree, null);


        shopId= ContentBox.getValueInt(getActivity(),ContentBox.KEY_SHOP_ID, 0);
        carId=ContentBox.getValueInt(getActivity(), ContentBox.KEY_CAR_ID, 0);


        btn0= (Button) v.findViewById(R.id.button3);
        btn1= (Button) v.findViewById(R.id.button4);

        commitBtn= (Button) v.findViewById(R.id.button5);

        upimage0= (ImageView) v.findViewById(R.id.upimage0);
        upimage1= (ImageView) v.findViewById(R.id.upimage1);
        upimage2= (ImageView) v.findViewById(R.id.upimage2);
        upimage3= (ImageView) v.findViewById(R.id.upimage3);

        upimage00= (ImageView) v.findViewById(R.id.upimage00);
        upimage11= (ImageView) v.findViewById(R.id.upimage11);
        upimage22= (ImageView) v.findViewById(R.id.upimage22);
        upimage33= (ImageView) v.findViewById(R.id.upimage33);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                intent.setPhotoCount(4);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent, TAKE_PHOTO_WITH_DATA0);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                intent.setPhotoCount(9);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent, TAKE_PHOTO_WITH_DATA1);
            }
        });


        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeBitmaps0!=null){

                    new OrderCommitTask().execute();

                }else{
                    Toast.makeText(getActivity(),"请添加车辆受损照片",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO_WITH_DATA0) {
            if (data != null) {
                clearImageView(TAKE_PHOTO_WITH_DATA0);
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);

                new LoadOrUploadImage(photos,TAKE_PHOTO_WITH_DATA0).execute();

            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO_WITH_DATA1){
            if (data != null) {
                clearImageView(TAKE_PHOTO_WITH_DATA1);
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);

                new LoadOrUploadImage(photos,TAKE_PHOTO_WITH_DATA1).execute();

            }
        }

    }

    private void clearImageView(int requestCode){
        if(requestCode==TAKE_PHOTO_WITH_DATA0){
            upimage0.setBackgroundDrawable(null);
            upimage1.setBackgroundDrawable(null);
            upimage2.setBackgroundDrawable(null);
            upimage3.setBackgroundDrawable(null);
        }else if(requestCode==TAKE_PHOTO_WITH_DATA1){
            upimage00.setBackgroundDrawable(null);
            upimage11.setBackgroundDrawable(null);
            upimage22.setBackgroundDrawable(null);
            upimage33.setBackgroundDrawable(null);
        }

    }


    class LoadOrUploadImage extends AsyncTask<String,String,String>{
        ArrayList<String> photos;
        int requestCode;
        List<Bitmap> bitmaps=new ArrayList<Bitmap>();
        LoadOrUploadImage(ArrayList<String> photos,int requestCode){
            this.photos=photos;
            this.requestCode=requestCode;
        }

        @Override
        protected String doInBackground(String... params) {
            for (String path:photos){
                bitmaps.add(ImageUtils.convertToBitmap(path));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           if(bitmaps!=null&&bitmaps.size()>0){

               if(requestCode==TAKE_PHOTO_WITH_DATA0){
                   typeBitmaps0=bitmaps;
               }else if(requestCode==TAKE_PHOTO_WITH_DATA1){
                   typeBitmaps1=bitmaps;
               }


               for (int i=0;i<bitmaps.size();i++){

                   initImageView(i,new BitmapDrawable(getResources(),bitmaps.get(i)));
               }


           }
        }

        private void initImageView(int index,Drawable drawable){
            if(requestCode==TAKE_PHOTO_WITH_DATA0){
                  if(index==0){
                      upimage0.setBackgroundDrawable(drawable);
                  }else if(index==1){
                      upimage1.setBackgroundDrawable(drawable);
                  }else if(index==2){
                      upimage2.setBackgroundDrawable(drawable);
                  }else if(index==3){
                      upimage3.setBackgroundDrawable(drawable);
                  }
            }else if(requestCode==TAKE_PHOTO_WITH_DATA1){
                if(index==0){
                    upimage00.setBackgroundDrawable(drawable);
                }else if(index==1){
                    upimage11.setBackgroundDrawable(drawable);
                }else if(index==2){
                    upimage22.setBackgroundDrawable(drawable);
                }else if(index==3){
                    upimage33.setBackgroundDrawable(drawable);
                }
            }
        }



    }

    class OrderCommitTask extends AsyncTask<String,String,String>{
        KProgressHUD progressHUD;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carId=ContentBox.getValueInt(getActivity(), ContentBox.KEY_CAR_ID, 0);

            progressHUD= KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("预约中...")
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
                MetaOrderInfo metaOrderInfo=new MetaOrderInfo(0,Constants.TYPE_PAY_TOSHOP,
                        Constants.STATE_ORDER_UNFINISHED,Constants.PAY_STATE_UNFINISHED,0,
                        carId,shopId,0,0,0,null,null, null,
                        null,
                        null);
                try {
                    metaOrderInfo=WSConnector.getInstance().createMetaOrder(metaOrderInfo);
                    if(typeBitmaps0!=null&&typeBitmaps0.size()>0){

                        for (Bitmap bm:typeBitmaps0){
                            try {
                                WSConnector.getInstance().createMetaOrderImg(metaOrderInfo.getId(),ImageUtils.bitmaptoString(bm));
                            } catch (UnsupportedEncodingException e) {
                                return e.getMessage();
                            }

                        }

                    }
                    if(typeBitmaps1!=null&&typeBitmaps1.size()>0){

                        for (Bitmap bm:typeBitmaps1){
                            try {
                                WSConnector.getInstance().createMetaOrderImg(metaOrderInfo.getId(),ImageUtils.bitmaptoString(bm));
                            } catch (UnsupportedEncodingException e) {
                                return e.getMessage();
                            }

                        }

                    }





                } catch (WSException e) {
                    return e.getErrorMsg();
                }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            boolean orderIsOk=false;
            progressHUD.dismiss();
            if(s==null){
                orderIsOk=true;
                // Toast.makeText(OrderReokActivity.this,"订单提交成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }

            Intent intent=new Intent(getActivity(),OrderResultActivity.class);
            intent.putExtra(Constants.AC_TYPE,Constants.AC_TYPE_META2);
            intent.putExtra(Constants.ORDER_RESULT_IS_OK,orderIsOk);
            intent.putExtra("Title","");
            startActivity(intent);
            getActivity().finish();

        }
    }


}
