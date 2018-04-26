package com.sedentapp.sedentapp.sedentapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReadDatabaseService extends Service {

    protected static final String TAG = "SedentApp";
    private RegistroPasosService registroPasosService;
    private final GetDailyStepCounterBroadcastReceiver broadCastReceiver = new GetDailyStepCounterBroadcastReceiver();

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        ReadDatabaseService getService() {
            return ReadDatabaseService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "[ReadDatabaseService] onCreate");
        this.registroPasosService = new RegistroPasosService();

        // Register
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.sedentapp.read_daily_step_counter");
        registerReceiver(broadCastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "[ReadDatabaseService] onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "[ReadDatabaseService] onDestroy");
        unregisterReceiver(broadCastReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "[ReadDatabaseService] onBind");
        updateDailyStepCounter();
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new ReadDatabaseService.LocalBinder();


    private void updateDailyStepCounter() {

        Log.d(TAG, "[ReadDatabaseService] updateDailyStepCounter");

        int i, dailyStepCounter = 0;
        Calendar calendar = Calendar.getInstance();
        List<RegistroPasos> registrosPasos = this.registroPasosService.getRegistroPasosByDia(this,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        for (i = 0; i < registrosPasos.size(); i++) {
            dailyStepCounter = registrosPasos.get(i).getPasos();
        }

        Intent intent = new Intent();
        intent.setAction("com.sedentapp.update_daily_step_counter");
        intent.putExtra("dailyStepCounter", dailyStepCounter);
        sendBroadcast(intent);

    }

    private class GetDailyStepCounterBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateDailyStepCounter();
        }
    }


}
