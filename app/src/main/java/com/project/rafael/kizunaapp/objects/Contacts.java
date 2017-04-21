package com.project.rafael.kizunaapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class Contacts extends RealmObject {
    @PrimaryKey private int idContact;
    private String nombreContc,numTel;

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public String getNombreContc() {
        return nombreContc;
    }

    public void setNombreContc(String nombreContc) {
        this.nombreContc = nombreContc;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
