package com.sedentapp.sedentapp.sedentapp.entities.registropasos.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;

import java.sql.Date;

@Dao
public interface RegistroPasosDao {

    @Insert
    void save(RegistroPasos registroPasos);

    @Query("SELECT * FROM RegistroPasos WHERE registroPasosId = :registroPasosId")
    RegistroPasos find(int registroPasosId);

    @Query("SELECT * FROM REGISTROPASOS WHERE dia = :dia AND mes = :mes AND ano = :ano AND hora = :hora")
    RegistroPasos getPAsosByFechaAndHora(int dia, int mes, int ano, int hora);

    @Update
    void update (RegistroPasos registroPasos);

    @Delete
    void delete(RegistroPasos registroPasos);

}
