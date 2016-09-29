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
        bc.getXAxis().setValueFormatter(new DateFormatter());

        entriesList = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            entriesList.add(new BarEntry(i, 0));
        }
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
        dataSet.setBarBorderWidth((float) 0.2);
        dataSet.setBarBorderColor(Color.BLACK);
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

            String[] splitXVal = pair[0].split(":");
            // splitXVal[0] = dd/mm/yyyy // TODO Factor in the day/month/year
            float xVal = Float.parseFloat(splitXVal[1]);
            int yVal = Integer.parseInt(pair[1]);

            entriesList.add(new BarEntry(xVal, yVal));
        }
    } // end parseGraphData()
}
