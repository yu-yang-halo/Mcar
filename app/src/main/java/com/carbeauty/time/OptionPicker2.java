package com.carbeauty.time;

import android.app.Activity;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Administrator on 2016/3/21.
 */
public class OptionPicker2 extends OptionPicker {


    /**
     * Instantiates a new Option picker.
     *
     * @param activity the activity
     * @param options  the options
     */
    public OptionPicker2(Activity activity, String[] options) {
        super(activity, options);
    }

    @Override
    protected void onCancel() {
        if(onMyCancelListener!=null){
            onMyCancelListener.onMyCancel();
        }
    }

    private OnMyCancelListener onMyCancelListener;

    public OnMyCancelListener getOnMyCancelListener() {
        return onMyCancelListener;
    }

    public void setOnMyCancelListener(OnMyCancelListener onMyCancelListener) {
        this.onMyCancelListener = onMyCancelListener;
    }

    /**
     * The interface On option pick listener.
     */
    public interface OnMyCancelListener {


        void onMyCancel();

    }




}
