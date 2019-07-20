package me.andarguy.myish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;

public class Cheese {

    private static final String TAG = "Cheese";

    public static float cheeseChance = 1f / 300f;
    private static final int SOME_OFFSET = 64;
    private float x;
    private boolean isActive;
    private Bitmap cheeseImage;

    private Cheese(Context context) {
        this.x = 0;
        isActive = true;
        cheeseImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.cheese);
    }

    public float getX() {
        return x;
    }

    public boolean isActive() {
        return isActive;
    }

    static Cheese makeCheese(Context context) {
        Random random = new Random();
        float c = random.nextFloat();
        Log.d(TAG, "Trying to make cheese. Random value is " + c + ' ' + (c < cheeseChance));
        if (c < cheeseChance) return new Cheese(context);
        return null;
    }

    private boolean isMouseEatingCheese(Mouse mouse, ImageView mouseView, Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int distance = Math.abs((displayMetrics.widthPixels / 2) - (Float.valueOf(x).intValue()));
        Log.d(TAG, "isMouseEatingCheese: " + distance);
        return distance < 150 && mouse.getSpeed() < 10f;
    }

    void update(Mouse mouse, ImageView mouseView, Activity activity) {
        x = x + mouse.getSpeed();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        MainActivity.canvasDraw.invalidate();

        Log.d(TAG, "update: " + x);

        if (x - cheeseImage.getWidth() > displayMetrics.widthPixels) isActive = false;
        else if (isMouseEatingCheese(mouse, mouseView, activity)) {
            isActive = false;
            mouse.eatCheese();
        }
    }
}
