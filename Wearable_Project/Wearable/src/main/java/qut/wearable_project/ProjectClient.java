package qut.wearable_project;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;

import java.util.UUID;

/**
 * @author James Galloway
 * TODO This description
 */
class ProjectClient {
    private final BandClient projectClient;
    private final UUID tileId;
    private final UUID page1Id;

    ProjectClient(BandClient c, UUID tId, UUID p1Id) {
        projectClient = c;
        tileId = tId;
        page1Id = p1Id;
    }

    /**
     * Removes the project tile from the Band.
     * @return True if the tile was successfully remove, otherwise false
     */
    boolean removeTile() {
        try {
            projectClient.getTileManager().removeTile(tileId).await();
            return true;
        } catch (BandException | InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a dialog to the Band.
     *
     * @param title The title of the dialog
     * @param msg The content of the dialog
     */
    void sendDialog(String title, String msg) {
        try {
            projectClient.getNotificationManager().showDialog(tileId, title, msg).await();
        } catch (InterruptedException | BandException ex) {
            ex.printStackTrace();
        }
    }
}
