package com.vinay.wizdem.spacestationfinder;

import android.view.MotionEvent;

import com.vinay.wizdem.spacestationfinder.model.iss_position.IssPosition;

/**
 * Created by vinay_1 on 1/7/2018.
 * EventBus model class for delegating event between publisher and subscriber
 */

public class MessageEvent {
    public IssPosition issPosition;

    public MessageEvent(IssPosition issPosition){
        this.issPosition = issPosition;
    }


}
