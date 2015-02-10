package org.sensors2.pd.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import org.sensors2.pd.R;
import org.sensors2.pd.sensors.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 12.11.14.
 */
public class GuideActivity extends Activity {

	private SensorManager sensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		TextView availableSensorsHeadline = (TextView) findViewById(R.id.availSensorsHeadline);
		TextView textViewAvailableSensors = (TextView) findViewById(R.id.textViewAvailableSensors);
		this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		StringBuilder availableSensors = new StringBuilder();
		List<Parameters> sensors = GetSensors(sensorManager);
		availableSensorsHeadline.setText(sensors.size() + " " + availableSensorsHeadline.getText());
		for (Parameters parameters : sensors) {
			int sensorId = parameters.getSensorType();
			Sensor sensor = parameters.getSensor();
			availableSensors.append("\n" + sensor.getName() +
					"\n max range: " + sensor.getMaximumRange() +
					"\n resolution: " + sensor.getResolution() +
					"\n send: ");
			for (int i = 0; i < parameters.getDimensions(); i++) {
				availableSensors.append(" sensor" + sensorId + "v" + i);
			}
			availableSensors.append("\n");
		}
		textViewAvailableSensors.setText(availableSensors);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public List<Parameters> GetSensors(SensorManager manager) {
		List<Parameters> parameters = new ArrayList<Parameters>();
		for (Sensor sensor : manager.getSensorList(Sensor.TYPE_ALL)) {
			parameters.add(new Parameters(sensor));
		}
		return parameters;
	}
}
