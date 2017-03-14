package com.in.den.android.openweather.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.OWService;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.DaoSession;
import com.in.den.android.openweather.db.Location;
import com.in.den.android.openweather.util.LocationInfo;
import com.in.den.android.openweather.util.SharedPreferenceOp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.in.den.android.openweather.OWService.REPLY_OWREQUEST_FAILURE;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Messenger mServiceMessenger;
    private Messenger mReceieveMessenger;
    private boolean bounded = false;
    private List<LocationInfo> locationInfos;
    private List<String> deletedlocations = new ArrayList<String>();
    private List<String> addedLocations = new ArrayList<>();
    private List<Operation> listop = new ArrayList<Operation>();
    private Handler handler = new Handler();
    private long currenttime;
    private int currentPos;
    private Intent intentservice;

    private long elapsedtime = 1000 * 60 * 60 * 6;
    private boolean bloadforced = false; //true for debug purpose only
    private boolean bFailed = false;

    @Inject
    SharedPreferenceOp sharedPreferenceOp;
    @Inject
    DaoSession daoSession;
    @Inject
    Typeface custom_font;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((OWApp) getApplication()).getApplicationComponent().inject(this);

        final long previousload = sharedPreferenceOp.getPreviousLoadTime();
        currenttime = System.currentTimeMillis();

        locationInfos = sharedPreferenceOp.getPreferenceLocations();

        boolean bAddedorDeleted = setAddedOrDeleted(locationInfos);

        if (currenttime - previousload < elapsedtime && !bloadforced
                && !bAddedorDeleted) {
            //nothing new so go to main activity
            startMainActivity(this);
        }

        if (locationInfos.size() > 0 &&
                (currenttime - previousload >= elapsedtime || bloadforced)) {
            //replace addedlist to load all places;
            addedLocations.clear();
            for (LocationInfo locationInfo : locationInfos) {
                addedLocations.add(locationInfo.getName());
            }
        }

        currentPos = 0;
        initOperationList();

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        tvTitle.setTypeface(custom_font);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mReceieveMessenger = new Messenger(new RecievegHandler(this));
        int mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ?
                0 : Context.BIND_ABOVE_CLIENT;
        intentservice = new Intent(this, OWService.class);
        startService(intentservice);
        Log.d(TAG, "bindservice");
        bindService(intentservice, mServiceConnection, mBindFlag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnableProgressbar);
    }

    private Runnable runnableProgressbar = new Runnable() {
        @Override
        public void run() {
            progressBar.setProgress(progressBar.getProgress() + 3);

            if (progressBar.getProgress() < 100) {

                handler.postDelayed(runnableProgressbar, 1000);
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();

        if (mServiceMessenger != null) {
            mServiceMessenger = null;
        }

        unbindService(mServiceConnection);
        stopService(intentservice);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            bounded = true;
            mServiceMessenger = new Messenger(service);

            makeRequest2Service();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mServiceMessenger = null;
            bounded = false;
        }
    }; // mServiceConnection


    private static class Operation {
        boolean addOp;
        String location;
    }

    public void initOperationList() {
        for (int i = 0; i < deletedlocations.size(); i++) {
            Operation op = new Operation();
            op.addOp = false;
            op.location = deletedlocations.get(i);
            listop.add(op);
        }
        for (int i = 0; i < addedLocations.size(); i++) {
            Operation op = new Operation();
            op.addOp = true;
            op.location = addedLocations.get(i);
            listop.add(op);
        }
    }


    private void makeRequest2Service() {
        if (!bounded) return;

        Message msg = new Message();
        Bundle bundle = new Bundle();

        Operation op = listop.get(currentPos);

        if (op.addOp) {
            msg.what = OWService.ADD_OPERATION;
        } else {
            msg.what = OWService.DELETE_OPERATION;
        }
        bundle.putString(OWService.REQUEST_LOCATION, op.location);
        msg.setData(bundle);
        msg.replyTo = mReceieveMessenger;

        try {
            mServiceMessenger.send(msg);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            //need to stop app here
        }
    }

    private void startMainActivity(Context context) {
        Intent intentmain = new Intent(context, MainActivity.class);
        intentmain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentmain);
        finish();
    }

    private boolean setAddedOrDeleted(List<LocationInfo> spLocations) {

        List<Location> dblocations = daoSession.getLocationDao().queryBuilder().list();

        for (Location l : dblocations) {
            String alias = l.getAlias();
            boolean bfound = false;
            if (alias != null) {
                for (LocationInfo locationInfo : spLocations) {
                    String name = locationInfo.getName();
                    if (alias.equalsIgnoreCase(name)) {
                        bfound = true;
                        break;
                    }
                }
            }
            if (!bfound) {
                if (alias != null)
                    deletedlocations.add(alias);
                else
                    deletedlocations.add(l.getName());
            }
        }

        for (LocationInfo locationInfo : spLocations) {
            String name = locationInfo.getName();
            boolean bfound = false;

            for (Location l : dblocations) {
                String alias = l.getAlias();
                if (alias != null && alias.equalsIgnoreCase(name)) {
                    bfound = true;
                    break;
                }
            }
            if (!bfound) {
                addedLocations.add(name);
            }
        }

        if (deletedlocations.size() == 0 && addedLocations.size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private class RecievegHandler extends Handler {

        Context context;

        public RecievegHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "requested processed with the code " + msg.what);

            if(msg.what == OWService.REPLY_OWRESPONSE_NOT200 ||
                    msg.what == OWService.REPLY_OWREQUEST_FAILURE) {
                bFailed = true;
            }

            if (currentPos < listop.size() - 1) {
                currentPos++;

                post(new Runnable() {
                    @Override
                    public void run() {
                        makeRequest2Service();
                    }
                });

            } else {

                if(bFailed) {
                    new AlertDialog.Builder(context).setMessage(R.string.alert_update_fail)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startMainActivity(context);
                                }})
                            .show();
                }
                else {

                    progressBar.setVisibility(View.GONE);
                    sharedPreferenceOp.setPreviousLoadTime(currenttime);
                    startMainActivity(context);
                }
            }
        }
    }
}
