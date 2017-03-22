package qut.wearable_remake;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ViewSwitcher;

import com.microsoft.band.BandClient;

import qut.wearable_remake.sensors.SensorInterface;

public class SettingsFragment extends Fragment {

    private Switch liveGraphingSwitch;
    private Switch sendHapticsSwitch;
    private ViewSwitcher chartSwitcher;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final EditText moveGoalEditTxt = (EditText) rootView.findViewById(R.id.moveGoalEditTxt);
        ((MainActivity)getActivity()).fragId = 1;

        //moveGoalEditTxt.setText((WearableApplication).getActivity()).getMoveGoal();

        ((WearableApplication) getActivity().getApplication())
                .setMoveGoal(Integer.parseInt(moveGoalEditTxt.getText().toString()));

        Button moveGoalBtn = (Button) rootView.findViewById(R.id.moveGoalBtn);
        moveGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WearableApplication) getActivity().getApplication())
                        .setMoveGoal(Integer.parseInt(moveGoalEditTxt.getText().toString()));
                ((MainActivity)getActivity()).dailyMovesBullet.updateMoveGoalLine();
            }
        });

        Switch recordDataSwitch = (Switch) rootView.findViewById(R.id.recordDataSwitch);
        recordDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BandClient bandClient = ((MainActivity)getActivity()).projectClient.getBandClient();
                SensorInterface[] sensors = ((MainActivity)getActivity()).projectClient.getSensors();
                if (isChecked) {
                    for (SensorInterface sensor : sensors) {
                        sensor.registerListener(bandClient);
                    }
                } else {
                    for (SensorInterface sensor : sensors) {
                        sensor.unregisterListener(bandClient);
                    }
                }
            }
        });

        liveGraphingSwitch = (Switch) rootView.findViewById(R.id.liveGraphSwitch);
        sendHapticsSwitch = (Switch) rootView.findViewById(R.id.sendHapticsSwitch);

        Button removeTileBtn = (Button) rootView.findViewById(R.id.removeTileBtn);
        removeTileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).projectClient.removeTile();
            }
        });

        return rootView;
    }


}