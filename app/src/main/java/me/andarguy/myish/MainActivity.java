package me.andarguy.myish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BluetoothHelper bluetoothHelper = new BluetoothHelper(this, this);
    DrawView canvasDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // bluetoothHelper.start();
        // bluetoothHelper.connect();

        canvasDraw = new DrawView(this, null);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
    }

    public class DrawView extends View {
        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            Bitmap cheeseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
            cheeseBitmap = Bitmap.createScaledBitmap(cheeseBitmap, getResources().getDimensionPixelSize(R.dimen.cheese_height), getResources().getDimensionPixelSize(R.dimen.cheese_width), false);
            canvas.drawBitmap(cheeseBitmap, 1f, 1f, paint);
            invalidate();
        }
    }


}


