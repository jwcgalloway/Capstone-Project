package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;
import android.util.LongSparseArray;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AccLineGraph extends AbstractGraph {
    private final LongSparseArray<Float> graphData;

    public AccLineGraph(LineChart lc, Activity a) {
        super(lc, a, "acc_data");
        graphData = new LongSparseArray<>();

        lc.setDescription("Accelerometer");

        lc.setDrawBorders(true);
        lc.setBorderColor(Color.BLACK);
        lc.setBorderWidth((float) 0.2);

        lc.getLegend().setEnabled(false);
        lc.getXAxis().setEnabled(false);
        lc.getAxisRight().setEnabled(false);

        this.loadSavedData();
        this.updateDisplay();
    }

    /**
     * Adds a new data point to the graph's data.
     *
     * @param x The X value of the data point.
     * @param y The Y value of the data point.
     */
    public void addToGraphData(long x, float y) {
        graphData.append(x, y);
    } // end incrementGraphData()

    /**
     * Reads the data saved in the graph's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the graph's save file.
     */
    @Override
    ChartData loadSavedData() {
        for (String[] pair : this.getDataFromFile()) {
            long xVal = Long.parseLong(pair[0]); // TODO Fix timestamp parsing
            float yVal = Float.parseFloat(pair[1]);
            graphData.put(xVal, yVal);
        }

        return convertEntries();
    } // end loadSavedData()

    /**
     * Saves the current state of the graph's data to the save file.
     */
    @Override
    void saveData() {
        // TODO
    } // end saveData()

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < graphData.size(); i++) {
            entries.add(new Entry(graphData.keyAt(i), graphData.valueAt(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        return new LineData(dataSet);
    } // end convertEntries()
}