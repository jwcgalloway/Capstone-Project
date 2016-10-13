package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;

public class MovesBulletGraph extends AbstractGraph {
    private final ArrayList<BarEntry> entriesList;
    private final ArrayList<Integer> colourScheme;

    public MovesBulletGraph(HorizontalBarChart hc, Activity a) {
        super(hc, a, "some_thing");

        hc.setDrawBorders(true);
        hc.setBorderColor(Color.BLACK);
        hc.setBorderWidth((float) 0.2);

        XAxis xAxis = hc.getXAxis();
        xAxis.setGranularity(10);
        xAxis.setDrawLabels(false);

        hc.getLegend().setEnabled(false);
        hc.getAxisRight().setEnabled(false);

        entriesList = new ArrayList<>();
        entriesList.add(new BarEntry(1, 50));
        entriesList.add(new BarEntry(1, 40));
        entriesList.add(new BarEntry(1, 30));
        entriesList.add(new BarEntry(1, 20));
        entriesList.add(new BarEntry(1, 10));
        entriesList.add(new BarEntry(1, 16));

        colourScheme = new ArrayList<>();
        colourScheme.add(Color.argb(50, 150, 0, 150));
        colourScheme.add(Color.argb(40, 150, 0, 150));
        colourScheme.add(Color.argb(30, 150, 0, 150));
        colourScheme.add(Color.argb(20, 150, 0, 150));
        colourScheme.add(Color.argb(10, 150, 0, 150));
        colourScheme.add(Color.BLACK);

        this.updateDisplay();
    }

    /**
     * Reads the data saved in the graph's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the graph's save file.
     */
    @Override
    ChartData loadSavedData() {
        return null;
    } // loadSavedData()

    /**
     * Saves the current state of the graph's data to the save file.
     */
    @Override
    void saveData() {

    } // end saveData()

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        BarDataSet dataSet = new BarDataSet(entriesList, "");
        dataSet.setColors(colourScheme);
        return new BarData(dataSet);
    } // convertEntries()
}
