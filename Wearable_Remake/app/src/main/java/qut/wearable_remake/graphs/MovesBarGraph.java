package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import qut.wearable_remake.HelperMethods;
import qut.wearable_remake.WearableApplication;

public class MovesBarGraph extends AbstractGraph {
    private final Map<String, Integer> graphData;
    private ArrayList<Integer> colourScheme;

    public MovesBarGraph(BarChart bc, Activity a) {
        super(bc, a, "move_count");
        graphData = new HashMap<>();

        colourScheme = new ArrayList<>();
        colourScheme.add(Color.parseColor("#1cbc81"));
        colourScheme.add(Color.parseColor("#159466"));
        colourScheme.add(Color.parseColor("#117651"));

        bc.setDescription("Movements by Hour");

        bc.setDrawBorders(true);
        bc.setBorderColor(Color.BLACK);
        bc.setBorderWidth((float) 0.2);

        bc.setDrawValueAboveBar(true);
        bc.getLegend().setEnabled(false);
        bc.getAxisRight().setEnabled(false);

        XAxis xAxis = bc.getXAxis();
        xAxis.setValueFormatter(new DateFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = bc.getAxisLeft();
        yAxisLeft.setGranularity(10);

        this.loadSavedData();
        this.updateDisplay();
    }

    /**
     * Increments the move count at a particular key in the graph's data.
     *
     * @param x The X value of the data point.
     */
    public void incrementGraphData(String x) {
        if (!graphData.containsKey(x)) {
            graphData.put(x, 1);
        } else {
            graphData.put(x, graphData.get(x) + 1);
        }
    } // end incrementGraphData()

    /**
     * Reads the data saved in the graph's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the graph's save file.
     */
    @Override
    ChartData loadSavedData() {
        int totalMoves = 0;
        for (String[] pair : this.getDataFromFile()) {
            int yVal = Integer.parseInt(pair[1]);
            totalMoves = totalMoves + yVal;
            graphData.put(pair[0], yVal);
        }

        ((WearableApplication) this.getActivity().getApplication()).setTotalMovesToday(totalMoves);
        return convertEntries();
    } // end loadSavedData()

    /**
     * Saves the current state of the graph's data to the save file.
     */
    @Override
    void saveData() {
        for (Map.Entry<String, Integer> entry : graphData.entrySet()) {
            HelperMethods.writeToFile("move_count", entry.getKey() + "," + Integer.toString(entry.getValue()), this.getActivity());
        }
    } // end saveData()

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    @Override
    ChartData convertEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : graphData.entrySet()) {
            String[] splitDate = entry.getKey().split(":");
            entries.add(new BarEntry(Integer.parseInt(splitDate[1]), entry.getValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setColors(colourScheme);
        dataSet.setDrawValues(false);
        return new BarData(dataSet);
    } // end convertEntries()
}
