package com.project.rafael.kizunaapp.alarms;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Telephony;
import android.widget.Toast;

import com.project.rafael.kizunaapp.NavigationActivity;
import com.project.rafael.kizunaapp.database.SentenciasSQL;
import com.project.rafael.kizunaapp.objects.Users;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by RAFAEL on 28/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static Runnable myRunnable;
    public static final Handler handler = new Handler();
    static SentenciasSQL sentenciasSQL = new SentenciasSQL();

    Handler bluetoothIn;
    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    public static BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String address,user;
    private Users users;
    private int sign,cod,ppm,o2,ind1,ind2;
    private double temp;

    @Override
    public void onReceive(final Context context, Intent intent) {

        user=NavigationActivity.user;
        users=sentenciasSQL.ObtenerUsuario(user);

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        //checkBTState()
        if (btAdapter == null) {
            Toast.makeText(context, "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Toast.makeText(context, "Active el bluetooth por favor", Toast.LENGTH_LONG).show();
                //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(enableBtIntent, 1);
            }
        }
        //

        address=users.getMACdisp();
        if (address!=null){
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(context, "Socket creation failed", Toast.LENGTH_LONG).show();
            }

            try
            {
                btSocket.connect();
            } catch (IOException e) {
                try
                {
                    btSocket.close();
                } catch (IOException e2)
                {
                    //
                }
            }
        }




        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("~");
                    if (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        //setear
                        //int dataLength = dataInPrint.length();
                        //setear
                        if (recDataString.charAt(0) == 'S') {
                            ind1 = recDataString.indexOf("C");
                            sign = Integer.parseInt(recDataString.substring(1, ind1).toString());
                            cod = Integer.parseInt(recDataString.substring(ind1 + 1, ind1 + 5).toString());
                            if (cod != users.getCodDisp()) {
                                //Toast.makeText(context,"Codigo de dispositivo erróneo",Toast.LENGTH_SHORT).show();
                            } else{
                                ind2 = recDataString.indexOf("_");
                                if (ind2 != -1) {
                                    ind1 = recDataString.indexOf("_", ind2 + 1);
                                    temp = Double.parseDouble(recDataString.substring(ind2 + 1, ind1).toString());
                                    ind2 = recDataString.indexOf("_", ind1 + 1);
                                    ppm = Integer.parseInt(recDataString.substring(ind1 + 1, ind2).toString());
                                    if (recDataString.charAt(ind2 + 1) == '0')
                                        o2 = Integer.parseInt(recDataString.substring(ind2 + 1, ind2 + 2).toString());
                                    else
                                        o2 = Integer.parseInt(recDataString.substring(ind2 + 1, ind2 + 3).toString());
                                    sentenciasSQL.GuardarEstadoUser(user, sign, temp, ppm, o2, true);
                                } else {
                                    sentenciasSQL.GuardarEstadoUser(user, sign, 0, 0, 0, false);
                                }
                            }
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        dataInPrint = " ";
                    }
                }
            }
        };

        if (btSocket!=null){
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

            /////

            myRunnable = new Runnable() {
                @Override
                public void run() {
                    mConnectedThread.write("S");
                    handler.postDelayed(this, 100);
                }
            };
            handler.postDelayed(myRunnable, 100);
        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);

                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {


            }
        }
    }


}




