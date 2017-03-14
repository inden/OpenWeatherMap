package com.in.den.android.openweather.db;

import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.in.den.android.openweather.net.OpenWeather;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by harumi on 04/03/2017.
 */

public class DaoMapper {

    public static Location getLocation(OpenWeather openWeather, String querylocation) {

        Location location = new Location();
        OpenWeather.City city = openWeather.getCity();
        location.setOwcityid(city.getId());
        location.setName(city.getName());
        location.setCountry(city.getCountry());
        location.setCoord_lat(city.getCoord().getLat());
        location.setCoord_lon(city.getCoord().getLon());
        location.setAlias(querylocation);

        return location;
    }

    public static List<Weather> getListWeather(OpenWeather openWeather) {
        List<Weather> list = new ArrayList<Weather>() ;

        List<OpenWeather.WList> wLists = openWeather.getList();
        for(OpenWeather.WList wList : wLists) {

            Weather weather = new Weather();
            weather.setCloud((double)wList.getClouds());
            weather.setDeg((double)wList.getDeg());
            weather.setDt(wList.getDt());
            weather.setHumidity((double)wList.getHumidity());
            weather.setOwcityid(openWeather.getCity().getId());
            weather.setPressure(wList.getPressure());
            weather.setRain(wList.getRain());
            weather.setSpeed(wList.getSpeed());
            weather.setTemp_day(wList.getTemp().getDay());
            weather.setTemp_min(wList.getTemp().getMin());
            weather.setTemp_max(wList.getTemp().getMax());
            weather.setTemp_morning(wList.getTemp().getMorn());
            weather.setTemp_eve(wList.getTemp().getEve());
            weather.setTemp_night(wList.getTemp().getNight());
            weather.setWeather_description(wList.getWeather().get(0).getDescription());
            weather.setWeather_main(wList.getWeather().get(0).getMain());
            weather.setWeather_id(wList.getWeather().get(0).getId());

            list.add(weather);
        }

        return list;
    }
}
