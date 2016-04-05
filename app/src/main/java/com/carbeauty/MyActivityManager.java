package com.carbeauty;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/3/28.
 */

public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private boolean runBackground=true;


    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    public boolean isRunBackground() {
        return runBackground;
    }

    public void setRunBackground(boolean runBackground) {
        this.runBackground = runBackground;
    }
}