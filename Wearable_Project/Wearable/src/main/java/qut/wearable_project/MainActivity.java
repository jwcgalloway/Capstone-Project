package qut.wearable_project;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
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
 *
 * TODO Project name
 * TODO This description
 * TODO User messages
 * TODO Persistence between values when app is run multiple times
 */
public class MainActivity extends AppCompatActivity {
    private ProjectClient projectClient;
    private Toast statusTst;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTst = Toast.makeText(MainActivity.this, null, Toast.LENGTH_LONG);

        /* Install Button */
        Button installBtn = (Button) findViewById(R.id.installBtn);
        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallAsync installation = new InstallAsync();
                installation.execute();
            }
        });

        /* Uninstall Button */
        Button uninstallBtn = (Button) findViewById(R.id.uninstallBtn);
        uninstallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response;
                if (projectClient.removeTile()) {
                    response = "Wearable Project tile has been successfully removed";
                } else {
                    response = "Could not remove Wearable Project tile";
                    projectClient.sendDialog("Uninstall", response);
                }
                statusTst.setText(response);
                statusTst.show();
            }
        });
    }

    enum TileLayoutIndex {
        MessagesLayout
    }

    enum TileMessagesPageElementId {
        Message
    }

    /**
     * @author James Galloway
     * Private class with functions required to install the Band application.
     *
     * TODO +1s after ordinal calls - enum
     */
    private class InstallAsync extends AsyncTask<Void, Void, Boolean> {
        private String response;
        BandClient bandClient;
        UUID tileId;
        UUID page1Id;

        /**
         * Installs the Band application by connecting to the device, creating a tile and
         * configuring the tile's pages.
         *
         * @param params Void.
         * @return True if the installation was successful, otherwise false.
         */
        @Override
        protected Boolean doInBackground(Void...params) {
            return connectToBand() && createTile() && setPageContent();
        } // end doInBackground

        /**
         * Initialise a ProjectClient if the installation was successful.  Set status toast
         * test and show toast.
         *
         * @param installed True if the application was successfully installed, otherwise false.
         */
        @Override
        protected void onPostExecute(Boolean installed) {
            if (installed) {
                projectClient = new ProjectClient(bandClient, tileId, page1Id);
                projectClient.sendDialog("Installation Successful", "And then some other message");
                response = "Installation Successful";
            }
            statusTst.setText(response);
            statusTst.show();
        } // end onPostExecute

        /**
         * Attempt to connect to Band.
         *
         * @return True if successful, otherwise false.
         */
        private boolean connectToBand() {
            BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();

            // Catch error if no paired Band(s) were found
            try {
                bandClient = BandClientManager.getInstance().create(MainActivity.this, pairedBands[0]);
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                response = "Could not find any Microsoft Bands paired with this device.";
                return false;
            }

            // Attempt to connect to Band
            BandPendingResult<ConnectionState> pendingResult = bandClient.connect();
            try {
                ConnectionState state = pendingResult.await();
                if (state == ConnectionState.CONNECTED) {
                    return true;
                } else {
                    response = "Connection Failed";
                    return false;
                }
            } catch (InterruptedException | BandException ex) {
                ex.printStackTrace();
                response = ex.getMessage();
                return false;
            }
        } // end connectToBand

        /**
         * Creates the project tile on the Band.
         *
         * @return True if the tile was successfully created, otherwise false.
         */
        private boolean createTile() {
            Bitmap smallIconBitmap = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
            BandIcon smallIcon = BandIcon.toBandIcon(smallIconBitmap);

            Bitmap largeIconBitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
            BandIcon largeIcon = BandIcon.toBandIcon(largeIconBitmap);

            tileId = UUID.randomUUID();
            PageLayout layout = createLayout();

            BandTile tile = new BandTile.Builder(tileId, "Wearable", largeIcon)
                    .setTileSmallIcon(smallIcon)
                    .setPageLayouts(layout)
                    .build();

            try {
                bandClient.getTileManager().addTile(MainActivity.this, tile).await();
                return true;
            } catch (InterruptedException | BandException ex) {
                ex.printStackTrace();
                response = ex.getMessage();
                return false;
            }
        } // end createTile

        /**
         * Creates the layout of the pages on the Band.
         *
         * @return Layout of the pages.
         */
        private PageLayout createLayout() {
            /* Scrollable vertical panel */
            ScrollFlowPanel panel = new ScrollFlowPanel(new PageRect(0, 0, 245, 102));
            panel.setFlowPanelOrientation(FlowPanelOrientation.VERTICAL);
            panel.setHorizontalAlignment(HorizontalAlignment.LEFT);
            panel.setVerticalAlignment(VerticalAlignment.TOP);

            /* Text block */
            WrappedTextBlock textBlock1 =
                    new WrappedTextBlock(new PageRect(0, 0, 245, 102), WrappedTextBlockFont.SMALL);
            textBlock1.setId(TileMessagesPageElementId.Message.ordinal() + 1);
            textBlock1.setMargins(new Margins(15, 0, 15, 0));
            textBlock1.setColor(Color.WHITE);
            textBlock1.setAutoHeightEnabled(true);
            textBlock1.setHorizontalAlignment(HorizontalAlignment.LEFT);
            textBlock1.setVerticalAlignment(VerticalAlignment.TOP);

            panel.addElements(textBlock1);
            return new PageLayout(panel);
        } // end createLayout

        /**
         * Sets the initial content of the pages on the Band.
         *
         * @return True if the page content was set successfully, otherwise false.
         */
        private boolean setPageContent() {
            page1Id = UUID.randomUUID();

            WrappedTextBlockData TBData =
                    new WrappedTextBlockData(TileMessagesPageElementId.Message.ordinal() + 1, "Ye Boi");

            PageData data =
                    new PageData(page1Id, TileLayoutIndex.MessagesLayout.ordinal()).update(TBData);

            try {
                bandClient.getTileManager().setPages(tileId, data).await();
                return true;
            } catch (InterruptedException | BandException ex) {
                ex.printStackTrace();
                response = ex.getMessage();
                return false;
            }
        } // end setPageContent

    } // end InstallAsync
}
