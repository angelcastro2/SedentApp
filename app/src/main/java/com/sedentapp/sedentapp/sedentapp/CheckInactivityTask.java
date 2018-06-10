package com.sedentapp.sedentapp.sedentapp;

import android.content.Context;
import android.content.Intent;

import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CheckInactivityTask extends TimerTask {

    private RegistroPasosService registroPasosService;
    private Context context;

    public CheckInactivityTask(Context context) {
        this.registroPasosService = new RegistroPasosService();
        this.context = context;
    }

    private int getInactivityHours(List<RegistroPasos> registroPasosByDia) {

        int hora1 = 0;
        int hora2 = 0;

        int max_diff = 0;
        int diff = 0;

        for (int i = 0; i < registroPasosByDia.size(); i++) {
            hora2 = registroPasosByDia.get(i).getHora();
            diff = hora2 - hora1;
            if (diff > max_diff) {
                max_diff = diff;
            }
        }

        return 0;
    }

    public void run() {

        Calendar calendar = Calendar.getInstance();

        List<RegistroPasos> registroPasosByDia = this.registroPasosService.getRegistroPasosByDia(this.context,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        int inactivityHours = getInactivityHours(registroPasosByDia);

        Intent intent = new Intent();
        intent.putExtra("inactivityTime", inactivityHours);
        intent.setAction("com.sedentapp.com.sedentapp.update_inactivity_time_counter");
        this.context.sendBroadcast(intent);


    }

}
