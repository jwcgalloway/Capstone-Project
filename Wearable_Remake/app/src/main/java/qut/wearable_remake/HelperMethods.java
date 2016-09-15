package qut.wearable_remake;

import android.app.Activity;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;

class HelperMethods {

    /**
     * TODO Check if previous install
     * Checks to see if there is an existing instance of the application.
     *
     * @return True is installed, otherwise false.
     */
    public static boolean isInstalled() {
        return false;
    }

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
}
