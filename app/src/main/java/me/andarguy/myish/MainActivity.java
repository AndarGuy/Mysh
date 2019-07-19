package me.andarguy.myish;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    BluetoothHelper bluetoothHelper = new BluetoothHelper(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothHelper.start();
        bluetoothHelper.connect();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
    }

    public class DrawView extends View {
        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(2);
            invalidate();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!CheckConnection.isAlive()) {
            CheckConnection = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (BluetoothActiv != myBluetoothAdapter.isEnabled()) {
                                BluetoothActiv = myBluetoothAdapter.isEnabled();
                                runOnUiThread(BlueToothNotActive);
                            }
                            CheckConnection.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            CheckConnection.start();
        }
    }

    private void sendData(String message) {
        TextInfo.setText(message);
        Log.d(TAG, "...Посылаем данные: " + message + "...");
        try {
            outStream.write(message.getBytes());
        } catch (IOException e) {
            Log.d("Fatal Error", "In onResume() and an exception occurred during write: " + e.getMessage());
        }
    }

    public void onReverse(View view){
        Reverse = !Reverse;
        Toast toast;
        if(Reverse){
            toast = Toast.makeText(getApplicationContext(),"Reverse ON", Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getApplicationContext(),"Reverse OFF", Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void onSlow_Fast(View view){
        Speed = !Speed;
        Toast toast;
        if(Speed){
            button_speed.setText("Slow");
            toast = Toast.makeText(getApplicationContext(),"Slow", Toast.LENGTH_SHORT);
        } else {
            button_speed.setText("Fast");
            toast = Toast.makeText(getApplicationContext(),"Fast", Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void Connect(View view){
        if(isConnected) {
            TextInfo.setText("Disconnecting...");
            Connect.setText("connect");
            try     {
                if (outStream != null) {
                    outStream.flush();
                }
                btSocket.close();
            } catch (IOException e2) {}
            isConnected=false;
            TextInfo.setText("Disconnected");
        } else {
            if(myBluetoothAdapter.isEnabled()) {
                TextInfo.setText("Connecting...");
                Connect.setText("Disconnect");
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
                } catch (IOException e) {
                    Log.d("Fatal Error", "output stream creation failed:" + e.getMessage() + ".");
                }
                TextInfo.setText("Connected");
                isConnected = true;
            }
        }
    }
}


}


