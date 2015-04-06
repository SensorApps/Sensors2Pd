package org.sensors2.pd.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.sensors.Parameters;
import org.sensors2.common.sensors.SensorActivity;
import org.sensors2.common.sensors.SensorCommunication;
import org.sensors2.common.touch.TouchActivity;
import org.sensors2.common.wifi.WifiActivity;
import org.sensors2.pd.R;
import org.sensors2.pd.filesystem.FileLoader;
import org.sensors2.pd.filesystem.FileSelector;
import org.sensors2.pd.sensors.PdDispatcher;
import org.sensors2.pd.sensors.Settings;
import org.sensors2.pd.touch.TouchCommunication;
import org.sensors2.pd.wifi.WifiCommunication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 12.11.14.
 */
public class Sensors2PdActivity extends Activity implements SensorEventListener, SensorActivity, WifiActivity, TouchActivity, FileSelector.FileSelectorListener {

	private Settings settings;
	private PdDispatcher dispatcher;
	private SensorManager sensorManager;
	private SensorCommunication sensorFactory;
	private WifiManager wifiManager;
	private WifiCommunication wifiFactory;
	private View touchView;
	private TouchCommunication touchFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors2pd);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		this.settings = this.loadSettings();
		this.dispatcher = new PdDispatcher(this);
		// Sensors
		this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.sensorFactory = new SensorCommunication(this);
		// Wifi
		this.wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		this.wifiFactory = new WifiCommunication(this);
		// Touch
		this.touchView = findViewById(R.id.scrollView1);
		this.touchFactory = new TouchCommunication(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.sensorFactory.onPause();
		this.dispatcher.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.loadSettings();
		this.dispatcher.onResume();
		this.sensorFactory.onResume();
	}

	private Settings loadSettings() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Settings settings = new Settings(preferences);
		return settings;
	}

	@Override
	public List<Parameters> GetSensors(SensorManager manager) {
		List<Parameters> parameters = new ArrayList<Parameters>();
		for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
			parameters.add(new org.sensors2.pd.sensors.Parameters(sensor));
		}
		return parameters;
	}

	@Override
	public DataDispatcher getDispatcher() {
		return this.dispatcher;
	}

	@Override
	public SensorManager getSensorManager() {
		return this.sensorManager;
	}

	@Override
	public View getTouchView() {
		return this.touchView;
	}

	@Override
	public Settings getSettings() {
		return this.settings;
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		this.sensorFactory.dispatch(sensorEvent);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sensors2_pd, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_browse:
				FileSelector loader = new FileSelector(this, this);
				loader.chooseFile();
				return true;
			case R.id.action_guide: {
				Intent intent = new Intent(this, GuideActivity.class);
				startActivity(intent);
				return true;
			}
			case R.id.action_settings: {
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			}
			case R.id.action_about: {
				Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onChosenFile(String filePath) {
		FileLoader loader = new FileLoader(filePath, this);
		File loadedFile = null;
		try {
			loadedFile = loader.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (this.dispatcher.setPdFile(loadedFile)) {
			findViewById(R.id.runningPdFileIntro).setVisibility(View.VISIBLE);
			TextView view = (TextView) findViewById(R.id.runningPdFile);
			view.setText(loadedFile.getName());
			view.setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.runningPdFileIntro).setVisibility(View.INVISIBLE);
			findViewById(R.id.runningPdFile).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public WifiManager getWifiManager() {
		return wifiManager;
	}
}
