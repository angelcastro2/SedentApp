package com.sedentapp.sedentapp.sedentapp.entities.registrohora;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Date;

public class RegistroHora {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long registroHoraId;


    private Long registroDiarioId;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fecha")
    private Date fecha;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "hora")
    private int hora;

    @NonNull
    @ColumnInfo(name = "pasos")
    private int pasos;


    public RegistroHora(){
    }

    public RegistroHora(@NonNull Date fecha, @NonNull int hora, @NonNull int pasos) {
        this.fecha = fecha;
        this.hora = hora;
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
    public int getHora() {
        return hora;
    }

    public void setHora(@NonNull int hora) {
        this.hora = hora;
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
        return "RegistroHora{" +
                "registroHoraId=" + registroHoraId +
                ", registroDiarioId=" + registroDiarioId +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", pasos=" + pasos +
                '}';
    }
}
