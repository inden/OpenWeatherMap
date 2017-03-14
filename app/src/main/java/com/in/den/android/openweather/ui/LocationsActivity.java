package com.in.den.android.openweather.ui;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.util.LocationInfo;
import com.in.den.android.openweather.util.SharedPreferenceOp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationsActivity extends AppCompatActivity implements LocationsArrayAdapter.Callback {

    @BindView(R.id.edit_location)
    EditText editLocation;
    @BindView(R.id.btn_add_loc)
    ImageButton btnAddLoc;
    @BindView(R.id.list_locations)
    ListView listLocations;

    @Inject
    SharedPreferenceOp sharedPreferenceOp;

    private LocationsArrayAdapter locationsArrayAdapter;
    private List<LocationInfo> locationInfos;
    private Handler handler = new Handler();
    private static int MAXCITY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_setting);

        ButterKnife.bind(this);
        ((OWApp)getApplication()).getApplicationComponent().inject(this);

        locationInfos = new ArrayList<LocationInfo>();
        locationsArrayAdapter = new LocationsArrayAdapter(LocationsActivity.this,
                R.layout.activity_loc_setting, locationInfos, this);

        listLocations.setAdapter(locationsArrayAdapter);

        initLocations();
    }

    @OnClick(R.id.btn_add_loc)
    public void addNewLocation() {

        int size = sharedPreferenceOp.getPreferenceLocations().size();
        if(size >= MAXCITY) {
            //
            Toast.makeText(this, R.string.mxcityalert, Toast.LENGTH_LONG).show();

            return;
        }

        final String location = editLocation.getText().toString().trim();
        if(!location.isEmpty()) {
            getWindow().getDecorView().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    sharedPreferenceOp.addNewLocation(location);
                    updateLocation();
                }
            });
        }
        editLocation.getText().clear();
    }

    @OnClick(R.id.imagebtn_refresh)
    public void refreshData() {
        //add here dialog

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }


    //This method is called from the Location Adapter (callback)
    @Override
    public void deleteLocation(final String location) {

        getWindow().getDecorView().getHandler().post(new Runnable() {
            @Override
            public void run() {
                sharedPreferenceOp.removeLocation(location);
                updateLocation();
            }
        });
    }

    public void initLocations() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
    }

    public void updateLocation() {
        //important : not to do this
        //locationInfos = sharedPreferenceOp.getPreferenceLocations();
        locationInfos.clear();
        locationInfos.addAll(sharedPreferenceOp.getPreferenceLocations());

        locationsArrayAdapter.notifyDataSetChanged();
    }

}
