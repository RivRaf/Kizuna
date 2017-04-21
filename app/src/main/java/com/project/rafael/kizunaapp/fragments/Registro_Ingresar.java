package com.project.rafael.kizunaapp.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rafael.kizunaapp.NavigationActivity;
import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.alarms.GPSLocator;
import com.project.rafael.kizunaapp.database.SentenciasSQL;

/**
 * Created by RAFAEL on 10/03/2017.
 */

public class Registro_Ingresar extends android.support.v4.app.Fragment{
    private EditText etUserIng;
    private EditText etPassIng;
    private TextView flBtnIng;
    private GPSLocator gpsLocator;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View convertview= LayoutInflater.from(getActivity()).inflate(R.layout.registro_ingresar,container,false);
        etUserIng=(EditText)convertview.findViewById(R.id.etUserIng);
        etPassIng=(EditText) convertview.findViewById(R.id.etPassIng);
        flBtnIng=(TextView)convertview.findViewById(R.id.flBtnIng);
        //
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(getActivity(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBtAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        //
        gpsLocator=new GPSLocator(getActivity());
        if(!gpsLocator.canGetLocation())
            gpsLocator.showSettingsAlert();
        //
        flBtnIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobar en bd el usuario y contraseña
                SentenciasSQL sentenciasSQL = new SentenciasSQL();
                if(sentenciasSQL.ValidarUser(etUserIng.getText().toString(),etPassIng.getText().toString())){
                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("user",etUserIng.getText().toString());
                    startActivity(intent);
                }else{
                    etPassIng.setError("Nombre de usuario o contraseña incorrectos");
                }
            }
        });

        return convertview;
    }

    public static Registro_Ingresar newInstance() {
        Registro_Ingresar f = new Registro_Ingresar();
        return f;
    }

}
