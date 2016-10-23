package qut.wearable_remake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.microsoft.band.BandClient;

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
    private Switch dualBandSwitch;

    private ProjectClient projectClient1;
    private ProjectClient projectClient2;

    private AccLineGraph accLineGraph;
    private HourlyMovesBar hourlyMovesBar;
    private DailyMovesBullet dailyMovesBullet;

    private long lastRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectAsync(this, this).execute();

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
                dailyMovesBullet.updateMoveGoalLine();
            }
        });

        Switch recordDataSwitch = (Switch) findViewById(R.id.recordDataSwitch);
        recordDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BandClient bandClient = projectClient1.getBandClient();
                SensorInterface[] sensors = projectClient1.getSensors();
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

        dualBandSwitch = (Switch) findViewById(R.id.dualBandSwitch);
        dualBandSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((WearableApplication) MainActivity.this.getApplication()).setDualBands(isChecked);
                if (isChecked) {
                    new ConnectAsync(MainActivity.this, MainActivity.this).execute();
                }
            }
        });

        liveGraphingSwitch = (Switch) findViewById(R.id.liveGraphSwitch);
        sendHapticsSwitch = (Switch) findViewById(R.id.sendHapticsSwitch);

        Button removeTileBtn = (Button) findViewById(R.id.removeTileBtn);
        removeTileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectClient1.removeTile();
            }
        });
    }

    /**
     * Called after the device has connected to the Band.
     * Checks to see if a previous setup exists on the device and/or Band.  If a previous setup is
     * found, the existing data is loaded otherwise a fresh setup is undertaken.
     *
     * @param bandClients The Band client returned from the ConnectAsync task
     */
    @Override
    public void onConnectDone(BandClient[] bandClients) {
        projectClient1 = new ProjectClient(bandClients[0], this);
        boolean isDualBands = ((WearableApplication) MainActivity.this.getApplication()).isDualBands();

        if (isDualBands) {
            if (bandClients.length > 1) {
                projectClient2 = new ProjectClient(bandClients[1], this);
            } else {
                dualBandSwitch.setChecked(false);
            }
        }

        if (HelperMethods.isInstalled(this, "acc_data")
                && HelperMethods.isInstalled(this, "move_count")
                && HelperMethods.isInstalled(this, "app_id")
                && HelperMethods.isInstalled(this, "please_fail")) { // Intentional fail until load data works
            // loads previous files
            initGraphs();
            projectClient1.sendDialog("Device status", "Connected to existing data.");
        } else {
            new Setup(MainActivity.this, projectClient1, this).execute();
            if (isDualBands) {
                new Setup(MainActivity.this, projectClient2, this).execute();
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
            projectClient1.sendHaptic();
        }









        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hourlyMovesBar.incrementDataSet(HelperMethods.getCurrentDate());

                if (liveGraphingSwitch.isChecked()) {
                    projectClient1.setMovePageData(moveCount);
                    hourlyMovesBar.updateDisplay();
                    dailyMovesBullet.updateDataSet();
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
        accLineGraph.addToDataSet(accData);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timestamp > lastRefreshed + GRAPH_REFRESH_TIME && liveGraphingSwitch.isChecked()) {
                    lastRefreshed = timestamp;
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
        dailyMovesBullet = new DailyMovesBullet(dailyBulletView, this);
    } // end initGraphs
}