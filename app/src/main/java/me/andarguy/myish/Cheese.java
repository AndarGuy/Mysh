package me.andarguy.myish;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

public class Cheese {

    private static final String TAG = "Cheese";

    public static float cheeseChance = 0.1f;
    private float x;
    private boolean isActive;

    private Cheese() {
        this.x = 0;
        isActive = true;
    }

    public float getX() {
        return x;
    }

    public boolean isActive() {
        return isActive;
    }

    static Cheese makeCheese() {
        Random random = new Random();
        float c = random.nextFloat();
        Log.d(TAG, "Trying to make cheese. Random value is " + c + ' ' + (c < cheeseChance));
        if (c < cheeseChance) return new Cheese();
        return null;
    }

    void update(Mouse mouse, Activity activity) {
        x = x + mouse.getSpeed();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        MainActivity.canvasDraw.invalidate();

        if (x > displayMetrics.widthPixels) isActive = false;
    }
}
