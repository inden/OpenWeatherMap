package com.in.den.android.openweather.db;

/**
 * Created by harumi on 04/03/2017.
 */

public class DbOperation {

    DaoSession mDaoSession;
    public DbOperation(DaoSession daoSession) {
        mDaoSession = daoSession;
    }

    public boolean isLocationFound(Location location) {
        int cityid = location.getOwcityid();
        return true;
    }
}
