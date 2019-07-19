package me.andarguy.myish;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.support.constraint.Constraints.TAG;

public class BluetoothHelper {

    private boolean isConnected=false;
    private Thread checkConnection = new Thread(new Runnable() {
        @Override
        public void run() {}
    });
    // ----------------------------------------------------
    private Context context;
    private Activity activity;
    private BluetoothAdapter myBluetoothAdapter;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // MAC-адрес Bluetooth модуля
    private static final String ADDRESS = "98:D3:31:F4:1D:14";

    public void start(){
        myBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        startBluetoothActivityCheck();
    }

    private volatile boolean isBluetoothActive = true;
    private void startBluetoothActivityCheck() {
        if (!checkConnection.isAlive()) {
            checkConnection = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (isBluetoothActive != myBluetoothAdapter.isEnabled()) {
                                isBluetoothActive = myBluetoothAdapter.isEnabled();
                                activity.runOnUiThread(bluetoothInformation);
                            }
                            checkConnection.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            checkConnection.start();
        }
    }

    Runnable bluetoothInformation = new Runnable() {
        @Override
        public void run() {
            Toast toast;
            if(isBluetoothActive){
                toast = Toast.makeText(context,"Bluetooth active", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(context,"Bluetooth not active", Toast.LENGTH_SHORT);
                if (isConnected) {
                    isConnected = false;
                    try {
                        if (outStream != null) {
                            outStream.flush();
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
            if(!isBluetoothActive) {return 2;}
            if(!isConnected){return 3;}
            return 4;
        }
    }

    public void connect(){
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
            if(myBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "- попытка соединения...");
                BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(ADDRESS);
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    if (myBluetoothAdapter.isDiscovering()) {
                        myBluetoothAdapter.cancelDiscovery();
                    }
                    Log.d(TAG, "...Соединяемся...");
                    btSocket.connect();
                    Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
                    outStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    Log.d("Fatal Error", "output stream creation failed:" + e.getMessage() + ".");
                }
                isConnected = true;
            }
            Toast toast;
            toast = Toast.makeText(context,"Connected", Toast.LENGTH_SHORT);
            toast.show();
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
