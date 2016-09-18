package qut.wearable_remake;

import android.app.Activity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Graph {
    private final LineChart mChart;
    private final Activity activity;
    private final TreeMap<Integer, String[]> map;

    Graph(LineChart lc, Activity a) {
        mChart = lc;
        activity = a;
        map = new TreeMap<>();
    }

    /**
     * Refreshes the graph with an empty data set for the purpose of displaying a blank graph.
     */
    public void setEmptyGraph() {
        List<Entry> list = new ArrayList<>(1);
        list.add(new Entry(0, 0));
        LineDataSet dataSet = new LineDataSet(list, "");
        updateScreen(new LineData(dataSet));
    } // end setEmptyGraph

    private List<Entry> loadDataIntoMap(){
        List<Entry> xAcc = new ArrayList<>();

        //get data
        String dataString = "";
        try {
            dataString = HelperMethods.getStrFromFile("acc_data", activity);
        }catch(Exception e){
            e.printStackTrace();
        }

        //process the data into treemap
        int key = 0;
        Scanner scanner = new Scanner(dataString);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //List<String> values = Arrays.asList(line.split(" , "));
            String[] values = line.split(",");

            //process into a map

            map.put(key,values);
            Log.d("Scanner Line Data",line);
            key++;
        }
        scanner.close();

        for (Map.Entry<Integer, String[]> entry : map.entrySet()) {
            //Log.d("TreeMap","Key: " + entry.getKey() + ". Value: " + entry.getValue());
            String entryString = entry.getValue()[1];
            float entryFloat = Float.parseFloat(entryString);
            //entryFloat = (float)Math.round(entryFloat);
            //Entry entryValue = new Entry(entryFloat,entry.getKey());
            xAcc.add(new Entry(entry.getKey(),entryFloat));
        }
        return xAcc;
    } // end loadDataIntoMap()

    private LineData fillDataSets(List<Entry> xAcc) {
        LineDataSet dataSet = new LineDataSet(xAcc, "Accelerometer Data X");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawFilled(true);
        return new LineData(dataSet);
    } // end fillDataSets()

    private void updateScreen(LineData data) {
        mChart.setData(data);
        //mChart.animateY(1000);
        mChart.invalidate();
    } // end updateScreen()


    public void refreshValues() {
        List<Entry> xAcc = loadDataIntoMap();
        LineData data = fillDataSets(xAcc);
        updateScreen(data);
    } // end refreshValues()
}

