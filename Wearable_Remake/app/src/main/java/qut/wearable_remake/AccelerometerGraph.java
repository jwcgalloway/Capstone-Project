package qut.wearable_remake;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class AccelerometerGraph {
    private final LineChart mChart;
    private final Activity mainActivity;

    public AccelerometerGraph(LineChart lc,Activity mainActivity) {
        mChart = lc;
        this.mainActivity = mainActivity;
        setDummyData(mainActivity);
    }

    public void setDummyData(Context mainActivity) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataSet = new LineDataSet(entries, "# of Calls");

        List<Entry> xVal = new ArrayList<Entry>();
        List<Entry> yVal = new ArrayList<Entry>();
        List<Entry> zVal = new ArrayList<Entry>();
        TreeMap<Integer, List<String>> map =new TreeMap<Integer, List<String>>();

        //get data
        String data = "";
        try {
            data = HelperMethods.getStrFromFile("acc_data", mainActivity);
        }catch(Exception e){
            e.printStackTrace();
        }

        //process the data into treemap
        int key = 0;
        Scanner scanner = new Scanner(data);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> values = Arrays.asList(line.split(" , "));
            //process into a map
            map.put(key,values);
            Log.d("Scanner Line Data",line);
            key++;
        }
        scanner.close();

        //test treemap
        //TREEMAP : Timestamp, ACCELX, ACCEL Y, ACCEL Z
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            Log.d("TreeMap","Key: " + entry.getKey() + ". Value: " + entry.getValue());

        }

        //fill datasets with values
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            Log.d("TreeMap","Key: " + entry.getKey() + ". Value: " + entry.getValue());
            String entryString = entry.getValue().get(1);
            float entryFloat = (float)Float.parseFloat(entryString);
            //entryFloat = (float)Math.round(entryFloat);
            //Entry entryValue = new Entry(entryFloat,entry.getKey());
            xVal.add(new Entry(entryFloat,entry.getKey()));

        }
        /*
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        */

        LineDataSet lineSet = new LineDataSet(xVal, "xVals");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
       // dataSet.setDrawCubic(true);
        dataSet.setDrawFilled(true);

       // mChart.setData(data);
        mChart.animateY(3000);
    }

    public LineChart getChart(){
        return this.mChart;
    }
}

