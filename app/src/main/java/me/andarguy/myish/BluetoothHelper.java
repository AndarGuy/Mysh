package me.andarguy.myish;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.support.constraint.Constraints.TAG;

public class BluetoothHelper {

    private boolean isConnected=false;
    private Thread CheckConnection = new Thread(new Runnable() {
        @Override
        public void run() {}
    });

    private Thread Reciver = new Thread(new Runnable() {
        @Override
        public void run() {}
    });
    // ----------------------------------------------------
    private Context context;
    private Activity activity;
    private BluetoothAdapter myBluetoothAdapter;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private int Value = -1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-адрес Bluetooth модуля
    private static String address = "98:D3:31:F4:1D:14";

    public void start(){
        myBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        startBluetoothActivityCheck();
        startReciver();
    }

    public void setAddress(String address){
        this.address = address;
    }

    public boolean isActiv(){
        return BluetoothActiv;
    }

    private volatile boolean BluetoothActiv = true;
    private void startBluetoothActivityCheck() {
        if (!CheckConnection.isAlive()) {
            CheckConnection = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (BluetoothActiv != myBluetoothAdapter.isEnabled()) {
                                BluetoothActiv = myBluetoothAdapter.isEnabled();
                                activity.runOnUiThread(bluetoothInformation);
                            }
                            CheckConnection.sleep(200);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            CheckConnection.start();
        }
    }

    private void startReciver() {
        if (!Reciver.isAlive()) {
            Reciver = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (isConnected) {
                                int Count_byte = 0;//inStream.available();
                                if(Count_byte > 0 ){
                                    inStream.skip(Count_byte-1);
                                    Value = inStream.read();
                                } else {
                                    Value = -1;
                                }
                            }
                            CheckConnection.sleep(2);
                        } catch (InterruptedException e) {
                        } catch (IOException e) {
                        }
                    }
                }
            });
            Reciver.start();
        }
    }

    Runnable bluetoothInformation = new Runnable() {
        @Override
        public void run() {
            Toast toast;
            if(BluetoothActiv){
                toast = Toast.makeText(context,"Bluetooth active", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(context,"Bluetooth not active", Toast.LENGTH_SHORT);
                if (isConnected) {
                    isConnected = false;
                    try {
                        if (outStream != null) {
                            outStream.flush();
                            outStream.close();
                        }
                        if (inStream != null) {
                            inStream.close();
                        }
                        btSocket.close();
                    } catch (IOException e2) {
                    }
                }
            }
            toast.show();
        }
    };


    public int sendData(String message) {
        if(isConnected) {
            Log.d(TAG, "...Посылаем данные: " + message + "...");
            try {
                outStream.write(message.getBytes());
            } catch (IOException e) {
                Log.d("Fatal Error", "In onResume() and an exception occurred during write: " + e.getMessage());
                return 1;
            }
            return 0;
        } else {
            if(!BluetoothActiv) {return 2;}
            if(!isConnected){return 3;}
            return 4;
        }
    }

    public int getValue(){
        return Value;
    }

    public void Connect(){
        if(isConnected) {
            try     {
                if (outStream != null) {
                    outStream.flush();
                }
                btSocket.close();
            } catch (IOException e2) {}
            isConnected=false;
            Toast toast;
            toast = Toast.makeText(context,"Disconnected", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            boolean Connect = true;
            if(myBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "- попытка соединения...");
                BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(address);
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    if (myBluetoothAdapter.isDiscovering()) {
                        myBluetoothAdapter.cancelDiscovery();
                    }
                    Log.d(TAG, "...Соединяемся...");
                    btSocket.connect();
                    Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
                    outStream = btSocket.getOutputStream();
                    inStream = btSocket.getInputStream();
                } catch (IOException e) {
                    Log.d("Fatal Error", "output stream creation failed:" + e.getMessage() + ".");
                    Connect = false;
                }
                isConnected = true;
            }
            if(!Connect) {
                try     {
                    if (outStream != null) {
                        outStream.flush();
                    }
                    btSocket.close();
                } catch (IOException e2) {}
                isConnected=false;
                Toast toast;
                toast = Toast.makeText(context, "Error: Not Connected", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast;
                toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public boolean isConnected(){
        return isConnected;
    }

    public BluetoothHelper(Context thisContext, Activity thisActivity){
        context = thisContext;
        activity = thisActivity;
    }

}
