package qut.wearable_remake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Switch;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;

import com.microsoft.band.BandClient;

import java.io.IOException;

import qut.wearable_remake.band.ConnectAsync;
import qut.wearable_remake.band.ProjectClient;
import qut.wearable_remake.band.Setup;
import qut.wearable_remake.graphs.AccLineGraph;
import qut.wearable_remake.graphs.DailyMovesBullet;
import qut.wearable_remake.graphs.HourlyMovesBar;
import qut.wearable_remake.sensors.SensorInterface;

public class MainActivity extends AppCompatActivity implements SpecialEventListener {
    private static final long GRAPH_REFRESH_TIME = 1000;

    private View progressClock;
    private Switch liveGraphingSwitch;
    private Switch sendHapticsSwitch;

    private ProjectClient projectClient;
    private AccLineGraph accLineGraph;
    private HourlyMovesBar hourlyMovesBar;

    private long lastRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectAsync(MainActivity.this, this).execute();

        progressClock = findViewById(R.id.progressClock);

        final EditText moveGoalEditTxt = (EditText) findViewById(R.id.moveGoalEditTxt);

        ((WearableApplication) this.getApplication())
                .setMoveGoal(Integer.parseInt(moveGoalEditTxt.getText().toString()));

        Button moveGoalBtn = (Button) findViewById(R.id.moveGoalBtn);
        moveGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WearableApplication) getApplication())
                        .setMoveGoal(Integer.parseInt(moveGoalEditTxt.getText().toString()));
            }
        });

        Switch recordDataSwitch = (Switch) findViewById(R.id.recordDataSwitch);
        recordDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BandClient bandClient = projectClient.getBandClient();
                SensorInterface[] sensors = projectClient.getSensors();
                if (isChecked) {
                    for (SensorInterface sensor : sensors) {
                        sensor.registerListener(bandClient);
                    }
                } else {
                    for (SensorInterface sensor : sensors) {
                        sensor.unregisterListener(bandClient);
                    }
                }
            }
        });

        liveGraphingSwitch = (Switch) findViewById(R.id.liveGraphSwitch);
        sendHapticsSwitch = (Switch) findViewById(R.id.sendHapticsSwitch);

        //testing uuid save function
        TextView uuid = (TextView) findViewById(R.id.uuid_text);

        String uuid_str = "default";
        try {
            uuid_str = HelperMethods.getDataFromFile("app_id", MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        uuid.setText(uuid_str);
        //end testing

        Button removeTileBtn = (Button) findViewById(R.id.removeTileBtn);
        removeTileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectClient.removeTile();
            }
        });
    }

    /**
     * Called after the device has connected to the Band.
     * Checks to see if a previous setup exists on the device and/or Band.  If a previous setup is
     * found, the existing data is loaded otherwise a fresh setup is undertaken.
     *
     * @param bandClient The Band client returned from the ConnectAsync task
     */
    @Override
    public void onConnectDone(BandClient bandClient) {
        if (bandClient != null) {
            projectClient = new ProjectClient(bandClient, this);
            if (HelperMethods.isInstalled(this, "acc_data")
                && HelperMethods.isInstalled(this, "move_count")
                && HelperMethods.isInstalled(this, "app_id")
                && HelperMethods.isInstalled(this, "please_fail")) { // Intentional fail until load data works
                // loads previous files
                initGraphs();
                projectClient.sendDialog("Device status", "Connected to existing data.");
            } else {
                new Setup(MainActivity.this, projectClient, this).execute();
            }
        }
    } // end onConnectDone()

    /**
     * Called after the app has been fully setup or fully loaded.
     * Initialises all save-dependent page data, such as graphs.
     */
    @Override
    public void onSetupDone() {
        initGraphs();
        findViewById(R.id.recordDataSwitch).setEnabled(true);
        Log.d("SWITCHFLAG", "");
    } // end onSetupDone

    /**
     * Called when the moveCount variable has been updated.
     * Updates page values and graphs relating to the movement count.
     */
    @Override
    public void onMoveCountChanged() {
        final int moveCount = ((WearableApplication) this.getApplication()).getTotalMovesToday() + 1;
        ((WearableApplication) this.getApplication()).setTotalMovesToday(moveCount);

        if (sendHapticsSwitch.isChecked()) {
            projectClient.sendHaptic();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (liveGraphingSwitch.isChecked()) {
                    hourlyMovesBar.incrementDataSet(HelperMethods.getCurrentDate());
                    hourlyMovesBar.updateDisplay();
                    projectClient.setMovePageData(moveCount);
                }
                progressClock.invalidate();
            }
        });
    } // end onMoveCountChanged()

    /**
     * Called when the accelerometer data has changed.
     * Updates page values and graphs relating to accelerometer data.
     *
     * @param timestamp The timestamp that the accelerometer data was taken at.
     * @param accData The accelerometer data.
     */
    @Override
    public void onAccChanged(final long timestamp, final float accData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timestamp > lastRefreshed + GRAPH_REFRESH_TIME && liveGraphingSwitch.isChecked()) {
                    lastRefreshed = timestamp;
                    accLineGraph.addToDataSet(accData);
                    accLineGraph.updateDisplay();
                }
            }
        });
    } // end onAccChanged()

    /**
     * Initialise all graphs.
     */
    private void initGraphs() {
        LineChart accLineView = (LineChart) findViewById(R.id.accLineView);
        accLineGraph = new AccLineGraph(accLineView, this);

        BarChart hourlyBarView = (BarChart) findViewById(R.id.hourlyBarView);
        hourlyMovesBar = new HourlyMovesBar(hourlyBarView, this);

        HorizontalBarChart dailyBulletView = (HorizontalBarChart) findViewById(R.id.dailyBulletView);
        DailyMovesBullet dailyMovesBullet = new DailyMovesBullet(dailyBulletView, this);
    } // end initGraphs
}