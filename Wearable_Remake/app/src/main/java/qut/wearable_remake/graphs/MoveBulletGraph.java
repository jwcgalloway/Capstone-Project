package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;

public class MoveBulletGraph extends AbstractGraph {
    private final ArrayList<BarEntry> entriesList;


    public MoveBulletGraph(HorizontalBarChart hc, Activity a) {
        super(hc, a, "some_thing");

        entriesList = new ArrayList<>();
        entriesList.add(new BarEntry(1, 10));
        entriesList.add(new BarEntry(1, 5));

        refreshDisplay(convertEntries());
    }

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        BarDataSet dataSet = new BarDataSet(entriesList, "");
        dataSet.setColor(Color.GREEN);
        return new BarData(dataSet);
    }

    /**
     * Parses string value pairs (X,Y) in the provided array list into entries and adds
     * the entries to the entries list.
     */
    @Override
    void parseGraphData() {

    }
}
