package qut.wearable_remake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import com.microsoft.band.BandClient;

import java.util.Locale;

import qut.wearable_remake.band.ConnectAsync;
import qut.wearable_remake.band.ProjectClient;
import qut.wearable_remake.band.Setup;
import qut.wearable_remake.graphs.AccLineGraph;
import qut.wearable_remake.graphs.MovementBarGraph;
import qut.wearable_remake.sensors.SensorInterface;

public class MainActivity extends AppCompatActivity implements SpecialEventListener {
    private static final long GRAPH_REFRESH_TIME = 1000;

    private ProjectClient projectClient;
    private AccLineGraph accLineGraph;
    private MovementBarGraph movementBarGraph;

    private long lastRefreshed;
    private boolean liveGraphing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectAsync(MainActivity.this, this).execute();

        Button removeTileBtn = (Button) findViewById(R.id.removeTileBtn);
        removeTileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectClient.removeTile();
            }
        });

        Switch recordDataSwitch = (Switch) findViewById(R.id.recAccSwitch);
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

        Switch liveGraphingSwitch = (Switch) findViewById(R.id.liveGraphSwitch);
        liveGraphingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                liveGraphing = isChecked;
            }
        });

        LineChart accChartView = (LineChart) findViewById(R.id.accLineGraph);
        accLineGraph = new AccLineGraph(accChartView, this);

        BarChart movementChartView = (BarChart) findViewById(R.id.movementBarGraph);
        movementBarGraph = new MovementBarGraph(movementChartView, this);
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
                && HelperMethods.isInstalled(this, "please_fail")) { // Intentional fail until load data works
                // TODO Load previous
                projectClient.sendDialog("Device status", "Connected to existing data.");
            } else {
                new Setup(MainActivity.this, projectClient).execute();
            }
        }
    } // end onConnectDone()

    /**
     * Called when the moveCount variable has been updated.
     * Writes the new count to the 'move_count' local file and updates the value displayed
     * on the info clock and the graph device.
     *
     * @param timestamp The timestamp that the movement count was taken at.
     * @param moveCount The movement count.
     */
    @Override
    public void onMoveCountChanged(final long timestamp, int moveCount) {
        final String countStr = String.format(Locale.getDefault(), "%d\n", moveCount);
        String str = String.format(Locale.getDefault(), "%d,", timestamp)
                + countStr;
        HelperMethods.writeToFile("move_count", str, MainActivity.this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView moveCountTxt = (TextView) findViewById(R.id.moveCountTxt);
                moveCountTxt.setText(countStr);

                if (liveGraphing) {
                    movementBarGraph.updateGraph();
                }
            }
        });
    } // end onMoveCountChanged()

    /**
     * Called when the accelerometer data has changed.
     * Writes the accelerometer data to the 'acc_data' local file, sets the orientation text view to
     * the given string, and refreshes the graph, provided live graphing is checked.
     *
     * @param timestamp The timestamp that the accelerometer data was taken at.
     * @param accData The accelerometer data.
     */
    @Override
    public void onAccChanged(final long timestamp, float accData) {
        String str = String.format(Locale.getDefault(), "%d,", timestamp)
                + String.format(Locale.getDefault(), "%f\n", accData);

        HelperMethods.writeToFile("acc_data", str, MainActivity.this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timestamp > lastRefreshed + GRAPH_REFRESH_TIME && liveGraphing) {
                    lastRefreshed = timestamp;
                    accLineGraph.updateGraph();
                }
            }
        });
    } // end onAccChanged()
}