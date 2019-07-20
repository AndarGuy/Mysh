package me.andarguy.myish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static DrawView canvasDraw;
    BluetoothHelper bluetoothHelper = new BluetoothHelper(this, this);
    TextView scoreView, speedView;
    FrameLayout gameAreaLayout;
    ImageView hider, energyIndicator, mouseView;
    LinearLayout blackScreen;
    EditText addressText;
    Thread game, animation;

    boolean animeState = true;

    Mouse mouse;
    EnergyIndicator indicator;
    Cheese cheese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // bluetoothHelper.start();
        // bluetoothHelper.connect();
        gameAreaLayout = findViewById(R.id.gameAreaLayout);
        scoreView = findViewById(R.id.score);
        speedView = findViewById(R.id.speed);
        energyIndicator = findViewById(R.id.energyBar);
        hider = findViewById(R.id.hider);
        mouseView = findViewById(R.id.mouseView);
        blackScreen = findViewById(R.id.BlackScreen);
        addressText = findViewById(R.id.bluetoothAddress);

        canvasDraw = new DrawView(this, null);

        gameAreaLayout.addView(canvasDraw);

        startGame();
    }

    public void CheckAddress(View view) {
        Editable address = addressText.getText();
        int countDots = 0;
        for (int i = 0; i < address.length(); ++i) {
            if (address.charAt(i) == ':') {
                countDots++;
            }
        }
        while (true) {
            if (countDots != 5 || address.length() != 17) {
                Log.e(TAG, "CheckAddress: error");
                Toast toast = Toast.makeText(this, "wrong input\n example: '00:11:22:33:44:55'", Toast.LENGTH_LONG);
                toast.show();
                break;
            }
            if (!bluetoothHelper.isActiv()) {
                Toast toast = Toast.makeText(this, "Bluetooth not active", Toast.LENGTH_SHORT);
                toast.show();
            }
            bluetoothHelper.setAddress(String.valueOf(address));
            bluetoothHelper.Connect();

            if (bluetoothHelper.isConnected()) {
                blackScreen.setVisibility(View.INVISIBLE);
                Thread game = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!mouse.isDied()) {
                            runOnUiThread(updateAll());
                            Log.d(TAG, mouse.toString());
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                });
                game.start();
                animation.start();
            }
            break;
        }
    }

    private void startGame() {
        mouse = new Mouse();
        indicator = new EnergyIndicator(hider, energyIndicator);
        cheese = Cheese.makeCheese(this);

        bluetoothHelper.start();

        game = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mouse.isDied()) {
                    runOnUiThread(updateAll());
                    Log.d(TAG, mouse.toString());
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });

        animation = new Thread(new Runnable() {
            @Override
            public void run() {
                final int MAX_ANIME_TIME = 1000, MIN_ANIME_TIME = 200;
                while (mouse.isDied()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeFrame();
                        }
                    });
                    try {
                        Thread.sleep(Float.valueOf((MAX_ANIME_TIME - MIN_ANIME_TIME) * (1 - (mouse.getSpeed() / Mouse.MAX_SPEED))).intValue());
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    private void changeFrame() {
        if (animeState) {
            mouseView.setImageDrawable(getDrawable(R.drawable.run2));
        } else {
            mouseView.setImageDrawable(getDrawable(R.drawable.run1));
        }
        animeState = !animeState;
    }

    Runnable updateAll() {
        return new Runnable() {
            @Override
            public void run() {
                mouse.update(100);
                scoreView.setText(String.valueOf(Float.valueOf(mouse.getScore()).intValue()));
                speedView.setText(String.valueOf(new DecimalFormat(".##").format(mouse.getSpeed())));
                indicator.update(mouse);
                if (cheese != null && cheese.isActive()) {
                    cheese.update(mouse, mouseView, MainActivity.this);
                    canvasDraw.invalidate();
                } else {
                    cheese = Cheese.makeCheese(MainActivity.this);
                }
            }
        };
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
            int cheeseOffset = 0;
            if (cheese != null) cheeseOffset = Float.valueOf(cheese.getX()).intValue();
            canvas.drawBitmap(cheeseBitmap, gameAreaLayout.getRight() - cheeseOffset, gameAreaLayout.getBottom() - cheeseBitmap.getHeight(), paint);
            invalidate();
        }
    }


}


