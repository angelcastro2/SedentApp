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

import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.Calendar;
import java.util.Timer;

import static android.content.pm.PackageManager.FEATURE_SENSOR_STEP_COUNTER;
import static android.content.pm.PackageManager.FEATURE_SENSOR_STEP_DETECTOR;

public class StepsService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    //private StepsDBHelper mStepsDBHelper;
    protected static final String TAG = "SedentApp";
    private RegistroPasosService registroPasosService;

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
            Log.d(TAG, "[StepsService] Step detector sensor registered");

            this.registroPasosService = new RegistroPasosService();

            Timer time = new Timer(); // Instantiate Timer Object
            CheckInactivityTask st = new CheckInactivityTask(getApplicationContext()); // Instantiate SheduledTask class
            time.schedule(st, 0, 1000*60); // Create Repetitively task for every 1 secs

        }
        else {
            Log.e(TAG, "[StepsService] Cannot register step detector sensor");
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
        Log.d(TAG, "[StepsService] onSensorChanged");

        String sensorName = event.sensor.getName();
        if (sensorName.equals(this.mStepDetectorSensor.getName()) | event.sensor.equals(this.mStepDetectorSensor)) {
            Log.d(TAG, "[StepsService] STEP DETECTED!");
            int steps = (int) event.values[0];
            updateStepCounter(steps);

        }

    }

    private void updateStepCounter(int steps) {

        Log.d(TAG, "[StepsService] updateStepCounter");

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

        Log.d(TAG, "[StepsService] StepRegister at " + calendar.toString() + ": " + registroPasos.getPasos());

        Intent intent = new Intent();
        intent.setAction("com.sedentapp.read_daily_step_counter");
        sendBroadcast(intent);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}