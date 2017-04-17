package wearable_ml.qut.wearable_machine_learning;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readAccData();
    }

    /**
     * Determines the orientation of the accelerometer.
     *
     * @param x - Value of the X axis
     * @param y - Value of the Y axis
     * @param z - Value of the Z axis
     *
     * @return An integer (positions 1 through 5) to represent the orientation of the band.
     */
    private int getOrientation(float x, float y, float z) {
        float largest;
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
                && largest > -1.5 && largest < -0.5) {
            return 1;
        }
        // Position 2: Flat
        else if (axis == 3 && largest > 0
                && largest < 1.5 && largest > 0.5) {
            return 2;
        }
        // Position 3: Tilt Left
        else if (axis == 2 && largest > 0
                && largest < 1.5 && largest > 0.5) {
            return 3;
        }
        // Position 4: Upside Down
        else if (axis == 3 && largest < 0
                && largest > -1.5 && largest < -0.5) {
            return 4;
        }
        // Position 5: Vertical Up or Vertical Down
        else if (axis == 1 && largest > 0
                && largest < 1.5 && largest > 0.5) {
            return 5;
        }
        // Unknown Position
        else {
            return 0;
        }
    } // end getOrientation

    /**
     * Filter the contents of an orientation vector.  All zeros are removed as well as
     * any sequence with a length less than 13.
     *
     * @param unfilteredOV - Original, unfiltered orientation vector.
     *
     * @return - The filtered orientation vector.
     */
    private List<Integer> filterOrientationVector(List<Integer> unfilteredOV) {
        List<Integer> filtered = new ArrayList<>();
        int repetitions = 0;
        int lastOrientation = 0;

        StringBuilder sbPre = new StringBuilder();
        StringBuilder sbPost = new StringBuilder();

        // Remove zeros
        for (Iterator<Integer> i = unfilteredOV.iterator(); i.hasNext();) {
            int orientation = i.next();
            sbPre.append(Integer.toString(orientation));

            if (orientation == 0) {
                i.remove();
            }
        }

        // Only add orientations which repeat more than 13 times
        for (Integer orientation : unfilteredOV) {
            String strOrientation = Integer.toString(orientation);

            if (orientation == lastOrientation) {
                repetitions++;
                if (repetitions == 13) {
                    filtered.add(orientation);
                    sbPost.append(strOrientation);
                }
            } else {
                repetitions = 0;
            }
            lastOrientation = orientation;
        }

        Log.d("Pre-filtered Vector: ", sbPre.toString() + "\n");
        Log.d("Post-filtered Vector: ", sbPost.toString() + "\n");

        return filtered;
    } // end filterOrientationVector


    /**
     * Recognises the actions in an orientation vector by analysing the vector's
     * orientation sequences.
     *
     * @param orientationVector - The orientation vector to be analysed.
     */
    private void recogniseActions(List<Integer> orientationVector) {
        List<Character> actions = new ArrayList<>();

        for (int i = 0; i < orientationVector.size(); i++) {
            // Action A
            if (orientationVector.get(i) == 1 && orientationVector.get(i + 1) == 2) {
                if (orientationVector.get(i + 2) == 1) {
                    i = i + 2;
                }
                i = i + 1;
                actions.add('A');
            }
            // Action B
            else if (orientationVector.get(i) == 1
                    && orientationVector.get(i + 1) == 5
                    && orientationVector.get(i + 2) == 1) {
                i = i + 2;
                actions.add('B');
            }
            // Action C
            else {
                actions.add('C');
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Character c : actions) {
            sb.append(c);
        }
        Log.d("Action Sequence: ", sb.toString());
    } // end recogniseActions


    private void readAccData() {
        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("fake_acc_data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            List<Integer> orientationVector = new ArrayList<>();

            // Get all orientations and generate orientation vector
            while ((line = br.readLine()) != null) {
                String[] accVals = line.split(",");

                int orientation = getOrientation(Float.parseFloat(accVals[1]),
                        Float.parseFloat(accVals[2]),
                        Float.parseFloat(accVals[3]));
                orientationVector.add(orientation);

                Log.d("Acc Line = ", accVals[1] + "," + accVals[2] + "," + accVals[3] + "\n" +
                        "Orientation = " + Integer.toString(orientation));
            }

            List<Integer> filteredOV = filterOrientationVector(orientationVector);
            recogniseActions(filteredOV);

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end readAccData
}
