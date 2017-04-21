package com.project.rafael.kizunaapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.RegHora;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.RealmList;

/**
 * Created by RAFAEL on 14/03/2017.
 */

public class Frag_Hist extends Fragment {
    private GraphView graphView;
    private CalendarView calendarv;
    private static SentenciasSQL sentenciasSQL=new SentenciasSQL();
    private static String user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.hist_frag,container,false);
        graphView=(GraphView)convertView.findViewById(R.id.graphview);
        calendarv=(CalendarView)convertView.findViewById(R.id.calendar);
        user=getActivity().getIntent().getStringExtra("user");

        Calendar calendar=Calendar.getInstance();
        int año=calendar.get(Calendar.YEAR);
        int mes=calendar.get(Calendar.MONTH);
        int dia=calendar.get(Calendar.DAY_OF_MONTH);
        Cargar(dia,mes,año);

        calendarv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                graphView.removeAllSeries();
                Cargar(dayOfMonth,month,year);
            }
        });
        return convertView;
    }

    private void Cargar(int dia,int mes, int año){
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>();
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>();

        series.setTitle("PPM");
        series.setColor(Color.rgb(17,176,33));
        series1.setTitle("%O2");
        series1.setColor(Color.rgb(55,106,247));
        series2.setTitle("Temp");
        series2.setColor(Color.rgb(247,183,55));

        RealmList<RegHora>lista=sentenciasSQL.ObtenerRegistrodelDia(user,dia,mes,año);
        if(lista!=null){
            for(RegHora regHora:lista){
                int hora = regHora.getHora();
                series.appendData(new DataPoint(hora,regHora.getSumPPM()/regHora.getContPPM()),true,72);
                series1.appendData(new DataPoint(hora+0.3,regHora.getSumO2()/regHora.getContO2()),true,72);
                series2.appendData(new DataPoint(hora+0.6,regHora.getSumTemp()/regHora.getContTemp()),true,72);
            }

        }else {
            Toast.makeText(getActivity(),"No hay datos disponibles",Toast.LENGTH_SHORT).show();
        }

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.GREEN);

        series1.setDrawValuesOnTop(true);
        series1.setValuesOnTopColor(Color.BLUE);

        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.rgb(247,183,55));

        graphView.addSeries(series);
        graphView.addSeries(series1);
        graphView.addSeries(series2);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMaxY(150);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}
