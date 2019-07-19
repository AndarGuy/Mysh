package me.andarguy.myish;

public class Mouse {

    public final static int MIN_MUSCLE_FORCE = 0, MAX_MUSCLE_FORCE = 255;
    public final static float MIN_SPEED = 0f, MAX_SPEED = 4f;
    private int energy = 100, score = 0, muscleForce = 0;
    private float speed = 1.0f, scorePerUpdate = 1;
    private boolean isRunning = false;

    public Mouse() {

    }

    private float getPercentFromForce(int force) {
        return muscleForce / (MAX_MUSCLE_FORCE - MIN_MUSCLE_FORCE);
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void update(int muscleForce) {
        if (speed > 1) isRunning = true;
        this.muscleForce = muscleForce;
        speed = (MAX_SPEED - MIN_SPEED) * getPercentFromForce(muscleForce);
        score = Float.valueOf(score + (scorePerUpdate * speed)).intValue();
    }
}
