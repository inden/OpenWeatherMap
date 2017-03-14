package com.in.den.android.openweather;


import com.in.den.android.openweather.db.DbModule;
import com.in.den.android.openweather.net.NetModule;
import com.in.den.android.openweather.ui.WeatherLocationFragment;
import com.in.den.android.openweather.ui.LocationsActivity;
import com.in.den.android.openweather.ui.LocationsArrayAdapter;
import com.in.den.android.openweather.ui.MainActivity;
import com.in.den.android.openweather.ui.SplashActivity;
import com.in.den.android.openweather.ui.UIModule;
import com.in.den.android.openweather.ui.WeatherLocationRVAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by harumi on 03/03/2017.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class, DbModule.class, UIModule.class})

public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(LocationsActivity mainActivity);

    void inject(LocationsArrayAdapter locationsArrayAdapter);

    void inject(OWService owService);

    void inject(SplashActivity splashActivity);

    void inject(WeatherLocationFragment itemFragment);



};

