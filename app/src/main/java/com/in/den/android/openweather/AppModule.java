package com.in.den.android.openweather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.in.den.android.openweather.util.SharedPreferenceOp;
/**
 * Created by harumi on 03/03/2017.
 */
@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Properties provideProperties(Application application) {
        Properties prop = new Properties();
        try {
            prop.load(application.getAssets().open(application.getString(R.string.properties_file)));
        }
        catch(IOException ex) {
        }
        return prop;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences("owapp", application.getBaseContext().MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferenceOp provideSharedPreferencesOp(Application application, Gson gson,
                                                   SharedPreferences sharedPreferences) {
        return new SharedPreferenceOp(application, gson, sharedPreferences);
    }

    @Provides
    @Singleton
    Typeface provideTypeface(Application application)
    {
        return Typeface.createFromAsset(application.getAssets(), "fonts/Quicksand-Bold.otf");
    }


}
