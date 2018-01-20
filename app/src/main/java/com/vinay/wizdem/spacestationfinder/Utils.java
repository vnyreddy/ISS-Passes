package com.vinay.wizdem.spacestationfinder;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by vinay_1 on 1/7/2018.
 * accessing permission status application wide
 */

public class Utils {
    public static final boolean SUCESS = true;
    public static final boolean FAILURE = false;
    public static View view;

    public static void makeSnackbar(String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isServiceRunnig(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) view.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
