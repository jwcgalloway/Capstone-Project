package qut.wearable_remake;

import android.app.Application;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class WearableApplication extends Application {
    private int moveGoal;
    private int totalMoveCount;
    private Map<Date, Integer> moveCountMap = new HashMap<>();

    public Map<Date, Integer> getMoveCountMap() { return moveCountMap; }
    public void updateMoveCounts(Date key, Integer val) {
        moveCountMap.put(key, val);
    }

    public int getTotalMoveCount() { return totalMoveCount; }
    public void setTotalMoveCount(int newCount) { totalMoveCount = newCount; }

    public int getMoveGoal() { return moveGoal; }
    public void setMoveGoal(int newGoal) { moveGoal = newGoal; }
}
