package qut.wearable_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Public class used to implement the onReceive function.  This function is used to deal
 * with events broadcast from the Band to the user's device.
 */
public class TileEventForwarder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "com.microsoft.band.action.ACTION_TILE_BUTTON_PRESSED") {
            Intent intentForward = new Intent();
            intentForward.setAction("BUTTON_PRESSED_FORWARD");
            context.sendBroadcast(intentForward);
        }
    } // end onReceive
}
