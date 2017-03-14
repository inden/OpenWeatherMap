package com.in.den.android.openweather;

import android.app.Application;

import com.in.den.android.openweather.db.DbModule;
import com.in.den.android.openweather.net.NetModule;

/**
 * Created by harumi on 03/03/2017.
 */

public class OWApp extends Application {

    private AppComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initAppComponent();
    }


    private  void initAppComponent() {

        applicationComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .dbModule(new DbModule("ow-db"))
                .build();

    }

    public AppComponent getApplicationComponent() {
        return applicationComponent;
    }


}
