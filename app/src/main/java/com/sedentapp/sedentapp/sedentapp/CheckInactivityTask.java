package com.sedentapp.sedentapp.sedentapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
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
    private SharedPreferences sharedPref;

    private NotificationManager mNotificationManager;

    private final String TAG = "SedentApp";

    public CheckInactivityTask(Context context) {
        Log.d(TAG, "[CheckInactivityTask] Constructor");
        this.registroPasosService = new RegistroPasosService();
        this.context = context;
        this.mNotificationManager = mNotificationManager;
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private int getInactivityHours(List<RegistroPasos> registroPasosByDia) {

        if (registroPasosByDia.size() > 0) {
            int lastHour = registroPasosByDia.get(registroPasosByDia.size()-1).getHora();
            int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            int inactivityHours = now - lastHour;
            Log.d(TAG, "[CheckInactivityTask]: inactivity hours: " + inactivityHours);

            if (inactivityHours > 0)
                return inactivityHours;
        }

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

        int tiempoLimite = sharedPref.getInt("inactividad_list", 9999);

        if ((tiempoLimite != -1) & (inactivityHours > tiempoLimite)) {
            NotificationHelper notificationHelper = new NotificationHelper(this.context);
            notificationHelper.createNotification("Tiempo de inactividad demasiado alto!","Llevas " + inactivityHours + " horas inactivo");
        }

    }

}
