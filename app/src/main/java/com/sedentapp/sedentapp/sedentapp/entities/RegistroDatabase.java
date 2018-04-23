package com.sedentapp.sedentapp.sedentapp.entities;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sedentapp.sedentapp.sedentapp.entities.registrodiario.RegistroDiario;
import com.sedentapp.sedentapp.sedentapp.entities.registrodiario.dao.RegistroDiarioDao;
import com.sedentapp.sedentapp.sedentapp.entities.registrohora.RegistroHora;
import com.sedentapp.sedentapp.sedentapp.entities.registrohora.dao.RegistroHoraDao;

@Database(entities = {RegistroDiario.class, RegistroHora.class}, version = 1, exportSchema = false)
public abstract class RegistroDatabase extends RoomDatabase{
    public abstract RegistroDiarioDao registroDiarioDao() ;
    public abstract RegistroHoraDao registroHoraDao() ;
}
