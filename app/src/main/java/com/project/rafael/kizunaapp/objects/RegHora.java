package com.project.rafael.kizunaapp.objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class RegHora extends RealmObject{
    //@PrimaryKey private int idRegHora;
    private int hora;
    private double sumTemp;
    private long sumPPM,sumO2;
    private int contTemp,contPPM,contO2;

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public double getSumTemp() {
        return sumTemp;
    }

    public void setSumTemp(double sumTemp) {
        this.sumTemp = sumTemp;
    }

    public long getSumPPM() {
        return sumPPM;
    }

    public void setSumPPM(long sumPPM) {
        this.sumPPM = sumPPM;
    }

    public long getSumO2() {
        return sumO2;
    }

    public void setSumO2(long sumO2) {
        this.sumO2 = sumO2;
    }

    public int getContTemp() {
        return contTemp;
    }

    public void setContTemp(int contTemp) {
        this.contTemp = contTemp;
    }

    public int getContPPM() {
        return contPPM;
    }

    public void setContPPM(int contPPM) {
        this.contPPM = contPPM;
    }

    public int getContO2() {
        return contO2;
    }

    public void setContO2(int contO2) {
        this.contO2 = contO2;
    }
}
