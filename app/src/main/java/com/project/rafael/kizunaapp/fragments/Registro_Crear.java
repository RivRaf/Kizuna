package com.project.rafael.kizunaapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Users;

/**
 * Created by RAFAEL on 10/03/2017.
 */

public class Registro_Crear extends android.support.v4.app.Fragment {
    private EditText etNombre, etApell,etCorr,etUserCre,etPassCre,etPassConf;
    private TextView flBtnCre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.registro_crear,container,false);
        etNombre=(EditText)convertView.findViewById(R.id.etNombre);
        etApell=(EditText)convertView.findViewById(R.id.etApell);
        etCorr=(EditText)convertView.findViewById(R.id.etCorr);
        etUserCre=(EditText)convertView.findViewById(R.id.etUserCre);
        etPassCre=(EditText)convertView.findViewById(R.id.etPassCre);
        etPassConf=(EditText)convertView.findViewById(R.id.etPassConf);
        flBtnCre=(TextView)convertView.findViewById(R.id.flBtnCre);


        flBtnCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Registrar el nuevo usuario
                if(etNombre.getText().length()==0 || etApell.getText().length()==0 || etCorr.getText().length()==0 ||etUserCre.getText().length()==0||etPassCre.getText().length()==0 || etPassConf.getText().length()==0){
                   Toast.makeText(getActivity(),"Llenar todos los campos",Toast.LENGTH_SHORT).show();
                    return;
                }

                SentenciasSQL sentenciasSQL=new SentenciasSQL();

                if(!etPassConf.getText().toString().equals(etPassCre.getText().toString())){
                    etPassConf.setError("Escriba la misma contraseña");
                    return;
                }

                if(sentenciasSQL.ValidarUserNameDisp(etUserCre.getText().toString())){
                    Users users= new Users();
                    users.setNombre(etNombre.getText().toString());
                    users.setApellido(etApell.getText().toString());
                    users.setCorreo(etCorr.getText().toString());
                    users.setUserName(etUserCre.getText().toString());
                    users.setPass(etPassCre.getText().toString());
                    users.setId(sentenciasSQL.NextUserID());

                    sentenciasSQL.RegistrarUser(users);

                    //limpiar los campos
                    etNombre.setText("");
                    etApell.setText("");
                    etCorr.setText("");
                    etUserCre.setText("");
                    etPassCre.setText("");
                    etPassConf.setText("");
                    //pedir que se logee
                    Toast.makeText(getActivity(),"Usuario Registrado, por favor logearse",Toast.LENGTH_LONG).show();
                    ViewPager vp = (ViewPager)getActivity().findViewById(R.id.vpSiSu);
                    vp.setCurrentItem(0);
                }else{
                    etUserCre.setError("El nombre de usuario ya existe");
                }
            }
        });

        return convertView;
    }

    public static Registro_Crear newInstance() {
        Registro_Crear f = new Registro_Crear();
        return f;
    }

}
