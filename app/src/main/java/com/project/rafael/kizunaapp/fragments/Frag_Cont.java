package com.project.rafael.kizunaapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rafael.kizunaapp.R;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Contacts;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by RAFAEL on 14/03/2017.
 */

public class Frag_Cont extends Fragment {
    private ListView lvContacts;
    private ImageButton btnAdd,btnDelete,btnDelete1;
    private ListView lvDisp;
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.cont_frag, container, false);
        lvContacts=(ListView) convertView.findViewById(R.id.lvContacts);
        btnAdd=(ImageButton)convertView.findViewById(R.id.btnAdd);
        btnDelete=(ImageButton)convertView.findViewById(R.id.btnDelete);
        btnDelete1=(ImageButton)convertView.findViewById(R.id.btnDelete1);

        final String user=getActivity().getIntent().getStringExtra("user");
        ArrayList<String> listContactos=new ArrayList<>();
        final SentenciasSQL sentenciasSQL= new SentenciasSQL();

        RealmList<Contacts> listaContactos= sentenciasSQL.ObtenerListaContacts(user);
        for(Contacts contacts:listaContactos){
            listContactos.add(contacts.getNombreContc()+"\n"+contacts.getNumTel());
        }
        final ArrayAdapter arrayAdapter= new ArrayAdapter(getActivity(),R.layout.device_name,listContactos);
        lvContacts.setAdapter(arrayAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog= new Dialog(getActivity());
                dialog.setTitle("Contactos disponibles");
                dialog.setContentView(R.layout.list_disp);
                lvDisp=(ListView)dialog.findViewById(R.id.lvDisp);

                CargarContactosTask cargarc= new CargarContactosTask(getActivity());
                cargarc.execute();


                lvDisp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tvDeviceName= (TextView) view.findViewById(R.id.tvDeviceName);
                        String inf = tvDeviceName.getText().toString();
                        int iend = inf.indexOf("\n");
                        if (iend != -1) {
                            String nombre = inf.substring(0,iend);
                            String number= inf.substring(iend+1,inf.length());
                            Contacts contacts= new Contacts();
                            contacts.setNombreContc(nombre);
                            contacts.setNumTel(number);
                            contacts.setIdContact(sentenciasSQL.ValidaryAgregarContactenRealm(contacts));

                            if(sentenciasSQL.ValidaryAgregarContactenUser(user,contacts)){
                                Toast.makeText(getActivity(),"Contacto Agregado",Toast.LENGTH_SHORT).show();
                                arrayAdapter.add(nombre+"\n"+number);
                            }else
                                Toast.makeText(getActivity(),"El contacto ya esta en la lista",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Seleccione los contactos para eliminarlos", Toast.LENGTH_SHORT).show();
                btnDelete.setVisibility(View.INVISIBLE);
                btnDelete1.setVisibility(View.VISIBLE);
            }
        });
        btnDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelete1.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(btnDelete.getVisibility()==View.VISIBLE){
                }else {
                    sentenciasSQL.EliminarContactenUser(user, position);
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                    Toast.makeText(getActivity(), "Contacto removido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    private class CargarContactosTask extends AsyncTask<Void,Void,ArrayAdapter<String>> {
        Context context;
        ProgressDialog progressDialog;
        ArrayAdapter arrayAdapter;

        public CargarContactosTask(Context context){
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cargando contactos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayAdapter<String> doInBackground(Void... params) {
            ArrayList<String> listac=new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String NAME = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                ContentResolver cr = context.getContentResolver();
                Cursor cursor1 = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                        "DISPLAY_NAME = '" + NAME + "'", null, null);
                if (cursor1.moveToFirst()) {
                    String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    while (phones.moveToNext()) {
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        listac.add(NAME+"\n"+number);
                        break;
                    }
                    phones.close();
                }
                cursor1.close();
            }
            cursor.close();
            arrayAdapter= new ArrayAdapter(context, R.layout.device_name,listac);
            return arrayAdapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
            super.onPostExecute(stringArrayAdapter);
            lvDisp.setAdapter(stringArrayAdapter);
            progressDialog.dismiss();
            dialog.show();
        }
    }



}
