package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import qut.wearable_remake.HelperMethods;
import qut.wearable_remake.WearableApplication;

public class HourlyMovesBar extends AbstractGraph {
    private final BarDataSet dataSet;
    private final BarChart chart;

    public HourlyMovesBar(BarChart bc, Activity a) {
        super(bc, a, "move_count");

        chart = bc;
        dataSet = this.loadSavedData();
        dataSet.setDrawValues(false);

        ArrayList<Integer> colourScheme = new ArrayList<>();
        colourScheme.add(Color.rgb(224, 84, 54));
        colourScheme.add(Color.rgb(170, 182, 84));
        colourScheme.add(Color.rgb(254, 142, 55));
        colourScheme.add(Color.rgb(113, 167, 164));
        dataSet.setColors(colourScheme);

        BarData graphData = new BarData(dataSet);
        bc.setData(graphData);

        bc.setDescription("");

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

        this.updateDisplay();
    }

    /**
     * Increments the move count at a particular key in the chart's data.
     *
     * @param x The X value of the data point.
     */
    public void incrementDataSet(String x) {
        int hour = HelperMethods.getHourFromDate(x);
        BarEntry entry = dataSet.getEntryForXPos(hour);

        if (entry == null) {
            dataSet.addEntry(new BarEntry(hour, 1));
        } else {
            entry.setY(entry.getY() + 1);
        }

        dataSet.notifyDataSetChanged();
        chart.setData(new BarData(dataSet));
    } // end incrementDataSet()

    /**
     * Reads the data saved in the chart's save file, parses and returns it in Chart Data form.
     */
    @Override
    BarDataSet loadSavedData() {
        int totalMoves = 0;
        ArrayList<BarEntry> loadedEntries = new ArrayList<>();

        for (String[] pair : this.getDataFromFile()) {
            int xVal = HelperMethods.getHourFromDate(pair[0]);
            int yVal = Integer.parseInt(pair[1]);

            totalMoves = totalMoves + yVal;
            loadedEntries.add(new BarEntry(xVal, yVal));
        }

        ((WearableApplication) this.getActivity().getApplication()).setTotalMovesToday(totalMoves);
        return new BarDataSet(loadedEntries, "");
    } // end loadSavedData()

    /**
     * Saves the current state of the chart's data to the save file.
     */
    @Override
    void saveData() {
        String date = HelperMethods.getCurrentDate().split(":")[0];
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            BarEntry entry = dataSet.getEntryForIndex(i);
            String content = date + ":" + Float.toString(entry.getX()) + "," + Float.toString(entry.getY());
            HelperMethods.writeToFile("move_count", content, this.getActivity());
        }
    } // end saveData()
}