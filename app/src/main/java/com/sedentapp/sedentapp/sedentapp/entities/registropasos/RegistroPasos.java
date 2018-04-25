package com.sedentapp.sedentapp.sedentapp.entities.registropasos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class RegistroPasos {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long registroPasosId;


    @NonNull
    @ColumnInfo(name = "dia")
    private int dia;

    @NonNull
    @ColumnInfo(name = "mes")
    private int mes;

    @NonNull
    @ColumnInfo(name = "ano")
    private int ano;

    @NonNull
    @ColumnInfo(name = "hora")
    private int hora;

    @NonNull
    @ColumnInfo(name = "pasos")
    private int pasos;


    public RegistroPasos(){
    }

    @Ignore
    public RegistroPasos(@NonNull int dia, @NonNull int mes, @NonNull int ano, @NonNull int hora, @NonNull int pasos) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.pasos = pasos;
    }

    @NonNull
    public Long getRegistroPasosId() {
        return registroPasosId;
    }

    public void setRegistroPasosId(@NonNull Long registroPasosId) {
        this.registroPasosId = registroPasosId;
    }

    @NonNull
    public int getDia() {
        return dia;
    }

    public void setDia(@NonNull int dia) {
        this.dia = dia;
    }

    @NonNull
    public int getMes() {
        return mes;
    }

    public void setMes(@NonNull int mes) {
        this.mes = mes;
    }

    @NonNull
    public int getAno() {
        return ano;
    }

    public void setAno(@NonNull int ano) {
        this.ano = ano;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistroPasos)) return false;

        RegistroPasos that = (RegistroPasos) o;

        if (getDia() != that.getDia()) return false;
        if (getMes() != that.getMes()) return false;
        if (getAno() != that.getAno()) return false;
        if (getHora() != that.getHora()) return false;
        if (getPasos() != that.getPasos()) return false;
        return getRegistroPasosId().equals(that.getRegistroPasosId());
    }

    @Override
    public int hashCode() {
        int result = getRegistroPasosId().hashCode();
        result = 31 * result + getDia();
        result = 31 * result + getMes();
        result = 31 * result + getAno();
        result = 31 * result + getHora();
        result = 31 * result + getPasos();
        return result;
    }

    @Override
    public String toString() {
        return "RegistroPasos{" +
                "registroPasosId=" + registroPasosId +
                ", dia=" + dia +
                ", mes=" + mes +
                ", ano=" + ano +
                ", hora=" + hora +
                ", pasos=" + pasos +
                '}';
    }
}
