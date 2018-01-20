package com.vinay.wizdem.spacestationfinder.model.flyby;

/**
 * Created by vinay_1 on 1/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IssResponse {

    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("risetime")
    @Expose
    private Integer risetime;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getRisetime() {
        //converting Unix timestamp to readable date and time
        Date date = new Date();
        date.setTime((long)risetime*1000);
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss aaa z");
        String time = dateFormat.format(date);
        return time;
    }

    public void setRisetime(Integer risetime) {
        this.risetime = risetime;
    }

}
