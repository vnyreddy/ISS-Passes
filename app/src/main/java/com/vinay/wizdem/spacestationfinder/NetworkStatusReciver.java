package com.vinay.wizdem.spacestationfinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by vinay_1 on 1/8/2018.
 */

public class NetworkStatusReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        // Check internet connection and accrding to state change the
        // text of activity by calling method
        if (networkInfo != null && networkInfo.isConnected()){
            EventBus.getDefault().post(new ReceiverEvent());
        }else {
            Toast.makeText(context,"NO Network, Please Check Connection",Toast.LENGTH_SHORT).show();
        }
    }
}
