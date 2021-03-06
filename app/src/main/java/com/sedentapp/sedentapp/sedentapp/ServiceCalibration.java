package com.sedentapp.sedentapp.sedentapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class ServiceCalibration extends Service {

    FusedLocationProviderClient mFusedLocationClient;
    double longitude;
    double latitude;

     int MY_PERMISSIONS_REQUEST_FINE_LOCATION;

    public ServiceCalibration() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // no communication channel is required
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        this.stopSelf();
        return START_NOT_STICKY;
    }

    public void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void getLocation() {


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            sendMessageToActivity(location);
                        }
                    }
                });

    }

    private void sendMessageToActivity(Location location) {
        Intent intent = new Intent("GPSLocation");
        // You can also include some extra data.
        Bundle b = new Bundle();
        b.putParcelable("location", location);
        intent.putExtra("location", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
