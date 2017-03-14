package com.in.den.android.openweather.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.DaoSession;
import com.in.den.android.openweather.db.Location;
import com.in.den.android.openweather.net.OpenWeatherService;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    @Inject
    DaoSession daoSession;
    @Inject
    OpenWeatherService openWeatherService;

    List<Location> locations;
    private static String MAINFRAGMENT = "mainfragment";
    private String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((OWApp) getApplication()).getApplicationComponent().inject(this);

        setSupportActionBar(toolbar);

        QueryBuilder<Location> queryBuilder = daoSession.getLocationDao().queryBuilder();
        locations = queryBuilder.list();

        Fragment currentFragment = null;
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            currentFragment = getSupportFragmentManager().
                    getFragment(savedInstanceState, MAINFRAGMENT);
        }
        else {
            WeatherLocationFragment fragment = WeatherLocationFragment.newInstance(2);
            //fragment.setListLocation(locations);
            currentFragment = fragment;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                currentFragment , MAINFRAGMENT)
                .commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        Fragment currentfragment = getSupportFragmentManager().findFragmentByTag(MAINFRAGMENT);
        getSupportFragmentManager().putFragment(outState, MAINFRAGMENT, currentfragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings_city :
                Intent intent = new Intent(this, LocationsActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*********************************************************
     * Override methods of OnListFramentInterfactionListner
     */
    @Override
    public void onListFragmentInteraction(Location item) {

        WeatherDetailFragment itemFragment2 = new WeatherDetailFragment();
        itemFragment2.setLocation(item);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, itemFragment2, MAINFRAGMENT)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void showMap(String name, Double coord_lat, Double coord_lon) {
        
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat", coord_lat);
        intent.putExtra("lng", coord_lon);
        intent.putExtra("title", name);

        startActivity(intent);
    }


}
