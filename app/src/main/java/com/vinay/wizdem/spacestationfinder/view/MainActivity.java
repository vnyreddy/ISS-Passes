package com.vinay.wizdem.spacestationfinder.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vinay.wizdem.spacestationfinder.MessageEvent;
import com.vinay.wizdem.spacestationfinder.PermissionUtil;
import com.vinay.wizdem.spacestationfinder.R;
import com.vinay.wizdem.spacestationfinder.ReceiverEvent;
import com.vinay.wizdem.spacestationfinder.model.flyby.Response;
import com.vinay.wizdem.spacestationfinder.presenter.MainPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView mRecyclerView;
    private MainAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageButton button;

    private static final int PERMISSION_REQUEST_LOCATION = 0;

    MainPresenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (View) findViewById(R.id.root);
        button = (ImageButton)findViewById(R.id.findIssFlyby);
        mRecyclerView = (RecyclerView) findViewById(R.id.iss_flyby_list);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()){
                    PermissionUtil.hasNetworkPermission = true;
                    requestCurrentLocation();
                }else {
                    Snackbar.make(view,"Internet unavilable, Please check..",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiverEvent(ReceiverEvent event){
        requestCurrentLocation();
    }

    private void requestLocationPermission() {
        // permission has not been granted, must be requested here.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            Snackbar.make(view, "Location permission is required to access current location.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(view, "Permission is not avilable. Request locaiton permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            //Request for location permission
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionUtil.hasLocationPermission = true;
                //permission grated. Start access location service
                Snackbar.make(view, "Location Permission Granted, Accessing location.",
                        Snackbar.LENGTH_SHORT).show();
                requestCurrentLocation();
            } else {
                PermissionUtil.hasLocationPermission = false;
                //permission request was denied
                Snackbar.make(view, "Location permission was denied.",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    // Current location
    private void requestCurrentLocation() {
        // Check for location access permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            PermissionUtil.hasLocationPermission = true;
            //location permission already available, start accessing location service
            Snackbar.make(view, "Accessing Location..",
                    Snackbar.LENGTH_SHORT).show();
            mFusedLocationClient.getLastLocation().
                    addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                //have to pass lat lon to api request
                                presenter.onFlybyRequest(location.getLatitude(),location.getLongitude(),location.getAltitude());

                            }
                        }
                    });
        } else {
            //Permission is not available, must be requested.
            PermissionUtil.hasLocationPermission = false;
            requestLocationPermission();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    // display the list of flyby timestamp and duration
    @Override
    public void displayList(List<Response> responses) {
        if(responses != null){
            button.setVisibility(View.GONE);
            adapter = new MainAdapter(responses);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }
    // in case of network failure, notifying user
    @Override
    public void onFailureRestRequest() {
        Toast.makeText(getApplicationContext(),"API failure to get response",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
