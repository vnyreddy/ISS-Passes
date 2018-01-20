package com.vinay.wizdem.spacestationfinder.presenter;

import android.util.Log;

import com.vinay.wizdem.spacestationfinder.model.flyby.FlyBy;
import com.vinay.wizdem.spacestationfinder.model.flyby.IssResponse;
import com.vinay.wizdem.spacestationfinder.rest.ApiClient;
import com.vinay.wizdem.spacestationfinder.rest.ApiInterface;
import com.vinay.wizdem.spacestationfinder.view.MainView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vinay_1 on 1/6/2018.
 * Every development process have defined certain rules and responsibilities to design each component in MVP,
 * here I choose not to use any context.
 *
 * Presenter Class for MainActivity
 * supports view decision making with the help of PresenterLifeCycle call backs
 * (didn't used in this instance, tying to convey the knowledge)
 * Communicate with model and call rest api for use current location flyby
 */

public class MainPresenter implements PresenterLifeCycle {

    private MainView view;
    private List<IssResponse> model;

    public MainPresenter(MainView view) {
        this.view = view;
        model = new ArrayList<>();
    }

    // requesting the current location flyby date and time by passing user current geo coordinates
    public void onFlybyRequest(double lat, double lon) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<FlyBy> call = apiInterface.getFlyByList(lat, lon);
        call.enqueue(new Callback<FlyBy>() {
            @Override
            public void onResponse(Call<FlyBy> call, Response<FlyBy> respons) {
                if (respons.code() == 400) {
                    view.onFailureRestRequest();
                } else {
                    model = respons.body().getIssResponse();

                    if (model != null) {
                        view.displayList(model);
                    } else view.onFailureRestRequest();
                }
            }

            @Override
            public void onFailure(Call<FlyBy> call, Throwable t) {
                view.onFailureRestRequest();
            }
        });
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
