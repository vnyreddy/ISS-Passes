package com.vinay.wizdem.spacestationfinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vinay.wizdem.spacestationfinder.IssLocationService;
import com.vinay.wizdem.spacestationfinder.MessageEvent;
import com.vinay.wizdem.spacestationfinder.R;
import com.vinay.wizdem.spacestationfinder.ReceiverEvent;
import com.vinay.wizdem.spacestationfinder.Utils;
import com.vinay.wizdem.spacestationfinder.model.iss_position.IssPosition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.internal.Util;

/**
 * Created by vinay_1 on 1/7/2018.
 * MapViewFragment is responsible for show map, display iss location marker
 * A Service is used to call the api for evey 5 seconds to update location
 * Used EventBus to communicate between Service and Fragment
 * <p>
 * **It can improve by obtaining network status and use broadcast receive to notify the user in case of network issue
 */

public class MapViewFragment extends MapFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private LatLng IssLocation;
    private Marker marker;
    private Intent intent;

    static boolean isFirst = true;

    public MapViewFragment() {
        getMapAsync(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        intent = new Intent(getActivity(), IssLocationService.class);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

    }

    //EventBus subscribe an event that published by service
    @Subscribe
    public void onMessageEvent(MessageEvent model) {
        IssLocation = new LatLng(model.issPosition.getLatitude(), model.issPosition.getLongitude());
        //Creating Marker first time, later use the marker obj and change its position with latest coordinates
        if (isFirst) {
            if (map != null) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_iss_1);
                MarkerOptions markerOptions = new MarkerOptions().position(IssLocation);
                markerOptions.title("ISS Current Location");
                markerOptions.icon(icon);
                try {
                    marker = map.addMarker(markerOptions);
                } catch (Exception e) {
                    Log.e("MARKER", String.valueOf(e));
                }
                map.moveCamera(CameraUpdateFactory.newLatLng(IssLocation));
                isFirst = false;
            }
        } else {
            marker.setPosition(IssLocation);
            map.moveCamera(CameraUpdateFactory.newLatLng(IssLocation));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
            startServiceProcedure();
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        stopServiceProcedure();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiverEvent(ReceiverEvent event) {
        if(event.flag == Utils.SUCESS){
            startServiceProcedure();
        }else {
            stopServiceProcedure();
        }
    }

    private void startServiceProcedure(){
        if(Utils.isNetworkConnected(this.getContext())){
            if(!Utils.isServiceRunnig(IssLocationService.class)){
                getActivity().startService(intent);
            }
        }
    }

    private void stopServiceProcedure(){
        if(Utils.isServiceRunnig(IssLocationService.class)){
            getActivity().stopService(intent);
        }
    }

}
