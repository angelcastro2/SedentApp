package com.sedentapp.sedentapp.sedentapp.entities.registrodiario;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
//TODO: ACABAR
@Entity
public class RegistroDiario {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long registroDiarioId;

    @NonNull
    private int hora;

    @NonNull
    private int pasos;


    public RegistroDiario(){
    }

    public RegistroDiario(@NonNull int hora, @NonNull int pasos) {
        this.hora = hora;
        this.pasos = pasos;
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
        return "RegistroDiario{" +
                "registroDiarioId=" + registroDiarioId +
                ", hora=" + hora +
                ", pasos=" + pasos +
                '}';
    }
}
