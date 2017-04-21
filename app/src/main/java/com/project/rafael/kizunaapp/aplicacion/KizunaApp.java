package com.project.rafael.kizunaapp.aplicacion;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by RAFAEL on 21/03/2017.
 */

public class KizunaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration= new RealmConfiguration.Builder().name("KizunaApp.realm").schemaVersion(1).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
