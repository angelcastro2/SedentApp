package com.sedentapp.sedentapp.sedentapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CheckInactivityTask extends TimerTask {

    private RegistroPasosService registroPasosService;
    private Context context;

    private NotificationManager mNotificationManager;

    private final String TAG = "SedentApp";

    public CheckInactivityTask(Context context, NotificationManager mNotificationManager ) {
        Log.d(TAG, "[CheckInactivityTask] Constructor");
        this.registroPasosService = new RegistroPasosService();
        this.context = context;
        this.mNotificationManager = mNotificationManager;
    }

    private int getInactivityHours(List<RegistroPasos> registroPasosByDia) {

        int hora1 = 0;
        int hora2 = 0;

        int max_diff = 0;
        int diff = 0;

        int lastHour = registroPasosByDia.get(registroPasosByDia.size()-1).getHora();
        int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        int inactivityHours = now - lastHour;
        Log.d(TAG, "[CheckInactivityTask]: inactivity hours: " + inactivityHours);

        if (inactivityHours > 0)
            return inactivityHours;
        else
            return 0;
    }

    public void run() {

        Log.d(TAG, "[CheckInactivityTask] run");

        Calendar calendar = Calendar.getInstance();

        List<RegistroPasos> registroPasosByDia = this.registroPasosService.getRegistroPasosByDia(this.context,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        int inactivityHours = getInactivityHours(registroPasosByDia);

        Intent intent = new Intent();
        intent.putExtra("inactivityTime", inactivityHours);
        intent.setAction("com.sedentapp.update_inactivity_time_counter");
        this.context.sendBroadcast(intent);


//        Intent resultIntent = new Intent(this.context, MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this.context, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

//        Notification.Builder mBuilder = new Notification.Builder(this.context) // builder notification
//                .setSmallIcon(R.drawable.ic_fitness_center_black_24dp)
//                .setContentTitle("Tiempo de inactividad demasiado alto!")
//                .setContentText("Llevas " + inactivityHours + " horas inactivo") .setWhen(System.currentTimeMillis())
//                .setVibrate(new long[]{0,100,200,300}).setLights(Color.RED, 2000, 1000)
//                .setTicker("New notification arrived!") // text shown when notification arrived
//                .addAction(android.R.drawable.ic_menu_share, "Share", pIntent) // max. 3 buttons
//                .setContentIntent(pIntent);
//        mNotificationManager.notify(0, mBuilder.build());


        if (inactivityHours > 2) {
            NotificationHelper notificationHelper = new NotificationHelper(this.context);
            notificationHelper.createNotification("Tiempo de inactividad demasiado alto!","Llevas " + inactivityHours + " horas inactivo");
        }

    }

}
