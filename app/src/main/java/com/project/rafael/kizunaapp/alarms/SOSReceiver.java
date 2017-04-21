package com.project.rafael.kizunaapp.alarms;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.project.rafael.kizunaapp.NavigationActivity;
import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.RegistroActivity;
import com.project.rafael.kizunaapp.SplashActivity;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Contacts;
import com.project.rafael.kizunaapp.objects.Users;

import java.util.ArrayList;

import io.realm.RealmChangeListener;

/**
 * Created by RAFAEL on 02/04/2017.
 */

public class SOSReceiver extends BroadcastReceiver {
    String user;
    SentenciasSQL sentenciasSQL;
    Users users;
    boolean cond=true;
    @Override
    public void onReceive(final Context context, Intent intent) {
        user= NavigationActivity.user;
        sentenciasSQL=new SentenciasSQL();
        users=sentenciasSQL.ObtenerUsuario(user);

        RealmChangeListener listener=new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                int o2=users.getO2();
                int ppm=users.getPpm();
                double temp=users.getTemp();
                if(temp>30&&cond==true){
                //if((temp<22||temp>38||(ppm>40&&ppm<60)||ppm>170||(o2<70))&&(o2!=0)){
                    cond=false;
                    OnOff onOff=new OnOff();
                    GPSLocator gpsLocator=new GPSLocator(context);
                    Boolean list[]=NavigationActivity.listOpciones;

                    onOff.Off(context);

                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append(users.getNombre()+" "+users.getApellido()+"-"+"PPM:"+String.valueOf(ppm)+"-O2:"+String.valueOf(o2)+"-Temp:"+String.valueOf(temp)+"-");
                    if (list[1])
                        stringBuilder.append("Edad:"+String.valueOf(users.getEdad())+"-");
                    if (list[2])
                        stringBuilder.append("Direcc:"+users.getDireccion()+"-");
                    if (list[3])
                        stringBuilder.append("Afección:"+users.getAfecciones()+"-");
                    if (list[0]){
                        if(gpsLocator.canGetLocation())
                            stringBuilder.append(" http://maps.google.com/maps?q=loc:"+String.valueOf(gpsLocator.getLatitude())+","+String.valueOf(gpsLocator.getLongitude()));
                    }

                    for (Contacts contacts:users.getListContacts()){
                        sendSMS(contacts.getNumTel(), stringBuilder.toString());
                    }

                    if (!NavigationActivity.mediaPlayer.isPlaying()) {
                        NavigationActivity.mediaPlayer.start();
                    }

                    Handler mHandler=new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                        }
                    }, 8000);


                }
            }
        };
        users.removeAllChangeListeners();
        users.addChangeListener(listener);
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
