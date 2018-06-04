package com.sedentapp.sedentapp.sedentapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class StepsService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    //private StepsDBHelper mStepsDBHelper;
    protected static final String TAG = "SedentApp";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "[StepsService] onCreate");
        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetectorSensor =
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            //mStepsDBHelper = new StepsDBHelper(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int
            startId) {
        Log.d(TAG, "[StepsService] START");
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "[StepsService] STEP DETECTED");
        //aqui guardar pasos en la base de datos
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}