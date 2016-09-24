package qut.wearable_remake.graphing;

import android.app.Activity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AccGraph extends AbstractGraph {

    public AccGraph(LineChart c, Activity a, String dfn) {
        super(c, a, dfn);
    }

    /**
     * Refreshes the graph with an empty dataEntries set for the purpose of displaying a blank graph.
     */
    @SuppressWarnings("unchecked") // TODO Generify?
    public void setGraphEmpty() {
        LineDataSet dataSet = new LineDataSet(new ArrayList<Entry>(), "Accelerometer Data");
        this.getChart().setData(new LineData(dataSet));
        this.getChart().invalidate();
    } // end setGraphEmpty()

    /**
     * Converts the given data entries to the appropriate chart data for this graph.
     *
     * @param dataEntries The data entries to convert.
     * @return The converted chart data.
     */
    @Override
    ChartData convertData(ArrayList<Entry> dataEntries) {
        LineDataSet dataSet = new LineDataSet(dataEntries, "Accelerometer Data");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setDrawFilled(true);
        return new LineData(dataSet);
    } // end convertData()
}

