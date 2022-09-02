package org.sensors2.pd.activities;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.sensors2.pd.Bundling;
import org.sensors2.pd.R;
import org.sensors2.pd.fragments.HelpSensorGroupFragment;
import org.sensors2.pd.sensors.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by thomas on 12.11.14.
 */
public class GuideActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		TextView availableSensorsHeadline = findViewById(R.id.availSensorsHeadline);
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Parameters> sensors = GetSensors(sensorManager);
		availableSensorsHeadline.setText(sensors.size() + " " + availableSensorsHeadline.getText());
		for (Parameters parameters : sensors) {
			this.CreateSensorFragments(parameters);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void CreateSensorFragments(Parameters parameters) {
		FragmentManager manager = getSupportFragmentManager();
		HelpSensorGroupFragment groupFragment = (HelpSensorGroupFragment) manager.findFragmentByTag(parameters.getSensorName());
		if (groupFragment == null) {
			this.CreateFragment(parameters, manager);
		}
	}

	private void CreateFragment(Parameters parameters, FragmentManager manager) {
		FragmentTransaction transaction = manager.beginTransaction();
		HelpSensorGroupFragment groupFragment = new HelpSensorGroupFragment();
		Bundle args = new Bundle();
		args.putInt(Bundling.DIMENSIONS, parameters.getDimensions());
		args.putInt(Bundling.SENSOR_TYPE, parameters.getSensorType());
		args.putString(Bundling.SENSOR_NAME, parameters.getSensorName());
		args.putFloat(Bundling.RANGE, parameters.getRange());
		args.putFloat(Bundling.RESOLUTION, parameters.getResolution());
		groupFragment.setArguments(args);
		transaction.add(R.id.sensor_group, groupFragment, parameters.getSensorName());
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Respond to the action bar's Up/Home button
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public List<Parameters> GetSensors(SensorManager manager) {
		List<Parameters> parameters = new ArrayList<>();
		for (Sensor sensor : manager.getSensorList(Sensor.TYPE_ALL)) {
			parameters.add(new Parameters(sensor));
		}
		return parameters;
	}
}