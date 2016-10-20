package qut.wearable_remake;

import android.app.Application;

@SuppressWarnings("WeakerAccess")
public class WearableApplication extends Application {
    private int moveGoal;
    private int totalMovesToday;
    private boolean dualBands = false;

    public int getTotalMovesToday() { return totalMovesToday; }
    public void setTotalMovesToday(int newCount) { totalMovesToday = newCount; }

    public int getMoveGoal() { return moveGoal; }
    public void setMoveGoal(int newGoal) { moveGoal = newGoal; }

    public void setDualBands(boolean db) { dualBands = db; }
    public boolean isDualBands() { return dualBands; }
}
