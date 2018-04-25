package com.sedentapp.sedentapp.sedentapp.entities.registropasos.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;

import java.sql.Date;
import java.util.List;

@Dao
public interface RegistroPasosDao {

    @Insert
    void save(RegistroPasos registroPasos);

    @Query("SELECT * FROM RegistroPasos WHERE registroPasosId = :registroPasosId")
    RegistroPasos find(long registroPasosId);

    @Query("SELECT * FROM RegistroPasos WHERE dia = :dia AND mes = :mes AND ano = :ano AND hora = :hora")
    RegistroPasos getPasosByFechaAndHora(int dia, int mes, int ano, int hora);

    //@Query("SELECT SUM(pasos) FROM RegistroPasos WHERE dia = :dia AND mes = :mes AND ano = :ano")
   // RegistroPasos getPasosByDia(int dia, int mes, int ano);

    @Query("SELECT * FROM RegistroPasos WHERE dia = :dia AND mes = :mes AND ano = :ano")
    List<RegistroPasos> getRegistroPasosByDia(int dia, int mes, int ano);

    @Update
    void update (RegistroPasos registroPasos);

    @Delete
    void delete(RegistroPasos registroPasos);

}
