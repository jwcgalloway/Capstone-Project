package qut.wearable_remake;

import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

class ProjectAccelerometer implements ProjectSensor {
    private final ProjectClient projectClient;
    private final BandAccelerometerEventListener listener;
    private boolean moving;
    private int offset;

    ProjectAccelerometer(final ProjectClient pc, final SpecialEventListener specialEvent) {
        projectClient = pc;
        moving = false;
        offset = 0;

        listener = new BandAccelerometerEventListener() {
            @Override
            public void onBandAccelerometerChanged(BandAccelerometerEvent bandEvent) {
                float x = bandEvent.getAccelerationX();
                float y = bandEvent.getAccelerationY();
                float z = bandEvent.getAccelerationZ();
                Log.d("X:", Float.toString(x));
                Log.d("Y:", Float.toString(y));
                Log.d("Z:", Float.toString(z));
                long time = bandEvent.getTimestamp();

                if (projectClient.getProjectContact().getWorn()) {
                    float[] accData = {x, y, z};
                    specialEvent.onAccChanged(accData, time);
                }

                // TODO 
                float sum = x + y + z;
                Log.d("Offset: ", Integer.toString(offset));
                Log.d("Sum: ", Float.toString(sum));
                if (sum + offset > 1.5 || sum + offset < -1.5 && !moving) {
                    moving = true;
                    projectClient.setMoveCount(projectClient.getMoveCount() + 1);
                } else {
                    moving = false;
                    if (z > 0.8) { // Flat Up
                        offset = 1;
                    } else if (y < -0.8) { // Left Tilt
                        offset = -1;
                    } else if (y > 0.8) { // Right Tilt
                        offset = 1;
                    } else if (z < -0.8) { // Flat Down
                        offset = -1;
                    }
                }
            }
        };
    }

    /**
     * Registers the sensor's event listener.
     *
     * @param bandClient The BandClient whose event listener is registered.
     */
    @Override
    public void registerListener(BandClient bandClient) {
        try {
            bandClient.getSensorManager().registerAccelerometerEventListener(listener, SampleRate.MS128);
        } catch (BandIOException ex) {
            ex.printStackTrace();
        }
    } // end registerListener()

    /**
     * Unregisters the sensor's event listener.
     *
     * @param bandClient The BandClient whose event listener is unregistered.
     */
    @Override
    public void unregisterListener(BandClient bandClient) {
        try {
            bandClient.getSensorManager().unregisterAccelerometerEventListener(listener);
        } catch (BandIOException ex) {
            ex.printStackTrace();
        }
    } // end unregisterListener()
}