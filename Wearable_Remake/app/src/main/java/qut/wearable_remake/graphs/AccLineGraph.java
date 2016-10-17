package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AccLineGraph extends AbstractGraph {
    private final LineDataSet dataSet;
    private final LineChart chart;
    private int pointCount;

    public AccLineGraph(LineChart lc, Activity a) {
        super(lc, a, "acc_data");

        pointCount = 0;
        chart = lc;
        dataSet = this.loadSavedData();
        dataSet.setColor(Color.rgb(170, 182, 84));
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2);

        LineData chartData = new LineData(dataSet);
        lc.setData(chartData);
        lc.setClickable(false);

        lc.setDescription("");
        lc.getLegend().setEnabled(false);
        lc.getXAxis().setEnabled(false);
        lc.getAxisRight().setEnabled(false);

        this.updateDisplay();
    }

    /**
     * Adds a new data point to the chart's data.
     *
     * @param y The Y value of the data point.
     */
    public void addToDataSet(float y) {
        this.dataSet.addEntry((new Entry(pointCount, y)));
        chart.setData(new LineData(dataSet));
        pointCount++;
    } // end incrementDataSet()

    /**
     * Reads the data saved in the chart's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the chart's save file.
     */
    @Override
    LineDataSet loadSavedData() {
        ArrayList<Entry> loadedEntries = new ArrayList<>();
        for (String[] pair : this.getDataFromFile()) {
            float yVal = Float.parseFloat(pair[1]);
            loadedEntries.add(new Entry(pointCount, yVal));
            pointCount++;
        }

        return new LineDataSet(loadedEntries, "");
    } // end loadSavedData()

    /**
     * Saves the current state of the chart's data to the save file.
     */
    @Override
    void saveData() {
        // TODO
    } // end saveData()
}