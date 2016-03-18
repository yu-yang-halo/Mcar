package com.carbeauty.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.carbeauty.R;
import com.photoselector.ui.PhotoSelectorActivity;

/**
 * Created by Administrator on 2016/3/13.
 */
public class MetaFreeFragment extends Fragment {
    private  Button btn0;
    private  Button btn1;

    private static final int TAKE_PHOTO_WITH_DATA0=1002;
    private static final int TAKE_PHOTO_WITH_DATA1=1003;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.from(getActivity()).inflate(R.layout.fr_metafree, null);
        btn0= (Button) v.findViewById(R.id.button3);
        btn1= (Button) v.findViewById(R.id.button4);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX, 4);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,TAKE_PHOTO_WITH_DATA0);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PhotoSelectorActivity.class);
                startActivityForResult(intent,TAKE_PHOTO_WITH_DATA1);
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
