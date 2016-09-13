package qut.wearable_project;



import android.content.Context;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/*
 * Created by arjaireynolds on 8/09/16.
 */
public  class GraphTest {
    private ArrayList<BarEntry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private BarDataSet dataset;
    /*
        Create entries for graph
     */

    public void test() {
        //add entries to y axis
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        //create dataset=
        //BarDataSet dataset = new BarDataSet(entries, "# of Calls");
         dataset = new BarDataSet(entries, "# of Calls");
        //defining x axis
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

    }
    /*
        Requires context from main activity
     */
    public BarChart createChart(Context context){
        BarChart chart = new BarChart(context);
        return chart;
    }

    /*
        Requires chart from main activity
     */
    public void addDataToChart(BarChart chart) {
        BarData data = new BarData(labels, dataset);
        chart.setData(data);
    }
}







