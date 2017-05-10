package qut.wearable_remake.graphs;

import android.app.Activity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ActionsBar extends AbstractGraph {
    private final BarDataSet dataSet;
    private final HorizontalBarChart chart;

    public ActionsBar(HorizontalBarChart hc, Activity a) {
        super(hc, a, "actions_count");

        chart = hc;

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 0));
        entries.add(new BarEntry(1f, 0));
        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(3f, 0));

        dataSet = new BarDataSet(entries, "Actions Performed");
        dataSet.setDrawValues(false);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData graphData = new BarData(dataSet);
        graphData.setBarWidth(0.7f);
        hc.setData(graphData);
        hc.setDescription("");
        hc.getAxisRight().setGranularity(1);
        hc.getAxisLeft().setDrawLabels(false);
        hc.setFitBars(true);
        hc.getLegend().setEnabled(false);

        XAxis xAxis = hc.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(4);
        xAxis.setTextSize(4f);
        xAxis.setDrawGridLines(false);

        final String[] labels = new String[]
                {"Stirring", "Reach and Retrieve", "Reach to Mouth", "Wrist Rotation"};
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value < labels.length) {
                    return labels[(int) value];
                } else {
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        this.updateDisplay();
    }

    /**
     * Increments the action count of a particular action in the chart's data.
     *
     * @param action The integer key of the action
     *      (0 = reach & retrieve, 1 = reach to mouth, 2 = wrist rotation, 3 = stirring)
     */
    public void incrementDataSet(int action) {
        BarEntry entry = dataSet.getEntryForIndex(action);
        entry.setY(entry.getY() + 1);
        dataSet.notifyDataSetChanged();
        chart.setData(new BarData(dataSet));
        chart.invalidate();
    } // end incrementDataSet()
}
