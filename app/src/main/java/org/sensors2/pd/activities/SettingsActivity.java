package org.sensors2.pd.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import org.sensors2.pd.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NavUtils;

/**
 * Created by thomas on 12.11.14.
 */
public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(org.sensors2.common.R.xml.common_preferences);
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
}