package org.sensors2.pd.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sensors2.pd.Bundling;
import org.sensors2.pd.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by thomas on 09.11.14.
 */
public class HelpSensorGroupFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = this.getArguments();
		assert args != null;
		int dimensions = args.getInt(Bundling.DIMENSIONS);
		int sensorType = args.getInt(Bundling.SENSOR_TYPE);
		String sensorName = args.getString(Bundling.SENSOR_NAME);
		@SuppressLint("InflateParams") View v = inflater.inflate(R.layout.help_sensor_group, null);
		TextView groupName = v.findViewById(R.id.group_name);
		groupName.setText(sensorName);
		AddText(v.findViewById(R.id.range), args.getFloat(Bundling.RANGE));
		AddText(v.findViewById(R.id.resolution), args.getFloat(Bundling.RESOLUTION));
		CreateSensors(sensorType, dimensions);
		return v;
	}

	private void AddText(TextView view, float val) {
		view.setText(view.getText() + ": " + val);
	}

	private void CreateSensors(int sensorType, int dimensions) {
		for (int i = 0; i < dimensions; i++) {
			FragmentManager manager = getChildFragmentManager();
			String fragmentTag = sensorType +";" +i;
			HelpSensorFragment sensorFragment = (HelpSensorFragment) manager.findFragmentByTag(fragmentTag);
			if (sensorFragment == null) {
				CreateSensorFragment(manager, fragmentTag, sensorType, i);
			}
		}
	}

	private void CreateSensorFragment(FragmentManager manager, String fragmentTag, int sensorType, int index) {
		FragmentTransaction transaction = manager.beginTransaction();
		HelpSensorFragment sensorFragment = new HelpSensorFragment();
		Bundle args = new Bundle();
		args.putInt(Bundling.SENSOR_TYPE, sensorType);
		args.putInt(Bundling.INDEX, index);
		sensorFragment.setArguments(args);
		transaction.add(R.id.sensor_list, sensorFragment, fragmentTag);
		transaction.commit();
	}
}
