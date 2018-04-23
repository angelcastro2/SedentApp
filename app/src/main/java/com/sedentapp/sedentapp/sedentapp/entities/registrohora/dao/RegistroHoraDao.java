package com.sedentapp.sedentapp.sedentapp.entities.registrohora.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sedentapp.sedentapp.sedentapp.entities.registrohora.RegistroHora;

import java.sql.Date;

@Dao
public interface RegistroHoraDao {

    @Insert
    void save(RegistroHora registroHora);

    @Query("SELECT * FROM RegistroHora WHERE registroHoraId = :registroHoraId")
    RegistroHora find(int registroHoraId);

    @Query("SELECT * FROM RegistroHora WHERE fecha = :fecha AND hora = :hora")
    Integer getPasosByFechaAndHora(Date fecha, int hora);

    @Update
    RegistroHora update (RegistroHora registroHora);

    @Delete
    void delete(RegistroHora registroHora);

}
