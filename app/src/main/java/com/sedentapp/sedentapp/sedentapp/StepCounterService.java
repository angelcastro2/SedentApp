package com.sedentapp.sedentapp.sedentapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.sedentapp.sedentapp.sedentapp.entities.RegistroDatabase;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StepCounterService extends Service {

    protected static final String TAG = "SedentApp";
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;
    private RegistroPasosService registroPasosService;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        StepCounterService getService() {
            return StepCounterService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "[StepCounterService] onCreate");
        connectFitness();
        mClient.connect();
        this.registroPasosService = new RegistroPasosService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "[StepCounterService] onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "[StepCounterService] onDestroy");
        mClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "[StepCounterService] onBind");
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    private void connectFitness() {
        Log.d(TAG, "[StepCounterService] connectFitness");
        if (mClient == null){
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE)) // GET STEP VALUES
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                                @Override
                                                public void onConnected(Bundle bundle) {
                                                    Log.d(TAG, "[StepCounterService] Connected!!!");
                                                    // Now you can make calls to the Fitness APIs.
                                                    findFitnessDataSources();

                                                }

                                                @Override
                                                public void onConnectionSuspended(int i) {
                                                    // If your connection to the sensor gets lost at some point,
                                                    // you'll be able to determine the reason and react to it here.
                                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                                        Log.d(TAG, "[StepCounterService] Connection lost.  Cause: Network Lost.");
                                                    } else if (i
                                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                                        Log.d(TAG,
                                                                "[StepCounterService] Connection lost.  Reason: Service Disconnected");
                                                    }
                                                }
                                            }
                    )
                    .build();
        }

    }

    private void findFitnessDataSources() {
        Log.d(TAG, "[StepCounterService] findFitnessDataSources");
        Fitness.SensorsApi.findDataSources(
                mClient,
                new DataSourcesRequest.Builder()
                        .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                        .setDataSourceTypes(DataSource.TYPE_DERIVED)
                        .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.d(TAG, "[StepCounterService]  Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.d(TAG, "[StepCounterService]  Data source found: " + dataSource.toString());
                            Log.d(TAG, "[StepCounterService]  Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA) && mListener == null) {
                                Log.d(TAG, "[StepCounterService] Data source for TYPE_STEP_COUNT_DELTA found!  Registering.");
                                registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                            }

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_CUMULATIVE) && mListener == null) {
                                Log.d(TAG, "[StepCounterService] Data source for TYPE_STEP_COUNT_CUMULATIVE found!  Registering.");
                                registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                            }
                        }
                    }
                });
    }

    private void registerFitnessDataListener(final DataSource dataSource, DataType dataType) {

        Log.d(TAG, "[StepCounterService] registerFitnessDataListener");

        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.d(TAG, "[StepCounterService] Detected DataPoint field: " + field.getName());
                    Log.d(TAG, "[StepCounterService] Detected DataPoint value: " + val);
                    updateStepCounter(val.asInt());
                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(),
                mListener).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.d(TAG, "[StepCounterService] Listener registered!");
                } else {
                    Log.d(TAG, "[StepCounterService] Listener not registered.");
                }
            }
        });

    }


    private void updateStepCounter(int steps) {

        Log.d(TAG, "[StepCounterService] updateStepCounter");

        Calendar calendar = Calendar.getInstance();

        RegistroPasos registroPasos = this.registroPasosService.getRegistroPasosByFechaAndHora(this,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY));

        if (registroPasos != null) {
            registroPasos.setPasos(registroPasos.getPasos() + steps);
            this.registroPasosService.update(registroPasos, this);
        }
        else {
            registroPasos = new RegistroPasos(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), steps);
            this.registroPasosService.save(registroPasos, this);
        }

        Log.d(TAG, "[StepCounterService] StepRegister at " + calendar.toString() + ": " + registroPasos.getPasos());

        Intent intent = new Intent();
        intent.setAction("com.sedentapp.read_daily_step_counter");
        sendBroadcast(intent);

    }
}
