package com.carbeauty.fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class BaseFragment extends Fragment {
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected ExecutorService executorService= Executors.newCachedThreadPool();
    protected KProgressHUD progressHUD = null;



}
