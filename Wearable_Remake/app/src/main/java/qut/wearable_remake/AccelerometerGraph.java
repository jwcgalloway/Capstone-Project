package qut.wearable_remake;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


class AccelerometerGraph {
    private final LineChart mChart;
    private final Activity mainActivity;
    private  final List<Entry> entries;
    private  LineDataSet dataSet;
    private LineData data;
    private final List<Entry> xVal;
    private TreeMap<Integer, String[]> map;



    public AccelerometerGraph(LineChart lc, Activity mainActivity) {
        mChart = lc;
        this.mainActivity = mainActivity;
        entries = new ArrayList<Entry>();
        xVal = new ArrayList<Entry>();
        map = new TreeMap<>();

    }

    /*
    public void setDummyData(Context mainActivity) {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0.08f, 0f));
        entries.add(new Entry(0.04f, 1f));
        entries.add(new Entry(0.18f, 2f));
        entries.add(new Entry(0.11f, 3f));
        entries.add(new Entry(-0.27f, 4f));
        entries.add(new Entry(-.02f, 5f));
        Collections.sort(entries, new EntryXComparator());
        //LineDataSet dataset = new LineDataSet(entries, "Accelerometer Data");

        //ArrayList<String> labels = new ArrayList<String>();


        //LineData data = new LineData(dataset);
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        //dataset.setDrawFilled(true);

        //mChart.setData(data);
       // mChart.animateY(5000);
        //mChart.invalidate();


        TreeMap<Integer, String[]> map = new TreeMap<>();

        //get data
        String dataString = "";
        try {
            dataString = HelperMethods.getStrFromFile("acc_data", mainActivity);
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

        //test treemap
        //TREEMAP : Timestamp, ACCELX, ACCEL Y, ACCEL Z
        for (Map.Entry<Integer, String[]> entry : map.entrySet()) {
            Log.d("TreeMap","Key: " + entry.getKey() + ". Value: " + entry.getValue());

        }


        LineDataSet dataset = new LineDataSet(xVal, "Accelerometer Data X");
        LineData data = new LineData(dataset);
        mChart.setData(data);
        mChart.animateY(5000);
        mChart.invalidate();


    }
    */

    //update function


    //load data into treemaps


    //fill the datasets


    //order:
    //load data, fill datasets, update the screen

    public void loadDataIntoMap(){
        //get data
        String dataString = "";
        try {
            dataString = HelperMethods.getStrFromFile("acc_data", mainActivity);
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
            Log.d("TreeMap","Key: " + entry.getKey() + ". Value: " + entry.getValue());
            String entryString = entry.getValue()[1];
            float entryFloat = Float.parseFloat(entryString);
            //entryFloat = (float)Math.round(entryFloat);
            //Entry entryValue = new Entry(entryFloat,entry.getKey());
           xVal.add(new Entry(entry.getKey(),entryFloat));

        }
    }


    private void fillDataSets(){
        dataSet = new LineDataSet(xVal, "Accelerometer Data X");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataSet.setDrawFilled(true);
        data = new LineData(dataSet);

    }

    private void updateScreen(){

        mChart.setData(data);
        mChart.animateY(5000);
        mChart.invalidate();
    }

    public void refreshValues(){
        loadDataIntoMap();
        fillDataSets();
        updateScreen();

    }

}

