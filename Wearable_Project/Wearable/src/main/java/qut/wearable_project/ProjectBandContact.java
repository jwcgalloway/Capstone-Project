package qut.wearable_project;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandContactState;
import com.microsoft.band.sensors.SampleRate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Lok Sum (Moon) Lo on 9/12/2016.
 *
 * Class for band contact state.
 */
class ProjectBandContact implements ProjectSensorInterface{
    private BandContactEventListener listener;
    private boolean worn;

    public void setListener(final Activity activity, final TextView txtViews) {
        listener = new BandContactEventListener() {
            @Override
            public void onBandContactChanged(BandContactEvent bandContactEvent) {

                if (bandContactEvent.getContactState() == BandContactState.WORN){
                    worn = true;
                } else {
                    worn = false;
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(worn){
                            txtViews.setText("Worn");
                        } else {
                            txtViews.setText("Not Worn");
                        }
                    }
                });
            }
        };
    }

    @Override
    public void setListener(Activity activity, TextView[] txtViews) {

    }

    @Override
    public boolean registerListener(BandClient bandClient, SampleRate rate) {
        try {
            bandClient.getSensorManager().registerContactEventListener(listener);
            return true;
        } catch (BandIOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unregisterListener(BandClient bandClient) {
        try {
            bandClient.getSensorManager().unregisterContactEventListener(listener);
            return true;
        } catch (BandIOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getWorn(){ return worn; }
}
