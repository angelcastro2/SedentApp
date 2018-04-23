package com.sedentapp.sedentapp.sedentapp.entities.registrodiario;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Date;

@Entity
public class RegistroDiario {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long registroDiarioId;

    @NonNull
    @ColumnInfo(name = "fecha")
    private Date fecha;

    @NonNull
    @ColumnInfo(name = "pasos")
    private int pasos;


    public RegistroDiario(){
    }

    public RegistroDiario(@NonNull Date fecha, @NonNull int pasos) {
        this.fecha = fecha;
        this.pasos = pasos;
    }

    @NonNull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull Date fecha) {
        this.fecha = fecha;
    }

    @NonNull
    public int getPasos() {
        return pasos;
    }

    public void setPasos(@NonNull int pasos) {
        this.pasos = pasos;
    }

    @Override
    public String toString() {
        return "RegistroDiario{" +
                "registroDiarioId=" + registroDiarioId +
                ", fecha=" + fecha +
                ", pasos=" + pasos +
                '}';
    }
}
