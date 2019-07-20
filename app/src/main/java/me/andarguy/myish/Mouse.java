package me.andarguy.myish;

import android.support.annotation.NonNull;

public class Mouse {

    private final static int MIN_MUSCLE_FORCE = 0, MAX_MUSCLE_FORCE = 255;
    private final static float MIN_SPEED = 0f;
    public final static float MAX_SPEED = 4f;
    private final static float MIN_ENERGY = 0;
    public final static float MAX_ENERGY = 100f;
    private int muscleForce = 0;
    private float speed = 1.0f, score = 0, energy = 100f;
    private float scorePerUpdate, energyConsumptionPerUpdate;
    private boolean isRunning = false, isDied = false;

    Mouse() {
        this.energy = MAX_ENERGY;
        this.speed = MIN_SPEED;
        this.muscleForce = MIN_MUSCLE_FORCE;
        scorePerUpdate = 1;
        energyConsumptionPerUpdate = 0.05f;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    float getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void eatCheese() {
        this.energy = MAX_ENERGY;
    }

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    boolean isDied() {
        return !isDied;
    }

    // Обновления раз в 5 мс
    void update(int muscleForce) {
        this.muscleForce = muscleForce;
        this.speed = (MAX_SPEED - MIN_SPEED) * (Integer.valueOf(muscleForce).floatValue() / (MAX_MUSCLE_FORCE - MIN_MUSCLE_FORCE));
        this.score = Float.valueOf(score + (scorePerUpdate * speed)).intValue();
        energy = energy - energyConsumptionPerUpdate;
        if (speed > 1) isRunning = true;
        isDied = energy < 0;

    }

    @NonNull
    @Override
    public String toString() {
        return "Mouse {Energy: " + getEnergy() + ", Speed: " + getSpeed() + ", Score: " + getScore() + "}";
    }
}
