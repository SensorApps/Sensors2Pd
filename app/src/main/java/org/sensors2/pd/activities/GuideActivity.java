package org.sensors2.pd.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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
		TextView textViewAvailableSensors = (TextView) findViewById(R.id.textViewAvailableSensors);
		StringBuilder availableSensors = new StringBuilder();
		availableSensors.append(listSensor.size()+" "+textViewAvailableSensors.getText());
		for(Sensor sensor : listSensor){
			int sensorId = sensor.getType();
			availableSensors.append("\n"+sensor.getName()+
					"\n max range: "+sensor.getMaximumRange()+
					"\n resolution: "+sensor.getResolution()+
					"\n send: sensor"+sensorId+"v0; sensor"+sensorId+"v1; sensor"+sensorId+"v2;\n");
		}
		textViewAvailableSensors.setText(availableSensors);
	}

}
