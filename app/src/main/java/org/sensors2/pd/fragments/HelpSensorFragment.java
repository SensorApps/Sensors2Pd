package org.sensors2.pd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sensors2.pd.Bundling;
import org.sensors2.pd.R;

public class HelpSensorFragment extends Fragment {

	public HelpSensorFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_single_sensor, null);
		Bundle args = this.getArguments();
		((TextView) view.findViewById(R.id.name)).setText("sensor" + args.getInt(Bundling.SENSOR_TYPE) + "v" + args.getInt(Bundling.INDEX));
		return view;
	}
}