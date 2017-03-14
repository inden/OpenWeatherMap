package com.in.den.android.openweather.util;

/**
 * Created by harumi on 05/03/2017.
 */

public class LocationInfo {
    private String name;
    private String country;
    private boolean bfound = true;

    public LocationInfo(){}

    public LocationInfo(String location) {
        name = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isBfound() {
        return bfound;
    }

    public void setBfound(boolean bfound) {
        this.bfound = bfound;
    }


}
