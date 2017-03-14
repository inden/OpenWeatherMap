package com.in.den.android.openweather.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by harumi on 03/03/2017.
 */

public interface OpenWeatherService {

    @GET("/data/2.5/forecast/daily")
    Call<OpenWeather> getOpenWeather(
            @Query("q") String location,
            @Query("mode") String mode,
            @Query("units") String units,
            @Query("cnt") int days
    );

    @GET("/data/2.5/forecast/daily")
    Call<OpenWeather> getOpenWeather(@Query("q") String location);

}
