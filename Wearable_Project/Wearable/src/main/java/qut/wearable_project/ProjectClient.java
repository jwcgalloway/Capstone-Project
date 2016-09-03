package qut.wearable_project;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.tiles.BandIcon;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.pages.FlowPanelOrientation;
import com.microsoft.band.tiles.pages.HorizontalAlignment;
import com.microsoft.band.tiles.pages.Margins;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.PageRect;
import com.microsoft.band.tiles.pages.ScrollFlowPanel;
import com.microsoft.band.tiles.pages.VerticalAlignment;
import com.microsoft.band.tiles.pages.WrappedTextBlock;
import com.microsoft.band.tiles.pages.WrappedTextBlockData;
import com.microsoft.band.tiles.pages.WrappedTextBlockFont;

import java.util.UUID;

/**
 * @author James Galloway
 * TODO This description
 * TODO Exceptions & error checking
 * TODO +1s after ordinal calls - enum
 */
class ProjectClient {
    private final BandClient projectClient;
    private final UUID tileID;
    private final UUID page1;
    private final Activity activity;

    enum TileLayoutIndex {
        MessagesLayout
    }

    enum TileMessagesPageElementId {
        Message
    }

    ProjectClient(BandClient c, Activity a) {
        projectClient = c;
        activity = a;

        tileID = UUID.randomUUID();
        page1 = UUID.randomUUID();
    }

    /**
     * Creates the project tile on the band
     */
    void createTile() {
        Bitmap smallIconBitmap = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
        BandIcon smallIcon = BandIcon.toBandIcon(smallIconBitmap);

        Bitmap largeIconBitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
        BandIcon largeIcon = BandIcon.toBandIcon(largeIconBitmap);

        PageLayout layout = createLayout();

        BandTile tile = new BandTile.Builder(tileID, "Wearable", largeIcon)
                .setTileSmallIcon(smallIcon)
                .setPageLayouts(layout)
                .build();

        try {
            projectClient.getTileManager().addTile(activity, tile).await();
        } catch (InterruptedException | BandException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes the project tile from the band
     */
    void removeTile() {
        try {
            projectClient.getTileManager().removeTile(tileID).await();
        } catch (BandException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sends a dialog to the Band.
     * @param title The title of the dialog
     * @param msg The content of the dialog
     */
    void sendDialog(String title, String msg) {
        try {
            projectClient.getNotificationManager().showDialog(tileID, title, msg).await();
        } catch (InterruptedException | BandException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates the layout of the pages on the Band.
     * @return Layout of the pages
     */
    private PageLayout createLayout() {
        /* Scrollable vertical panel */
        ScrollFlowPanel panel = new ScrollFlowPanel(new PageRect(0, 0, 245, 102));
        panel.setFlowPanelOrientation(FlowPanelOrientation.VERTICAL);
        panel.setHorizontalAlignment(HorizontalAlignment.LEFT);
        panel.setVerticalAlignment(VerticalAlignment.TOP);

        /* Text block */
        WrappedTextBlock textBlock1 =
                new WrappedTextBlock(new PageRect(0, 0, 245, 102), WrappedTextBlockFont.MEDIUM);
        textBlock1.setId(TileMessagesPageElementId.Message.ordinal() + 1);
        textBlock1.setMargins(new Margins(15, 0, 15, 0));
        textBlock1.setColor(Color.WHITE);
        textBlock1.setAutoHeightEnabled(true);
        textBlock1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        textBlock1.setVerticalAlignment(VerticalAlignment.TOP);

        panel.addElements(textBlock1);
        return new PageLayout(panel);
    }

    /**
     * Sets the initial content of the pages on the Band
     */
    void setPageContent() {
        WrappedTextBlockData TBData =
                new WrappedTextBlockData(TileMessagesPageElementId.Message.ordinal() + 1, "Message One - Ye boi");

        PageData data =
                new PageData(page1, TileLayoutIndex.MessagesLayout.ordinal()).update(TBData);
        try {
           projectClient.getTileManager().setPages(tileID, data).await();
        } catch (InterruptedException | BandException ex) {
            ex.printStackTrace();
        }
    }
}
