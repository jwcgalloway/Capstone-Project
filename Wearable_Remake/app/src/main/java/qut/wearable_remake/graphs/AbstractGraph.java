package qut.wearable_remake.graphs;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import qut.wearable_remake.HelperMethods;

abstract class AbstractGraph {
    private final Chart chart;
    private final String saveFilename;
    private final Activity activity;

    AbstractGraph(Chart c, Activity a, String sfn) {
        chart = c;
        activity = a;
        saveFilename = sfn;
    }


    /**
     * Reads the data saved in the graph's save file, parses and returns it in Chart Data form.
     *
     * @return Chart Data representing the data stored in the graph's save file.
     */
    abstract ChartData loadSavedData();

    /**
     * Saves the current state of the graph's data to the save file.
     */
    abstract void saveData();

    /**
     * Converts the current data set into chart data.
     *
     * @return The current data set converted into chart data.
     */
    abstract ChartData convertEntries();

    /**
     * Gets, splits and returns the value pairs (X,Y) from the graph's data file.
     *
     * @return An array list containing each value pair.
     */
    ArrayList<String[]> getDataFromFile() {
        String dataStr = "";
        try {
            dataStr = HelperMethods.getDataFromFile(saveFilename, activity);
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
    } // end getDataFromFile()

    /**
     * Updates the graphical display of the graph with the provided data.
     */
    @SuppressWarnings("unchecked")
    public void updateDisplay() {
        chart.setData(convertEntries());
        chart.invalidate();
    } // end updateDisplay()

    Activity getActivity() { return activity; }
}