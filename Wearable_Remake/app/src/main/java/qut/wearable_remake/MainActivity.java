package qut.wearable_remake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import com.microsoft.band.BandClient;

import java.util.Locale;

class MainActivity extends AppCompatActivity implements SpecialEventListener {
    private ProjectClient projectClient;

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
                ProjectSensor[] sensors = projectClient.getSensors();
                if (isChecked) {
                    for (ProjectSensor sensor : sensors) {
                        sensor.registerListener(bandClient);
                    }
                } else {
                    for (ProjectSensor sensor : sensors) {
                        sensor.unregisterListener(bandClient);
                    }
                }
            }
        });

        LineChart mChart = (LineChart) findViewById(R.id.mChart);
        new AccelerometerGraph(mChart);
        HelperMethods.saveInit(this);
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
            if (HelperMethods.isInstalled()) {
                // TODO Load previous
            } else {
                new Setup(MainActivity.this, projectClient).execute();
            }
        }
    } // end onConnectDone()

    /**
     * Called when the moveCount variable has been updated.
     * Writes the new count to the 'move_count' local file and updates the value displayed
     * on the device.
     *
     * @param moveCount The movement count.
     */
    @Override
    public void onMoveCountChanged(int moveCount) {
        final String countStr = String.format(Locale.getDefault(), "%d", moveCount);
        HelperMethods.writeToFile("move_count", countStr, MainActivity.this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView moveCountTxt = (TextView) findViewById(R.id.moveCountTxt);
                moveCountTxt.setText(countStr);
            }
        });
    } // end onMoveCountChanged()

    /**
     * Called when the accelerometer data has changed.
     * Writes the accelerometer data to the 'acc_data' local file.
     *
     * @param accData The accelerometer data.
     */
    @Override
    public void onAccChanged(float[] accData, long time) {
        String str = String.format(Locale.getDefault(), "%f", accData[0])
                + String.format(Locale.getDefault(), "%f", accData[1])
                + String.format(Locale.getDefault(), "%f", accData[2])
                + String.format(Locale.getDefault(), "%d", time);

        HelperMethods.writeToFile("acc_data", str, MainActivity.this);
    } // end onAccChanged()
}