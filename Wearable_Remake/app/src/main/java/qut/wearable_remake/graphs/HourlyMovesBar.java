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
    private final BarChart chart;
    private BarDataSet dataSet1;
    private BarDataSet dataSet2;

    public HourlyMovesBar(BarChart bc, Activity a) {
        super(bc, a, "move_count");

        chart = bc;
        loadSavedData();
        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet1.setColor(Color.rgb(51, 188, 161));
        dataSet2.setColor(Color.rgb(124, 124, 124));

        BarData graphData = new BarData(dataSet1, dataSet2);
        graphData.setBarWidth(0.46f);

        bc.setData(graphData);
        bc.groupBars(0, 0.04f, 0.02f);
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
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinValue(0);

        YAxis yAxisLeft = bc.getAxisLeft();
        yAxisLeft.setGranularity(10);

        this.updateDisplay();
    }

    /**
     * Increments the move count at a particular key in the chart's data.
     *
     * @param x The X value of the data point.
     */
    public void incrementBand1(String x) {
        int hour = HelperMethods.getHourFromDate(x);
        BarEntry entry = dataSet1.getEntryForXPos(hour);

        if (entry == null) {
            dataSet1.addEntry(new BarEntry(hour, 1));
        } else {
            entry.setY(entry.getY() + 1);
        }

        dataSet1.notifyDataSetChanged();
        chart.notifyDataSetChanged();
    } // end incrementBand1()

    /**
     * Reads the data saved in the chart's save file, parses and returns it in Chart Data form.
     */
    private void loadSavedData() {
        int totalMoves = 0;
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (String[] pair : this.getDataFromFile()) {
            int xVal = HelperMethods.getHourFromDate(pair[0]);
            int yVal1 = Integer.parseInt(pair[1]);
            int yVal2 = Integer.parseInt(pair[2]);

            totalMoves = totalMoves + yVal1;
            entries1.add(new BarEntry(xVal, yVal1));
            entries2.add(new BarEntry(xVal, yVal2));
        }

        ((WearableApplication) this.getActivity().getApplication()).setTotalMovesToday(totalMoves);
        dataSet1 = new BarDataSet(entries1, "");
        dataSet2 = new BarDataSet(entries2, "");
    } // end loadSavedData()

    /**
     * Saves the current state of the chart's data to the save file.
     */
    void saveData() {
        String date = HelperMethods.getCurrentDate().split(":")[0];
        for (int i = 0; i < dataSet1.getEntryCount(); i++) {
            BarEntry entry = dataSet1.getEntryForIndex(i);
            String content = date + ":" + Float.toString(entry.getX()) + "," + Float.toString(entry.getY());
            HelperMethods.writeToFile("move_count", content, this.getActivity());
        }
    } // end saveData()
}