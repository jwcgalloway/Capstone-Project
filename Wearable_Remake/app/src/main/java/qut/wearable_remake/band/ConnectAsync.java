package qut.wearable_remake.band;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;

import qut.wearable_remake.SpecialEventListener;
import qut.wearable_remake.WearableApplication;

/**
 * Class used solely to asynchronously connect the device to any paired Bands.
 */
public class ConnectAsync extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog connectDialog;
    private BandClient bandClient1;
    private BandClient bandClient2;
    private final Activity activity;
    private final SpecialEventListener listener;
    private final boolean isDualBands;

    public ConnectAsync(Activity a, SpecialEventListener l) {
        super();
        activity = a;
        listener = l;
        isDualBands = ((WearableApplication) a.getApplication()).isDualBands();
    }

    /**
     * Start the progress dialog while the connection takes place.
     */
    @Override
    protected void onPreExecute() {
        connectDialog = ProgressDialog.show(activity, "", "Connecting...", true);
    } // end onPreExecute()

    /**
     * Connect to either one or two Bands, depending on whether the dual Band option is selected.
     */
    @Override
    protected Boolean doInBackground(Void...params) {
        BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();

        bandClient1 = connectToBand(pairedBands, 0);
        if (isDualBands && pairedBands.length > 1) {
            bandClient2 = connectToBand(pairedBands, 1);
        }

        return bandClient1 != null || bandClient2 != null;
    } // end doInBackground()

    /**
     * Dismiss progress dialog upon completion and return the Band client through the callback.
     */
    @Override
    protected void onPostExecute(Boolean connected) {
        if (!connected) { // Unable to connect to any Band.
            Toast.makeText(activity, "Connection Failed.", Toast.LENGTH_LONG).show();
        } else if (isDualBands) {
            if (bandClient2 != null) { // Willing & able to connect to both Bands.
                listener.onConnectDone(new BandClient[]{bandClient1, bandClient2});
            } else { // Willing to connect to both Bands, able to connect to one.
                Toast.makeText(activity, "Could not connect to secondary Band.  Continuing with a single Band.", Toast.LENGTH_LONG).show();
                listener.onConnectDone(new BandClient[]{bandClient1});
            }
        } else { // Willing & able to connect to one Band.
            listener.onConnectDone(new BandClient[]{bandClient1});
        }
        connectDialog.dismiss();
    } // end onPostExecute()

    /**
     * Attempt to connect to a Band at a particular index of Bands paired with the device.
     */
    private BandClient connectToBand(BandInfo[] pairedBands, int bandIndex) {
        try {
            BandClient bandClient = BandClientManager.getInstance().create(activity, pairedBands[bandIndex]);
            BandPendingResult<ConnectionState> pendingResult = bandClient.connect();
            ConnectionState state = pendingResult.await();
            if (state == ConnectionState.CONNECTED) {
                return bandClient;
            }
        } catch (IndexOutOfBoundsException | InterruptedException | BandException e) {
            e.printStackTrace();
        }
        return null;
    } // end connectToBand()
} // end ConnectAsync
