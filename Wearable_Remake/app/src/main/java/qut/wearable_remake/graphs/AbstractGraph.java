package qut.wearable_remake.graphs;

import android.app.Activity;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import qut.wearable_remake.HelperMethods;

abstract class AbstractGraph {
    private final Chart MPChart;
    private final Activity activity;
    private final String dataFilename;

    AbstractGraph(Chart c, Activity a, String dfn) {
        MPChart = c;
        activity = a;
        dataFilename = dfn;
    }

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    abstract ChartData convertEntries();

    /**
     * Parses string value pairs (X,Y) in the provided array list into entries and adds
     * the entries to the entries list.
     *
     */
    abstract void parseGraphData();


    /**
     * Updates the graph with the latest data available.
     */
    public void updateGraph() {
        parseGraphData();
        refreshDisplay(convertEntries());
    } // end parseGraphData()

    /**
     * Gets, splits and returns the value pairs (X,Y) from the graph's data file.
     *
     * @return An array list containing each value pair.
     */
    ArrayList<String[]> getGraphData() {
        String dataStr = "";
        try {
            dataStr = HelperMethods.getDataFromFile(dataFilename, activity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String[]> strValuePairs = new ArrayList<>();

        Scanner scanner = new Scanner(dataStr);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String[] splitVals = nextLine.split(",");

            strValuePairs.add(splitVals);
        }
        scanner.close();

        return strValuePairs;
    } // end getGraphData()

    /**
     * Updates the graphical display of the graph with the provided data.
     */
    @SuppressWarnings("unchecked")
    void refreshDisplay(ChartData data) {
        MPChart.setData(data);
        MPChart.invalidate();
    } // end refreshDisplay()
}