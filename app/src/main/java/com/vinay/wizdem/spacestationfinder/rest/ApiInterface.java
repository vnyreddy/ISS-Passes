package com.vinay.wizdem.spacestationfinder.rest;

import com.vinay.wizdem.spacestationfinder.model.flyby.FlyBy;
import com.vinay.wizdem.spacestationfinder.model.iss_position.Iss;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vinay_1 on 1/6/2018.
 */

public interface ApiInterface {

    @GET("iss-pass.json")
    Call<FlyBy> getFlyByList(@Query("lat") double latitude, @Query("lon") double longitude);

    @GET("iss-now.json")
    Call<Iss> getIssLocation();
}
