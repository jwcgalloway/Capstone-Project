package qut.wearable_project;

import android.annotation.SuppressLint;
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

/**
 * @author James Galloway
 * TODO This description
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

        /* Connect Button */
        Button connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectAsync connection = new ConnectAsync();
                connection.execute();
            }
        });

        /* Create Tile Button */
        Button createBtn = (Button) findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        projectClient.createTile();
                    }
                });
            }
        });

        /* Remove Tile Button */
        Button removeBtn = (Button) findViewById(R.id.removeBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        projectClient.removeTile();
                    }
                });
            }
        });

        /* Send Dialog Button */
        Button sendDiagBtn = (Button) findViewById(R.id.sendDiagBtn);
        sendDiagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        projectClient.sendDialog("Title", "Content - Ye boi");
                    }
                });
            }
        });

        /* Setup Pages Button */
        Button setupBtn = (Button) findViewById(R.id.setupBtn);
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        projectClient.setPageContent();
                    }
                });
            }
        });
    }

    /**
     * @author James Galloway
     * Private class which connects to the Microsoft Band the phone is paired with.
     */
    private class ConnectAsync extends AsyncTask<Void, Void, Boolean> {
        private String response;

        /**
         * Attempt to connect to band
         * @param params Void
         * @return True if successful, otherwise false
         */
        @Override
        protected Boolean doInBackground(Void...params) {
            BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();

            // Catch error if no paired bands were found
            BandClient bandClient;
            try {
                bandClient = BandClientManager.getInstance().create(MainActivity.this, pairedBands[0]);
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                response = "Could not find any Microsoft Bands paired with this device.";
                return false;
            }

            BandPendingResult<ConnectionState> pendingResult = bandClient.connect();

            try {
                ConnectionState state = pendingResult.await();
                if (state == ConnectionState.CONNECTED) {
                    projectClient = new ProjectClient(bandClient, MainActivity.this);
                    response = "Connected";
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
        }

        /**
         * Set toast text and show toast
         * @param connected True if connected, otherwise false
         */
        @Override
        protected void onPostExecute(Boolean connected) {
            statusTst.setText(response);
            statusTst.show();
        }
    } // end ConnectAsync
}
