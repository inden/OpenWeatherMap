package com.in.den.android.openweather.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by harumi on 04/03/2017.
 */
@Module
public class DbModule {

    private String dbname;
    public DbModule(String dbname) {
        this.dbname = dbname;
    }

    @Provides
    @Singleton
    DaoMaster.OpenHelper provideOpenHelper(Application application) {
        return new DaoMaster.DevOpenHelper(application, dbname, null);
    }

    @Provides
    @Singleton
    SQLiteDatabase provideWritableDatabase(DaoMaster.OpenHelper helper) {
        return helper.getWritableDatabase();
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(SQLiteDatabase db) {
        return new DaoMaster(db);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }
}
