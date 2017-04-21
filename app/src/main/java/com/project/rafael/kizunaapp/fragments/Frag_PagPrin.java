package com.project.rafael.kizunaapp.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.project.rafael.kizunaapp.NavigationActivity;
import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.alarms.OnOff;
import com.project.rafael.kizunaapp.alarms.SOSReceiver;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Users;

import io.realm.RealmChangeListener;

/**
 * Created by RAFAEL on 14/03/2017.
 */

public class Frag_PagPrin extends Fragment {
    private TextView btnPausa,btnRec,ppm,o2,temp;
    private SimpleDraweeView ivfoto;

    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 5d;
    RealmChangeListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView=LayoutInflater.from(getActivity()).inflate(R.layout.pagprin_frag,container,false);
        btnPausa=(TextView) convertView.findViewById(R.id.BtnPausa);
        btnRec=(TextView)convertView.findViewById(R.id.BtnRecon);
        ppm=(TextView)convertView.findViewById(R.id.etppm);
        o2=(TextView)convertView.findViewById(R.id.etO2);
        temp=(TextView)convertView.findViewById(R.id.etTemp);
        ivfoto=(SimpleDraweeView)convertView.findViewById(R.id.ivfoto);
        final SentenciasSQL sentenciasSQL=new SentenciasSQL();
        final OnOff onOff =new OnOff();
        final Users users= sentenciasSQL.ObtenerUsuario(getActivity().getIntent().getStringExtra("user"));


        GraphView graph2 = (GraphView) convertView.findViewById(R.id.graph2);
        mSeries2 = new LineGraphSeries<>();
        graph2.addSeries(mSeries2);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setBackgroundColor(getResources().getColor(R.color.color07));
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(40);
        graph2.getViewport().setMinY(0);
        graph2.getViewport().setMaxY(1500);

        if(users.getRutaImag()!=null)
            ivfoto.setImageURI(Uri.parse("file://"+users.getRutaImag()));

        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(users.getCodDisp()>0){
                    onOff.Off(getActivity());
                    //AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    //AlarmReceiver.handler.removeCallbacks(AlarmReceiver.myRunnable);
                    //manager.cancel(NavigationActivity.pendingIntent);
                    Toast.makeText(getActivity(), "Se detuvo la toma de datos", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Realizar la configuracion primero",Toast.LENGTH_SHORT).show();
            }
        });

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(users.getCodDisp()>0){
                    onOff.Off(getActivity());
                    Toast.makeText(getActivity(), "Reconectando...", Toast.LENGTH_SHORT).show();
                    onOff.On(getActivity());
                    NavigationActivity.mediaPlayer.pause();

                    NavigationActivity.manager2.cancel(NavigationActivity.pendingIntent1);
                    NavigationActivity.manager2.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),NavigationActivity.pendingIntent1);

                    users.removeAllChangeListeners();
                    users.addChangeListener(listener);

                }else{
                    Toast.makeText(getActivity(),"Realizar la configuracion primero",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ///
        listener=new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                int io2=users.getO2();
                if(io2==0){
                    ppm.setText("--Desconexion--");
                    o2.setText("--Desconexion--");
                }else {
                    o2.setText(String.valueOf(io2));
                    ppm.setText(String.valueOf(users.getPpm()));
                }
                temp.setText(String.valueOf(users.getTemp()));

                graph2LastXValue += 1d;
                mSeries2.appendData(new DataPoint(graph2LastXValue,users.getSignal()), true, 40);
            }
        };
        users.removeAllChangeListeners();
        users.addChangeListener(listener);
        ///

        return convertView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity PrincipalActivity;

        if (context instanceof Activity){
            PrincipalActivity=(Activity) context;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        /*mHandler.removeCallbacks(mTimer2);*/
        super.onPause();
    }

}
