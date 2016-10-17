package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import qut.wearable_remake.WearableApplication;

public class DailyMovesBullet extends AbstractGraph {

    public DailyMovesBullet(HorizontalBarChart hc, Activity a) {
        super(hc, a, "some_thing");

        hc.setDrawBorders(true);
        hc.setBorderColor(Color.BLACK);
        hc.setBorderWidth((float) 0.2);
        hc.setDescription("");
        hc.setDrawValueAboveBar(false);

        XAxis xAxis = hc.getXAxis();
        xAxis.setGranularity(10);
        xAxis.setDrawLabels(false);

        int moveGoal = ((WearableApplication) this.getActivity().getApplication()).getMoveGoal();
        LimitLine moveGoalLimitLine = new LimitLine(moveGoal);
        xAxis.addLimitLine(moveGoalLimitLine);

        hc.getLegend().setEnabled(false);
        hc.getAxisRight().setEnabled(false);

        int segmentSize = moveGoal / 5;
        ArrayList<BarEntry> defaultEntries = new ArrayList<>();
        defaultEntries.add(new BarEntry(1, segmentSize * 6));
        defaultEntries.add(new BarEntry(1, segmentSize * 5));
        defaultEntries.add(new BarEntry(1, segmentSize * 4));
        defaultEntries.add(new BarEntry(1, segmentSize * 3));
        defaultEntries.add(new BarEntry(1, segmentSize * 2));
        defaultEntries.add(new BarEntry(1, segmentSize));

        int moveCount = ((WearableApplication) this.getActivity().getApplication()).getTotalMovesToday();
        defaultEntries.add(new BarEntry(1, moveCount));
        BarDataSet dataSet = new BarDataSet(defaultEntries, "");

        ArrayList<Integer> colourScheme = new ArrayList<>();
        colourScheme.add(Color.argb(60, 150, 0, 150));
        colourScheme.add(Color.argb(50, 150, 0, 150));
        colourScheme.add(Color.argb(40, 150, 0, 150));
        colourScheme.add(Color.argb(30, 150, 0, 150));
        colourScheme.add(Color.argb(20, 150, 0, 150));
        colourScheme.add(Color.argb(10, 150, 0, 150));
        colourScheme.add(Color.BLACK);

        dataSet.setColors(colourScheme);
        BarData graphData = new BarData(dataSet);
        hc.setData(graphData);

        this.updateDisplay();
    }

    /**
     * Reads the data saved in the chart's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the chart's save file.
     */
    @Override
    BarDataSet loadSavedData() {
        return null;
    } // loadSavedData()

    /**
     * Saves the current state of the chart's data to the save file.
     */
    @Override
    void saveData() {

    } // end saveData()
}
