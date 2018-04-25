package com.sedentapp.sedentapp.sedentapp.entities.registropasos.service;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sedentapp.sedentapp.sedentapp.entities.RegistroDatabase;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;

import java.util.List;

public class RegistroPasosService {
    private RegistroDatabase registroDatabase;

    private RegistroDatabase getDatabase(Context context){
        if (registroDatabase == null){
            registroDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    RegistroDatabase.class, "RegistroDatabase.db")
                    .build();
        }
        return registroDatabase;
    }

    public void save(RegistroPasos registroPasos, Context context){
        registroDatabase = getDatabase(context);
        registroDatabase.registroPasosDao().save(registroPasos);
    }

    public RegistroPasos find(Context context, long registroPasosId){
        registroDatabase = getDatabase(context);
        return registroDatabase.registroPasosDao().find(registroPasosId);
    }

    public void update(RegistroPasos registroPasos, Context context){
        registroDatabase = getDatabase(context);
        registroDatabase.registroPasosDao().update(registroPasos);
    }

    public void delete(RegistroPasos registroPasos, Context context){
        registroDatabase = getDatabase(context);
        registroDatabase.registroPasosDao().delete(registroPasos);
    }

    public long getPasosByDia(Context context, int dia, int mes, int ano){
        long total = 0;
        registroDatabase = getDatabase(context);
        List<RegistroPasos> listaPasos = registroDatabase.registroPasosDao().getRegistroPasosByDia(dia,mes,ano);

        for (RegistroPasos r: listaPasos){
            total += r.getPasos();
        }
        return total;
    }


}
