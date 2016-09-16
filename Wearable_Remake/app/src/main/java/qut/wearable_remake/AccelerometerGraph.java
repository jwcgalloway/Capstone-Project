package qut.wearable_remake;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

class AccelerometerGraph {
    private final LineChart mChart;

    AccelerometerGraph(LineChart lc) {
        mChart = lc;
        setDummyData();
    }

    private void setDummyData() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataSet = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        LineDataSet data = new LineData(labels, dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);

        mChart.setData(data);
        mChart.animateY(3000);
    }
}

