package com.sedentapp.sedentapp.sedentapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.location.LocationResult;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class CalibrationActivity extends Activity implements View.OnClickListener, android.content.DialogInterface.OnClickListener {

    private Button get_location_button;
    private Location start_location;
    private Location end_location;
    private Boolean start_flag = true;

    private Button buttonGetLocation;

    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        buttonGetLocation = (Button) findViewById(R.id.get_location_button);
        buttonGetLocation.setOnClickListener(this);

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        // don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention!");
            builder.setMessage("Sorry, location is not determined. Please enable location providers");
            builder.setPositiveButton("OK", this);
            builder.setNeutralButton("Cancel", this);
            builder.create().show();

        }

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
         }
         if (network_enabled) {
             locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
         }
    }


class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // This needs to stop getting the location data and save the battery power.
            locManager.removeUpdates(locListener);

            String londitude = "Londitude: " + location.getLongitude();
            String latitude = "Latitude: " + location.getLatitude();
            String altitiude = "Altitiude: " + location.getAltitude();
            String accuracy = "Accuracy: " + location.getAccuracy();
            String time = "Time: " + location.getTime();

            AlertDialog alertDialog = new AlertDialog.Builder(CalibrationActivity.this).create();
            alertDialog.setTitle("posicion");
            alertDialog.setMessage(londitude + "," + latitude + "," + altitiude);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_NEUTRAL){
            AlertDialog alertDialog = new AlertDialog.Builder(CalibrationActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("No se ha podido obtener localización");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

}
