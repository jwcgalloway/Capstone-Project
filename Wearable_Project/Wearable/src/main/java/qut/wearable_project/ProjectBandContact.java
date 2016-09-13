package qut.wearable_project;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
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

    @Override
    public void setListener(Activity activity, TextView[] txtViews) {
        listener = new BandContactEventListener() {
            @Override
            public void onBandContactChanged(BandContactEvent bandContactEvent) {
                activity.runOnUiThread(new Runnable() {

                });
            }
        };
    }

    @Override
    public boolean registerListener(BandClient bandClient, SampleRate rate) {
        try {
            bandClient.getSensorManager().registerContactEventListener(listener, rate);
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
}
