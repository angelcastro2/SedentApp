package com.sedentapp.sedentapp.sedentapp.entities.registrodiario.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sedentapp.sedentapp.sedentapp.entities.registrodiario.RegistroDiario;

import java.sql.Date;

@Dao
public interface RegistroDiarioDao {

    @Insert
    void save(RegistroDiario registroDiario);

    @Query("SELECT * FROM RegistroDiario WHERE registroDiarioId = :registroDiarioId")
    RegistroDiario find(int registroDiarioId);

    @Query("SELECT pasos FROM RegistroDiario WHERE fecha = :fecha")
    Integer getPasosByFecha(Date fecha);

    @Update
    RegistroDiario update (RegistroDiario registroDiario);

    @Delete
    void delete(RegistroDiario registroDiario);

}
