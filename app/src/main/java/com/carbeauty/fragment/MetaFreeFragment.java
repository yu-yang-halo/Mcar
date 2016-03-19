package com.carbeauty.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.carbeauty.ImageUtils;
import com.carbeauty.R;

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fr_metafree, null);
        btn0= (Button) v.findViewById(R.id.button3);
        btn1= (Button) v.findViewById(R.id.button4);

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


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                bitmaps.add(ImageUtils.convertToBitmap(path, 400,400));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           if(bitmaps!=null&&bitmaps.size()>0){
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


}
