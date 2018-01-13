package com.vinay.wizdem.spacestationfinder.view;

import com.vinay.wizdem.spacestationfinder.model.flyby.IssResponse;

import java.util.List;

/**
 * Created by vinay_1 on 1/6/2018.
 * this View interface is to delegate message activity and presenter
 */

public interface MainView {
    void displayList(List<IssResponse> responses);
    void onFailureRestRequest();
}
