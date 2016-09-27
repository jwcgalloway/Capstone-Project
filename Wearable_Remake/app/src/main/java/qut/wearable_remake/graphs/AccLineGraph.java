package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AccLineGraph extends AbstractGraph {
    private final ArrayList<Entry> entriesList;

    public AccLineGraph(LineChart lc, Activity a) {
        super(lc, a, "acc_data");

        lc.setDescription("Accelerometer");
        lc.setDrawBorders(true);
        lc.setBorderColor(Color.BLACK);
        lc.setBorderWidth((float) 0.2);
        lc.getLegend().setEnabled(false);

        entriesList = new ArrayList<>();
        entriesList.add(new Entry(0, 0));
        refreshDisplay(convertEntries());
    }

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        LineDataSet dataSet = new LineDataSet(entriesList, "");
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        return new LineData(dataSet);
    } // end convertEntries()

    /**
     * Parses string value pairs (X,Y) contained in the graph's data file into entries and adds
     * the entries to the entries list.
     */
    @Override
    void parseGraphData() {
        for (String[] pair : this.getGraphData()) {
            int xVal = Integer.parseInt(pair[0].substring(7)); // TODO Fix timestamp parsing
            float yVal = Float.parseFloat(pair[1]);

            entriesList.add(new Entry(xVal, yVal));
        }
    } // end parseGraphData()
}