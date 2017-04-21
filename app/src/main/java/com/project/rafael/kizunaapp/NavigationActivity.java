package com.project.rafael.kizunaapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.project.rafael.kizunaapp.alarms.AlarmReceiver;
import com.project.rafael.kizunaapp.alarms.SOSReceiver;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.fragments.Frag_Conf;
import com.project.rafael.kizunaapp.fragments.Frag_Cont;
import com.project.rafael.kizunaapp.fragments.Frag_Hist;
import com.project.rafael.kizunaapp.fragments.Frag_PagPrin;
import com.project.rafael.kizunaapp.objects.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fgtOverflow)
    FrameLayout fgtOverflow;
    @BindView(R.id.nav_view)//
    NavigationView navigationView;//
    private SentenciasSQL sentenciasSQL= new SentenciasSQL();
    private Users users;
    public static String user;//////
    public static PendingIntent pendingIntent;
    public static PendingIntent pendingIntent1;
    public static SimpleDraweeView imageView;
    public static TextView tvNomApll;
    public static TextView tvCorreo;
    public static Boolean listOpciones[]={false,false,false,false};
    public static MediaPlayer mediaPlayer;

    public static AlarmManager manager;/////
    public static AlarmManager manager2;/////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user=getIntent().getStringExtra("user");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Frag_PagPrin frag_pagPrin = new Frag_PagPrin();
        fragmentTransaction.replace(R.id.fgtOverflow, frag_pagPrin);
        fragmentTransaction.commit();
        users=sentenciasSQL.ObtenerUsuario(user);
        getSupportActionBar().setTitle(users.getNombre()+" "+users.getApellido());

        //
        Toast.makeText(NavigationActivity.this,"Bienvenido "+users.getUserName(),Toast.LENGTH_SHORT).show();

        View vw= navigationView.getHeaderView(0);
        tvNomApll= (TextView) vw.findViewById(R.id.tvNombreApell);
        tvCorreo=(TextView)vw.findViewById(R.id.tvCorreo);
        imageView= (SimpleDraweeView) vw.findViewById(R.id.imageView);
        if(users.getRutaImag()!=null)
            imageView.setImageURI(Uri.parse("file://"+users.getRutaImag()));
        if (users.getNombre()!=null&&users.getApellido()!=null)
            tvNomApll.setText(users.getNombre()+" "+users.getApellido());
        if(users.getCorreo()!=null)
            tvCorreo.setText(users.getCorreo());

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //
        Intent SOSintent=new Intent(NavigationActivity.this, SOSReceiver.class);
        pendingIntent1= PendingIntent.getBroadcast(NavigationActivity.this,0,SOSintent,0);
        manager2.cancel(pendingIntent1);
        manager2.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent1);
        //
        AlarmReceiver.handler.removeCallbacks(AlarmReceiver.myRunnable);
        if (AlarmReceiver.btSocket!=null){
            try {
                AlarmReceiver.btSocket.close();
            } catch (IOException e2) {
            }
        }
        Intent alarmIntent = new Intent(NavigationActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(NavigationActivity.this, 0, alarmIntent, 0);
        manager.cancel(pendingIntent);
        if(users.getCodDisp()>0) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
        //
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(NavigationActivity.this, R.raw.sos);
        }
        mediaPlayer.setLooping(true);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean estado;

        if(item.isChecked())
            estado=false;
        else
            estado=true;

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_ubicacion:
                listOpciones[0]=estado;
                break;
            case R.id.action_edad:
                listOpciones[1]=estado;
                break;
            case R.id.action_direccion:
                listOpciones[2]=estado;
                break;
            default:
                listOpciones[3]=estado;
                break;
        }
        item.setChecked(estado);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        navigationView.getMenu().setGroupEnabled(R.id.icon_group,true);
        int id = item.getItemId();

        if (id == R.id.pagPrin) {
            item.setEnabled(false);
            getSupportActionBar().setTitle(users.getNombre()+" "+users.getApellido());
            Frag_PagPrin frag_pagPrin = new Frag_PagPrin();
            fragmentTransaction.replace(R.id.fgtOverflow, frag_pagPrin);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.regHist) {
            item.setEnabled(false);
            getSupportActionBar().setTitle("Registro histórico");
            Frag_Hist frag_hist = new Frag_Hist();
            fragmentTransaction.replace(R.id.fgtOverflow, frag_hist);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.cont) {
            item.setEnabled(false);
            getSupportActionBar().setTitle("Contactos");

            Frag_Cont frag_cont = new Frag_Cont();
            fragmentTransaction.replace(R.id.fgtOverflow, frag_cont);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.config) {
            item.setEnabled(false);
            getSupportActionBar().setTitle("Mis detalles");

            Frag_Conf frag_conf = new Frag_Conf();
            fragmentTransaction.replace(R.id.fgtOverflow, frag_conf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationActivity.this);
            alertDialog.setTitle("Logging out...");
            alertDialog.setMessage("Está seguro de querer salir?");
            alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.setNeutralButton("Log out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ///AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    AlarmReceiver.handler.removeCallbacks(AlarmReceiver.myRunnable);
                    if(AlarmReceiver.btSocket!=null){
                        try {
                            AlarmReceiver.btSocket.close();
                        } catch (IOException e2) {
                        }
                    }

                    manager.cancel(pendingIntent);
                    //
                    manager2.cancel(pendingIntent1);
                    //
                    Toast.makeText(NavigationActivity.this, "Se detuvo la toma de datos", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NavigationActivity.this,RegistroActivity.class);
                    startActivity(intent);
                }
            });
            alertDialog.create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
