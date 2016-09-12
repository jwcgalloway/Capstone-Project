package qut.wearable_project;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

public class DisplayGraphActivity extends AppCompatActivity {
    //graph
    private RelativeLayout mainLayout;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_graph);
        //TODO
        //display graph programmatically here
        //setup graphing maybe using outside classes
        //somehow update the graph live, and deleting the oldest off the graph after so many iterations

        mainLayout = (RelativeLayout) findViewById(R.id.activity_display_graph);
        //create the lineChart
        mChart = new LineChart(this);
        mainLayout.addView(mChart);
        //Customise the line Chart
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data atm mates");
        //enable value highlighting
        mChart.setHighlightEnabled(true);
        //enable touch gestures
        mChart.setTouchEnabled(true);
        //enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        //enable pinch zoom to avoid separately scaling x and y
        mChart.setPinchZoom(true);

        //background colours
        mChart.setBackgroundColor(Color.LTGRAY);

        //data
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        //add the data
        mChart.setData(data);

        //get legend object
        Legend l = mChart.getLegend();

        //customize
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(120f);
        y1.setDrawGridLines(true);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);

    }
}
