package qut.wearable_remake;

import android.app.Application;

@SuppressWarnings("WeakerAccess")
public class WearableApplication extends Application {
    private int moveGoal;
    private int moveCount;

    public int getMoveCount() { return moveCount; }
    public void setMoveCount(int newCount) { moveCount = newCount; }

    public int getMoveGoal() { return moveGoal; }
    public void setMoveGoal(int newGoal) { moveGoal = newGoal; }
}
