package com.vinay.wizdem.spacestationfinder.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vinay_1 on 1/6/2018.
 * Used Retrofit API Client
 *
 */

public class ApiClient {
    private static final String BASE_URL = "http://api.open-notify.org/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
