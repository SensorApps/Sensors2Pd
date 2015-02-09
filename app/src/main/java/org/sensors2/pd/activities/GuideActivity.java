package org.sensors2.pd.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import org.sensors2.pd.R;

import java.util.List;

/**
 * Created by thomas on 12.11.14.
 */
public class GuideActivity extends Activity {


	protected SensorManager mSensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		// Sensors
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> listSensor = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		TextView availableSensorsHeadline = (TextView) findViewById(R.id.availSensorsHeadline);
		availableSensorsHeadline.setText(listSensor.size()+" "+availableSensorsHeadline.getText());
		TextView textViewAvailableSensors = (TextView) findViewById(R.id.textViewAvailableSensors);
		StringBuilder availableSensors = new StringBuilder();
		for(Sensor sensor : listSensor){
			int sensorId = sensor.getType();
			availableSensors.append("\n"+sensor.getName()+
					"\n max range: "+sensor.getMaximumRange()+
					"\n resolution: "+sensor.getResolution()+
					"\n send: sensor"+sensorId+"v0; sensor"+sensorId+"v1; sensor"+sensorId+"v2;\n");
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

}
