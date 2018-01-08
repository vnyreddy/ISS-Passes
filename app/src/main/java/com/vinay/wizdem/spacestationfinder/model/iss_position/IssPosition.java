package com.vinay.wizdem.spacestationfinder.model.iss_position;

/**
 * Created by vinay_1 on 1/7/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssPosition {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public float getLatitude() {
        return new Float(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return new Float(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
