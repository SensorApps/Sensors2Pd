package org.sensors2.pd.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.sensors2.common.sensors.DataDispatcher;
import org.sensors2.common.sensors.Parameters;
import org.sensors2.common.sensors.SensorActivity;
import org.sensors2.common.sensors.Settings;

import java.util.List;

/**
 * Created by thomas on 12.11.14.
 */
public class Sensors2PdActivity extends Activity implements SensorEventListener, SensorActivity, OnTouchListener {
	@Override
	public List<Parameters> GetSensors(SensorManager manager) {
		return null;
	}

	@Override
	public DataDispatcher getDispatcher() {
		return null;
	}

	@Override
	public SensorManager getSensorManager() {
		return null;
	}

	@Override
	public Settings getSettings() {
		return null;
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		return false;
	}
}
