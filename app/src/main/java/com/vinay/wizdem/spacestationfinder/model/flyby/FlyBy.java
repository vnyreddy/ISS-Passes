package com.vinay.wizdem.spacestationfinder.model.flyby;

/**
 * Created by vinay_1 on 1/6/2018.
 * used JSON to POJO converter
 * for ISS_PASS_TIME http://api.open-notify.org/iss-pass.json?lat=LAT&lon=LON
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlyBy {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request")
    @Expose
    private Request request;
    @SerializedName("response")
    @Expose
    private List<IssResponse> response;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<IssResponse> getIssResponse() {
        return response;
    }

    public void setIssResponse(List<IssResponse> response) {
        this.response = response;
    }

}
