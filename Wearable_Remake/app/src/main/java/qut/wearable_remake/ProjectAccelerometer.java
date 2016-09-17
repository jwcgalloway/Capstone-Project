package qut.wearable_remake;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

class ProjectAccelerometer implements ProjectSensor {
    private final ProjectClient projectClient;
    private final BandAccelerometerEventListener listener;
    private static final long BOUNCE_TIME = 750;
    private long lastActivated;
    private boolean moving;
    private float offset;

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
                long time = bandEvent.getTimestamp();

              //  if (projectClient.getProjectContact().getWorn()) {
                    float[] accData = {x, y, z};
                    specialEvent.onAccChanged(accData, time);
               // }

                float sum = x + y + z;
                if ((sum - offset > 0.3 || sum - offset < -0.3) && !moving
                        && bandEvent.getTimestamp() > lastActivated + BOUNCE_TIME) {
                    lastActivated = bandEvent.getTimestamp();

                    moving = true;
                    projectClient.setMoveCount(projectClient.getMoveCount() + 1);
                } else {
                    moving = false;
                    offset = sum;
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