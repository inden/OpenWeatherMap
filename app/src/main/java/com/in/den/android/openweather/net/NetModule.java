package com.in.den.android.openweather.net;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.in.den.android.openweather.R;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harumi on 03/03/2017.
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    Interceptor provideInterceptor(Application application, Properties properties) {

        final String APIKEY_PARAM = properties.getProperty(
                application.getString(R.string.properties_apikey_param));
        final String APIKEY_VALUE = properties.getProperty(
                application.getString(R.string.properties_apikey_value));
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(APIKEY_PARAM, APIKEY_VALUE)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor interceptor) {

        OkHttpClient httpClient =
                new OkHttpClient.Builder().addInterceptor(interceptor
                ).build();

        return httpClient;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient,
                             Application application, Properties config) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(config.getProperty(
                        application.getString(R.string.properties_baseurl)))
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    OpenWeatherService provideOpenWeatherService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherService.class);
    }

}
