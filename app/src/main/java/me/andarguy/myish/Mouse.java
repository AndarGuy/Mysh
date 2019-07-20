package me.andarguy.myish;

public class Mouse {

    public final static int MIN_MUSCLE_FORCE = 0, MAX_MUSCLE_FORCE = 255;
    public final static float MIN_SPEED = 0f, MAX_SPEED = 4f, MIN_ENERGY = 0, MAX_ENERGY = 100f;
    private int muscleForce = 0;
    private float speed = 1.0f, score = 0, energy = 100f;
    private float scorePerUpdate = 1, energyConsumptionPerUpdate = 0.01f;
    private boolean isRunning = false, isDied = false;

    public Mouse() {
        this.energy = MAX_ENERGY;
        this.speed = MIN_SPEED;
        this.muscleForce = MIN_MUSCLE_FORCE;
    }

    private float getPercentFromForce(int force) {
        return muscleForce / (MAX_MUSCLE_FORCE - MIN_MUSCLE_FORCE);
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

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isDied() {
        return isDied;
    }

    // Обновления раз в 5 мс
    public boolean update(int muscleForce) {
        this.muscleForce = muscleForce;
        speed = (MAX_SPEED - MIN_SPEED) * getPercentFromForce(muscleForce);
        score = Float.valueOf(score + (scorePerUpdate * speed)).intValue();
        energy = energy - energyConsumptionPerUpdate;
        if (speed > 1) isRunning = true;
        isDied = energy < 0;

        return isDied;
    }

    @Override
    public String toString() {
        return "Mouse {Energy: " + getEnergy() + ", Speed: " + getSpeed() + ", Score: " + getScore() + "}";
    }
}
