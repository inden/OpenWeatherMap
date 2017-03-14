package com.in.den.android.openweather.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.in.den.android.openweather.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by harumi on 05/03/2017.
 */

public class SharedPreferenceOp {
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    Gson gson;
    @Inject
    Application application;

    private boolean bDefaultLocation = true;
    private String mDefaultLocation = "Paris";

    private static final String JSONARRAYSTRING = "[]";

    public SharedPreferenceOp(Application application, Gson gson,
                              SharedPreferences sharedPreferences) {
        this.application = application;
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;
    }

    public List<LocationInfo> getPreferenceLocations() {

        String jsonstring = sharedPreferences.getString(
                application.getString(R.string.sp_location_key),
                JSONARRAYSTRING);

        List<LocationInfo> loclist = new ArrayList<LocationInfo>();

        try {

            //loclist = gson.fromJson(jsonstring, ArrayList.class);

            loclist = new ArrayList<LocationInfo>(stringToArray(jsonstring, LocationInfo[].class));

        }
        catch (Exception ex) {}

        if(bDefaultLocation && loclist.size() == 0) {
            loclist.add(new LocationInfo(mDefaultLocation));
        }

        return loclist;
    }

    /*
    Problem to cast to LocationInfo
    this solution is provided in
    http://stackoverflow.com/questions/27253555/
    com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class
     */
    public  <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        //this returns non resizeable list
        //http://stackoverflow.com/questions/9320409/unsupportedoperationexception-at-java-util-abstractlist-add

        T[] arr = gson.fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public void setNotFoundLocations(List<String> notfounds) {
        List<LocationInfo> locations = getPreferenceLocations();
        for(String place : notfounds) {
            setNotFoundLocation(locations, place);
        }

        String newjson = gson.toJson(locations);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(application.getString(R.string.sp_location_key), newjson);
        editor.commit();
    }

    private void setNotFoundLocation(List<LocationInfo> locations, String notFoundLocation) {
        for(LocationInfo info : locations) {
            if(info.getName().equalsIgnoreCase(notFoundLocation)) {
                info.setBfound(false);
            }
        }
    }

    public void addOrRemoveLocation(String newloc, boolean badd) {


       List<LocationInfo> locations = getPreferenceLocations();

        if(badd) {
            boolean found = false;
            for(LocationInfo info : locations) {
                if(info.getName().equalsIgnoreCase(newloc)) {
                   found = true;
                    break;
                }
            }
            if(!found) {
                LocationInfo info = new LocationInfo();
                info.setName(newloc);
                locations.add(info);
            }
        }
        else {
            LocationInfo removeinfo = null;
            for(LocationInfo info : locations) {
                if(info.getName().equalsIgnoreCase(newloc)) {
                    removeinfo = info;
                    break;
                }
            }
            if(removeinfo != null)
               locations.remove(removeinfo);
        }

        String newjson = gson.toJson(locations);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(application.getString(R.string.sp_location_key), newjson);
        editor.commit();

    }

    public void addNewLocation(String newloc) {
        addOrRemoveLocation(newloc, true);
    }

    public void removeLocation(String newloc) {
        addOrRemoveLocation(newloc, false);
    }


    public long getPreviousLoadTime() {

        return sharedPreferences.getLong(application.getString(R.string.previousload), 0);
    }

    public void setPreviousLoadTime(long loadtime) {

        sharedPreferences.edit().putLong(application.getString(R.string.previousload), loadtime)
                .commit();
    }
}
