package com.project.rafael.kizunaapp.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.project.rafael.kizunaapp.NavigationActivity;

import java.io.IOException;

/**
 * Created by RAFAEL on 29/03/2017.
 */

public class OnOff {
    public static void Off(Context context){

        AlarmReceiver.handler.removeCallbacks(AlarmReceiver.myRunnable);
        if (AlarmReceiver.btSocket!=null){
            try {
                AlarmReceiver.btSocket.close();
            } catch (IOException e2) {
            }
        }


        NavigationActivity.manager.cancel(NavigationActivity.pendingIntent);///
    }

    public static void On(Context context){
        NavigationActivity.manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), NavigationActivity.pendingIntent);
    }
}
