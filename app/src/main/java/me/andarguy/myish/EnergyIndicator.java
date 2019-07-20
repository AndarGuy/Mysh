package me.andarguy.myish;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

public class EnergyIndicator {
    ImageView hider, fuelBar;

    private static final String TAG = "EnergyIndicator";


    public EnergyIndicator(ImageView hider, ImageView bar) {
        this.hider = hider;
        this.fuelBar = bar;
    }

    public void update(Mouse mouse) {
        int full = fuelBar.getWidth();
        int showingPart = Float.valueOf((mouse.getEnergy() / Mouse.MAX_ENERGY) * full).intValue();
        hider.setTranslationX(showingPart);
        Log.d(TAG, "update: " + hider.getTranslationX() + " " + full);
    }
}
