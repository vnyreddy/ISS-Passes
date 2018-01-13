package com.vinay.wizdem.spacestationfinder;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vinay.wizdem.spacestationfinder.model.iss_position.Iss;
import com.vinay.wizdem.spacestationfinder.model.iss_position.IssPosition;
import com.vinay.wizdem.spacestationfinder.rest.ApiClient;
import com.vinay.wizdem.spacestationfinder.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vinay_1 on 1/7/2018.
 * Service to call the APi for every 5 seconds to obtain the ISS current position
 * used work thread to do in background
 *
 */

public class IssLocationService extends Service {

    private IssPosition model;
    private Thread t;
    private volatile boolean isThreadRunning = false;

    public IssLocationService(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!isThreadRunning){
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isThreadRunning){
                        requestIssLocation();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            });
            isThreadRunning = true;
            t.start();
        }
        return START_STICKY;
    }

    //Retrofit service call
    private void requestIssLocation() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Iss> call = apiInterface.getIssLocation();
        call.enqueue(new Callback<Iss>() {
            @Override
            public void onResponse(Call<Iss> call, Response<Iss> response) {
                if(response != null){
                    model = response.body().getIssPosition();
                    //EventBus publishing event with IssPosition obj
                    EventBus.getDefault().post(new MessageEvent(model));
                }
            }

            @Override
            public void onFailure(Call<Iss> call, Throwable t) {
                Log.i("ISS_NOW", "Unsuccessful Request.");
            }
        });
    }


    @Override
    public void onDestroy() {
        isThreadRunning = false;
        stopSelf();
        super.onDestroy();
    }
}
