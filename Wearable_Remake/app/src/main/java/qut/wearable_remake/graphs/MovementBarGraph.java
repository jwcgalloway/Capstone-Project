package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;

public class MovementBarGraph extends AbstractGraph {
    private final ArrayList<BarEntry> entriesList;

    public MovementBarGraph(BarChart bc, Activity a) {
        super(bc, a, "move_count");

        bc.setDescription("Movement Count");
        bc.setDrawBorders(true);
        bc.setBorderColor(Color.BLACK);
        bc.setBorderWidth((float) 0.2);
        bc.getLegend().setEnabled(false);

        entriesList = new ArrayList<>();

        BarData emptyData = new BarData();
        emptyData.addEntry(new BarEntry(0, 0), 0);
        setGraphEmpty(emptyData);
    }

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        BarDataSet dataSet = new BarDataSet(entriesList, "");
        dataSet.setDrawValues(false);
        return new BarData(dataSet);
    } // end convertEntries()

    /**
     * Parses string value pairs (X,Y) in the provided array list into entries and adds
     * the entries to the entries list.
     */
    @Override
    void parseGraphData() {
        for (String[] pair : this.getGraphData()) {
            int xVal = 1; // TODO Get real timestamp
            int yVal = Integer.parseInt(pair[1]);

            entriesList.add(new BarEntry(xVal, yVal));
        }
    } // end parseGraphData()
}
