package com.project.rafael.kizunaapp.objects;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.Context;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class Users extends RealmObject {
    @PrimaryKey private int id;
    private String userName,pass,nombre,apellido,correo;
    private String direccion,afecciones,ocupacion,rutaImag;
    private int edad,codDisp;
    private String MACdisp;
    private RealmList<RegDia> regDias=new RealmList<>();
    private RealmList<Contacts> listContacts;
    private int o2,ppm;
    private double temp;
    private int signal;


    public RealmList<RegDia> getRegDias() {
        return regDias;
    }

    public void setRegDias(RealmList<RegDia> regDias) {
        this.regDias = regDias;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getO2() {
        return o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getMACdisp() {
        return MACdisp;
    }

    public void setMACdisp(String MACdisp) {
        this.MACdisp = MACdisp;
    }
    public RealmList<Contacts> getListContacts() {
        return listContacts;
    }

    public void setListContacts(RealmList<Contacts> listContacts) {
        this.listContacts = listContacts;
    }

    public String getRutaImag() {
        return rutaImag;
    }

    public void setRutaImag(String rutaImag) {
        this.rutaImag = rutaImag;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAfecciones() {
        return afecciones;
    }

    public void setAfecciones(String afecciones) {
        this.afecciones = afecciones;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getCodDisp() {
        return codDisp;
    }

    public void setCodDisp(int codDisp) {
        this.codDisp = codDisp;
    }
}
