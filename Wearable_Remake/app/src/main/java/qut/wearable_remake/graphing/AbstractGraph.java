package qut.wearable_remake.graphing;

import android.app.Activity;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import qut.wearable_remake.HelperMethods;

public abstract class AbstractGraph {
    private final Chart MPChart;
    private final Activity activity;
    private final String dataFilename;

    AbstractGraph(Chart c, Activity a, String dfn) {
        MPChart = c;
        activity = a;
        dataFilename = dfn;
    }

    /**
     * Converts the given data entries to the appropriate chart data for this graph.
     *
     * @param dataEntries The data entries to convert.
     * @return The converted chart data.
     */
    abstract ChartData convertData(ArrayList<Entry> dataEntries);

    /**
     * Gets the data contained in the specified file, formats and adds it to an array list.
     *
     * @return An array list containing each entry contained within the file.
     */
    private ArrayList<Entry> getData() {
        ArrayList<Entry> dataEntries = new ArrayList<>();

        String dataStr = "";
        try {
            dataStr = HelperMethods.getStrFromFile(dataFilename, activity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(dataStr);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitVals = line.split(",");

            float xVal = Float.parseFloat(splitVals[0].substring(7)); // TODO Fix timestamp formatting
            float yVal = Float.parseFloat(splitVals[1]);

            dataEntries.add(new Entry(xVal, yVal));
        }
        scanner.close();

        return dataEntries;
    } // end getData()

    /**
     * Updates the graphical display of the graph with the provided data.
     */
    @SuppressWarnings("unchecked")
    public void updateChart() {
        MPChart.setData(convertData(getData()));
        MPChart.invalidate();
    } // end updateChart()

    Chart getChart() { return MPChart; }
}
