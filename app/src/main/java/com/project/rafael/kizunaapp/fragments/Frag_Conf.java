package com.project.rafael.kizunaapp.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.project.rafael.kizunaapp.NavigationActivity;
import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.adapters.ImageFilePath;
import com.project.rafael.kizunaapp.alarms.AlarmReceiver;
import com.project.rafael.kizunaapp.alarms.OnOff;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Users;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import io.realm.Realm;

/**
 * Created by RAFAEL on 14/03/2017.
 */

public class Frag_Conf extends Fragment {
    private EditText etDir,etAfec,etOcup,etCod,etNombre,etApell,etCorr;
    private Spinner spEdad;
    private TextView btnConectar,btnSave,btnTresP,tvRuta;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private SimpleDraweeView sdvFoto;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView= LayoutInflater.from(getActivity()).inflate(R.layout.conf_frag,container,false);
        spEdad=(Spinner) convertView.findViewById(R.id.spEdad);
        etDir=(EditText) convertView.findViewById(R.id.etDir);
        etAfec=(EditText) convertView.findViewById(R.id.etAfec);
        etOcup=(EditText) convertView.findViewById(R.id.etOcup);
        etCod=(EditText) convertView.findViewById(R.id.etCod);
        etNombre=(EditText) convertView.findViewById(R.id.etNombre);
        etApell=(EditText) convertView.findViewById(R.id.etApell);
        etCorr=(EditText)convertView.findViewById(R.id.etCorr);

        btnConectar=(TextView) convertView.findViewById(R.id.btnConectar);
        btnSave=(TextView) convertView.findViewById(R.id.btnSave);
        btnTresP=(TextView)convertView.findViewById(R.id.btnTresP);
        tvRuta=(TextView)convertView.findViewById(R.id.tvRuta);
        sdvFoto=(SimpleDraweeView)convertView.findViewById(R.id.sdvFoto);

        final String user=getActivity().getIntent().getStringExtra("user");
        final SentenciasSQL sentenciasSQL=new SentenciasSQL();
        final Users users= sentenciasSQL.ObtenerUsuario(user);
        final OnOff onOff=new OnOff();

        ArrayList<String> Edades= new ArrayList<>();
        Edades.add("  ");
        for (int i=6;i<=120;i++){
            Edades.add(String.valueOf(i));
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,Edades);
        spEdad.setAdapter(arrayAdapter);

        if(users.getEdad()!=0){
            int i=1;
            for (;users.getEdad()!=Integer.parseInt(spEdad.getItemAtPosition(i).toString());i++){
            }
            spEdad.setSelection(i);
        }


        etDir.setText(users.getDireccion());
        etAfec.setText(users.getAfecciones());
        etOcup.setText(users.getOcupacion());
        if (users.getNombre()!=null)
            etNombre.setText(users.getNombre());
        if (users.getApellido()!=null)
            etApell.setText(users.getApellido());
        if (users.getCorreo()!=null)
            etCorr.setText(users.getCorreo());
        if(users.getCodDisp()!=0)
        etCod.setText(String.valueOf(users.getCodDisp()));
        tvRuta.setText(users.getRutaImag());
        if(users.getRutaImag()!=null){
            tvRuta.setText(users.getRutaImag());
            sdvFoto.setImageURI(Uri.parse("file://"+users.getRutaImag()));
        }



        btnConectar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(users.getCodDisp()>0) {
                        if (Integer.parseInt(etCod.getText().toString())!=(users.getCodDisp())){
                            Toast.makeText(getActivity(),"Guargar antes de solicitar la conexion",Toast.LENGTH_SHORT).show();
                        }else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setTitle("Dispositivos disponibles");
                            dialog.setContentView(R.layout.list_disp);
                            ListView lvDisp = (ListView) dialog.findViewById(R.id.lvDisp);

                            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBtAdapter == null) {
                                Toast.makeText(getActivity(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                if (!mBtAdapter.isEnabled()) {
                                    //Prompt user to turn on Bluetooth
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, 1);
                                }
                            }

                            mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);
                            mPairedDevicesArrayAdapter.clear();// clears the array so items aren't duplicated when resuming from onPause
                            // Get a set of currently paired devices and append to pairedDevices list
                            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                            // Add previously paired devices to the array
                            if (pairedDevices.size() > 0) {
                                for (BluetoothDevice device : pairedDevices) {
                                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                }
                            } else {
                                mPairedDevicesArrayAdapter.add("No hay dispositivos emparejados");
                                dialog.dismiss();
                            }
                            lvDisp.setAdapter(mPairedDevicesArrayAdapter);

                            lvDisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if(users.getMACdisp()!=null)
                                        onOff.Off(getActivity());

                                    Toast.makeText(getActivity(), "Conectando...", Toast.LENGTH_SHORT).show();
                                    String info = ((TextView) view).getText().toString();
                                    String MAC = info.substring(info.length() - 17);
                                    // enviar la direccion MAC
                                    sentenciasSQL.DefinirMAC(user,MAC);
                                    onOff.On(getActivity());
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                }else
                        Toast.makeText(getActivity(),"Necesario guardar datos validos primero",Toast.LENGTH_SHORT).show();
                }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spEdad.getSelectedItem().toString().equals("  ")||etCod.getText().toString().length()==0||etNombre.getText().toString().length()==0||etApell.getText().toString().length()==0||etCorr.getText().toString().length()==0){
                    etCod.setError("Necesario");
                    etNombre.setError("Necesario");
                    etApell.setError("Necesario");
                    etCorr.setError("Necesario");
                    Toast.makeText(getActivity(),"Llenar al menos estos campos y la edad",Toast.LENGTH_SHORT).show();
                    return;
                }
                sentenciasSQL.ActualizarUsuario(user,Integer.parseInt(spEdad.getSelectedItem().toString()),etDir.getText().toString(),etAfec.getText().toString(),etOcup.getText().toString(),Integer.parseInt(etCod.getText().toString()),tvRuta.getText().toString(),etNombre.getText().toString(),etApell.getText().toString(),etCorr.getText().toString());
                NavigationActivity.imageView.setImageURI(Uri.parse("file://"+tvRuta.getText().toString()));
                NavigationActivity.tvNomApll.setText(etNombre.getText().toString()+" "+etApell.getText().toString());
                NavigationActivity.tvCorreo.setText(etCorr.getText().toString());
                Toast.makeText(getActivity(),"Datos guardados",Toast.LENGTH_SHORT).show();
            }
        });

        btnTresP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });


        return convertView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    InputStream inputStream =
                            getActivity().getContentResolver().openInputStream(data.getData());
                    sdvFoto.setImageBitmap(BitmapFactory.decodeStream(inputStream));

                    tvRuta.setText(ImageFilePath.getPath(getActivity().getApplicationContext(), data.getData()));
                } catch (Exception e) {
                }
            }
        }
    }

}
