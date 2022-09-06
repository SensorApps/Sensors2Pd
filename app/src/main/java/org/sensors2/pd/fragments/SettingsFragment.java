package org.sensors2.pd.fragments;

import android.os.Bundle;

import org.sensors2.pd.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.common_preferences, rootKey);
    }
}