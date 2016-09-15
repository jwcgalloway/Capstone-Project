package qut.wearable_remake;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

class ProjectAccelerometer implements ProjectSensor {
    private final ProjectClient projectClient;
    private final BandAccelerometerEventListener listener;
    private float movePeak;

    ProjectAccelerometer(final ProjectClient pc, final SpecialEventListener v) {
        projectClient = pc;
        movePeak = 0;

        listener = new BandAccelerometerEventListener() {
            @Override
            public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
                float x = event.getAccelerationX();
                float y = event.getAccelerationY();
                float z = event.getAccelerationZ();
                long time = event.getTimestamp();

                if (projectClient.getProjectContact().getWorn()) {
                    float[] accData = {x, y, z};
                    v.onAccChanged(accData, time);
                }

                // TODO Get good math
                if (x + y + z > movePeak + 1 || x + y + z < movePeak - 1) {
                    movePeak = x + y + z;
                    projectClient.setMoveCount(projectClient.getMoveCount() + 1);
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
            bandClient.getSensorManager().registerAccelerometerEventListener(listener, SampleRate.MS16);
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