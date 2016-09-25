package qut.wearable_remake;

import android.app.Activity;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class HelperMethods {

    /**
     * Checks to see if there is an existing instance of the application by checking if the internal
     * save files already exist.
     *
     * @param context Context of the current page.
     * @param fileName Name of internally saved file to be checked if exists.
     * @return True is installed, otherwise false.
     */
    static boolean isInstalled(Context context, String fileName) {
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
    static void writeToFile(String filename, String content, Activity activity) {
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
        FileInputStream stream = new FileInputStream(file);
        String str = streamToString(stream);
        stream.close();

        PrintWriter pw = new PrintWriter(filePath);
        pw.close();

        return str;
    } // end getDataFromFile()
}
