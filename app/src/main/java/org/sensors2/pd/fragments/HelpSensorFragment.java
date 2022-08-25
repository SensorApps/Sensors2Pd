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

public class HelpSensorFragment extends Fragment {

	public HelpSensorFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		@SuppressLint("InflateParams") View view = inflater.inflate(R.layout.help_single_sensor, null);
		Bundle args = this.getArguments();
		assert args != null;
		((TextView) view.findViewById(R.id.name)).setText("sensor_" + args.getInt(Bundling.SENSOR_TYPE) + "_" + args.getInt(Bundling.INDEX));
		return view;
	}
}