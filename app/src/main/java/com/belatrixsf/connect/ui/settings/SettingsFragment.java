package com.belatrixsf.connect.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.belatrixsf.connect.R;

/**
 * Created by echuquilin on 6/07/16.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String NOTIFICATIONS_ENABLED_KEY = "settings_key_notifications_switch";

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        restorePreviusSettings();
    }

    private void restorePreviusSettings() {
        if (sharedPreferences.contains(NOTIFICATIONS_ENABLED_KEY)) {
            Preference notificationEnabledPref = findPreference(NOTIFICATIONS_ENABLED_KEY);
            notificationEnabledPref.setDefaultValue(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED_KEY,true));
        }
    }
}
