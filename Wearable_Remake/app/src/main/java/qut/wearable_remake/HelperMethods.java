package qut.wearable_remake;

import android.app.Activity;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HelperMethods {

    /**
     * Checks to see if there is an existing instance of the application by checking if the internal
     * save files already exist.
     *
     * @param context Context of the current page.
     * @param fileName Name of internally saved file to be checked if exists.
     * @return True is installed, otherwise false.
     */
    public static boolean isInstalled(Context context, String fileName) {
        String filePath = context.getFilesDir().toString() + String.format("/%s", fileName);
        File file = new File(filePath);
        return file.exists();
    } // end isInstalled()

    /**
     * Writes a given string to a given file.
     *
     * @param filename The filepath of the file to be edited.
     * @param content The string that will be written.
     * @param activity The activity used to obtain the filepath.
     */
    public static void writeToFile(String filename, String content, Activity activity) {
        try {
            FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_APPEND);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    } // end writeToFile()

    /**
     * Converts the contents of an input stream to a string.
     *
     * @param stream The input stream to be converted.
     * @return The contents of the input stream as a string.
     * @throws IOException If line could not be read.
     */
    private static String streamToString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("UnusedAssignment")
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    } // end streamToString()

    /**
     * Reads a given file and returns its contents as a string.  The contents of the file are then
     * deleted.
     *
     * @param fileName The name of the file to be read.
     * @param  context Context of the current activity.
     * @return The string contents of the file.
     * @throws IOException If file is not found.
     */
    public static String getDataFromFile(String fileName, Context context) throws IOException {
        String filePath = context.getFilesDir().toString() + String.format("/%s", fileName);
        File file = new File(filePath);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    } // end getDataFromFile()

    /**
     * Reads the saved UUID data and returns an array of 2 consisting the UUID and pageID.
     *
     * @param a Activity for the read file function.
     * @return The array of type UUID of the app's saved UUID and pageID
     */
    public static List<UUID> getUUID (Activity a) {
        String uuid_str = null;
        try {
            uuid_str = HelperMethods.getDataFromFile("app_id", a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> strings = Arrays.asList(uuid_str.split(","));
        List<UUID> uuid_list = new ArrayList<>();
        for (String i:strings) {
            UUID val = UUID.fromString(i);
            uuid_list.add(val);
        }
        return uuid_list;
    }

    /**
     * Gets the current date & time and returns it in string form using the following format:
     * dd.MM.yyyy:HH
     *
     * @return The string representation of the current date & time
     */
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy:HH:mm:ss", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        return dateFormat.format(currentDate);
    } // end getCurrentDate()

    /**
     * Separates the hour segment from a date string and converts and returns it as an int.
     * The hour string must be the last segment, preceded by a colon, eg: dd.MM.yyyy:HH.
     *
     * @param date The string representation of the date.
     * @return The hour segment in int form.
     */
    public static int getHourFromDate(String date) {
        return Integer.parseInt(date.split(":")[1]);
    } // end getHourFromDate


    /**
     * Determines the orientation of the accelerometer.
     *
     * @param x - Value of the X axis
     * @param y - Value of the Y axis
     * @param z - Value of the Z axis
     *
     * @return An integer (positions 1 through 5) to represent the orientation of the band.
     */
    static int getOrientation(float x, float y, float z) {
        float largest;
        double range = 0.5;
        int axis;

        // Find axis with largest absolute value
        if (Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)) {
            largest = x;
            axis = 1;
        } else if (Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)) {
            largest = y;
            axis = 2;
        } else {
            largest = z;
            axis = 3;
        }

        // Position 1: Tilted Right
        if (axis == 2 && largest < 0
                && largest > -1 - range && largest < -1 + range) {
            return 1;
        }
        // Position 2: Flat
        else if (axis == 3 && largest > 0
                && largest < 1 + range && largest > 1 - range) {
            return 2;
        }
        // Position 3: Tilt Left
        else if (axis == 2 && largest > 0
                && largest < 1 + range && largest > 1 - range) {
            return 3;
        }
        // Position 4: Upside Down
        else if (axis == 3 && largest < 0
                && largest > -1 - range && largest < -1 + range) {
            return 4;
        }
        // Position 5: Vertical Up or Vertical Down
        else if (axis == 1 && largest > 0
                && largest < 1 + range && largest > 1 - range) {
            return 5;
        }
        // Position 6: Vertical Up & Left
        else if (axis == 1 && largest < 0
                && largest > -1 - range && largest < -1 + range) {
            return 6;
        }
        // Unknown Position
        else {
            return 0;
        }
    } // end getOrientation

    /**
     * Recognises the actions in an orientation vector by analysing the vector's
     * orientation sequences.
     *
     * @param orientationVector - The orientation vector to be analysed.
     */
     static int recogniseActions(ArrayList<Integer> orientationVector) {
        // Reach and Retrieve
        if (orientationVector.get(0) == 3
                && orientationVector.get(1) == 2
                && orientationVector.get(2) == 3) {
            return 0;
        }
        // Reach to Mouth
        else if (orientationVector.get(0) == 3
                && orientationVector.get(1) == 6
                && orientationVector.get(2) == 3) {
            return 1;
        }
        // Wrist Rotation
        else if (orientationVector.get(0) == 4
                && orientationVector.get(1) == 3
                && orientationVector.get(2) == 2) {
            return 2;
        }

        // Stirring
        else if (orientationVector.get(0) == 2
                && orientationVector.get(1) == 5
                && orientationVector.get(2) == 2) {
            return 3;
        }

        return -1;
    } // end recogniseActions
}
