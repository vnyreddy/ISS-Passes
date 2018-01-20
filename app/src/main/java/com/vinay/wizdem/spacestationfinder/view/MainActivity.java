package com.vinay.wizdem.spacestationfinder.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
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
import com.vinay.wizdem.spacestationfinder.Utils;
import com.vinay.wizdem.spacestationfinder.R;
import com.vinay.wizdem.spacestationfinder.ReceiverEvent;
import com.vinay.wizdem.spacestationfinder.model.flyby.IssResponse;
import com.vinay.wizdem.spacestationfinder.presenter.MainPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.vinay.wizdem.spacestationfinder.Utils.view;

public class MainActivity extends AppCompatActivity implements MainView,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView mRecyclerView;
    private MainAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageButton button;
    private Resources res;

    private static final int PERMISSION_REQUEST_LOCATION = 10;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (View) findViewById(R.id.root);
        res = getResources();
        button = (ImageButton) findViewById(R.id.findIssFlyby);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.iss_flyby_list);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        presenter = new MainPresenter(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginIssPassShowProcedure();
            }
        });
        beginIssPassShowProcedure();
    }
    //begin process by checking permission and then accessing location
    private void beginIssPassShowProcedure() {
        //Checking network
        if (Utils.isNetworkConnected(this)) {
            requestCurrentLocation();
        } else {
            Utils.makeSnackbar(res.getString(R.string.no_internet));
        }
    }

    // ReceiverEvent originate form broadcast receiver which is listening to the network status
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiverEvent(ReceiverEvent event) {
        if (event.flag == Utils.SUCESS) {
            requestCurrentLocation();
        } else {
            Utils.makeSnackbar(res.getString(R.string.no_internet));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    requestCurrentLocation();

                } else {
                    //permission was denied
                    Utils.makeSnackbar(res.getString(R.string.loc_deny));
                }
                return;
            }
        }
    }

    // accessing current location
    private void requestCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission is not available, must be requested.
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            //requestLocationPermission();
            return;
        }
        mFusedLocationClient.getLastLocation().
                addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            //have to pass lat lon to api request through the presenter object
                            presenter.onFlybyRequest(location.getLatitude(), location.getLongitude());
                        } else {
                            Snackbar.make(view, res.getString(R.string.loc_try_again),
                                    Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Request current location again
                                    requestCurrentLocation();
                                }
                            }).show();
                        }
                    }
                });
    }

    // display the list of flyby timestamp and duration
    @Override
    public void displayList(List<IssResponse> responses) {
        if (responses != null) {
            //  button.setVisibility(View.GONE);
            adapter = new MainAdapter(responses);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }

    // in case of network failure, notifying user
    @Override
    public void onFailureRestRequest() {
        Toast.makeText(getApplicationContext(), res.getString(R.string.api_fail), Toast.LENGTH_SHORT).show();
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
