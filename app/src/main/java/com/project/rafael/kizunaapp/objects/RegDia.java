package com.project.rafael.kizunaapp.objects;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class RegDia extends RealmObject{
    //@PrimaryKey private int idRegDia;
    private int año;
    private int mes;
    private int dia;
    private RealmList<RegHora> regHoras=new RealmList<>();

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public RealmList<RegHora> getRegHoras() {
        return regHoras;
    }

    public void setRegHoras(RealmList<RegHora> regHoras) {
        this.regHoras = regHoras;
    }
}
