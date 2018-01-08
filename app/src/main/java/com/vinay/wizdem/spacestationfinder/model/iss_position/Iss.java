package com.vinay.wizdem.spacestationfinder.model.iss_position;

/**
 * Created by vinay_1 on 1/7/2018.
 * ISS Current Location http://api.open-notify.org/iss-now.json
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Iss {

    @SerializedName("iss_position")
    @Expose
    private IssPosition issPosition;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;

    public IssPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(IssPosition issPosition) {
        this.issPosition = issPosition;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

}