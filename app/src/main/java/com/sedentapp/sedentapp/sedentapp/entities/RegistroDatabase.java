package com.sedentapp.sedentapp.sedentapp.entities;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.dao.RegistroPasosDao;

@Database(entities = {RegistroPasos.class}, version = 1, exportSchema = false)
public abstract class RegistroDatabase extends RoomDatabase{
    public abstract RegistroPasosDao registroHoraDao() ;
}
