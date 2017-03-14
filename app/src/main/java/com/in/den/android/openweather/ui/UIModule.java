package com.in.den.android.openweather.ui;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by harumi on 05/03/2017.
 */
@Module
public class UIModule {

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(Application application) {
        return (LayoutInflater) application.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }
}
