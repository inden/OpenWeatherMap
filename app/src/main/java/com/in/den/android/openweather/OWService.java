package com.in.den.android.openweather;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.in.den.android.openweather.db.DaoMapper;
import com.in.den.android.openweather.db.DaoSession;
import com.in.den.android.openweather.db.Location;
import com.in.den.android.openweather.db.LocationDao;
import com.in.den.android.openweather.db.Weather;
import com.in.den.android.openweather.db.WeatherDao;
import com.in.den.android.openweather.net.OpenWeather;
import com.in.den.android.openweather.net.OpenWeatherService;
import com.in.den.android.openweather.util.SharedPreferenceOp;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harumi on 06/03/2017.
 */

public class OWService extends Service {

    @Inject
    SharedPreferenceOp sharedPreferenceOp;
    @Inject
    DaoSession daoSession;
    @Inject
    OpenWeatherService openWeatherService;

    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler());

    private String TAG = "OWService";
    public static final String REQUEST_LOCATION = "request_location";
    public static final String REQUEST_NBDAYS = "request_nbdays";
    public static final String REQUEST_METRIC = "request_metrics";

    public static final int REPLY_OWRESPONSE_NOT200 = 1;
    public static final int REPLY_OWREQUEST_FAILURE = 2;
    public static final int REPLY_OWSUCESS = 3;
    public static final int REPLY_DELETECITY_SUCCESS = 4;

    public static final int ADD_OPERATION = 100;
    public static final int DELETE_OPERATION = 200;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServerMessenger.getBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        ((OWApp) getApplication()).getApplicationComponent().inject(this);
    }

    protected class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Messenger replymessenger = msg.replyTo;
            if (bundle == null) return;

            switch (msg.what) {
                case ADD_OPERATION: loadWeather(bundle, replymessenger); break;
                case DELETE_OPERATION : deleteWeather(bundle, replymessenger); break;
            }
        }
    }

    private void deleteWeather(Bundle bundle, final Messenger replymessenger) {
        String location = bundle.getString(REQUEST_LOCATION, "");

        LocationDao locationDao = daoSession.getLocationDao();
        WeatherDao weatherDao = daoSession.getWeatherDao();

        List<Location> listloc = locationDao.queryBuilder()
                .where(LocationDao.Properties.Alias.eq(location)).list();

        if(listloc.size() > 0) {
            Location l = listloc.get(0);
            List<Weather> weathers = l.getLocationid();
            for(Weather w : weathers) {
                weatherDao.delete(w);
            }
            locationDao.delete(l);
        }

        replyMessage(replymessenger, REPLY_DELETECITY_SUCCESS);
    }

    private void loadWeather(Bundle bundle, final Messenger replymessenger) {

        final String location = bundle.getString(REQUEST_LOCATION, "");
        String querymetric = bundle.getString(REQUEST_METRIC, "metric");
        int querynbday = bundle.getInt(REQUEST_NBDAYS, 7);

        Call<OpenWeather> call = openWeatherService.
                getOpenWeather(location, "json", querymetric, querynbday);
        call.enqueue(new Callback<OpenWeather>() {
            @Override
            public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                int i = response.code();
                Log.d(TAG, i + "");
                if (i == 200) {
                    final OpenWeather ow = response.body();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadinDB(ow, location);
                            replyMessage(replymessenger, REPLY_OWSUCESS);

                        }
                    });

                } else {
                    replyMessage(replymessenger, REPLY_OWRESPONSE_NOT200);
                }
            }

            @Override
            public void onFailure(Call<OpenWeather> call, Throwable t) {
                replyMessage(replymessenger, REPLY_OWREQUEST_FAILURE);
            }
        });
    }

    private void replyMessage(Messenger messenger, int reply) {
        Message msg = new Message();
        msg.what = reply;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void loadinDB(OpenWeather openWeather, String location) {
        Location loc = DaoMapper.getLocation(openWeather, location);
        List<Weather> listweather = DaoMapper.getListWeather(openWeather);

        LocationDao locationDao = daoSession.getLocationDao();
        WeatherDao weatherDao = daoSession.getWeatherDao();

        List<Location> listloc = locationDao.queryBuilder()
                .where(LocationDao.Properties.Owcityid.eq(loc.getOwcityid())).list();

        long locid;
        if(listloc.size() == 0) {
            locid = locationDao.insert(loc);
        }
        else {

            locid = listloc.get(0).getId();

            List<Weather> dbweather = weatherDao.queryBuilder().
                    where(WeatherDao.Properties.LocationId.eq(locid)).list();

            for(Weather w : dbweather) {
                weatherDao.delete(w);
            }
        }

        for(Weather weather : listweather) {
            weather.setLocationId(locid);
            weatherDao.insert(weather);
        }
    }
}
