package qut.wearable_remake;

import com.microsoft.band.BandClient;

public interface SpecialEventListener {

    /**
     * Called after the device has connected to the Band.
     *
     * @param result The Band client returned from the ConnectAsync task.
     */
    void onConnectDone(BandClient result);

    /**
     * Called when the moveCount variable has been updated.
     *
     * @param moveCount The movement count.
     */
    void onMoveCountChanged(int moveCount);


    /**
     * Called when the accelerometer data has changed.
     *
     * @param accData The accelerometer data.
     * @param timestamp The timestamp that the accelerometer data was taken at.
     * @param orientation The current orientation (String) of the Band.
     */
    void onAccChanged(float accData, long timestamp, String orientation);
}