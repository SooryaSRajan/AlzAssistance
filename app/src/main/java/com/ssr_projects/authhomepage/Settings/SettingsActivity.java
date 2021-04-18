package com.ssr_projects.authhomepage.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.ssr_projects.authhomepage.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SwitchPreferenceCompat mListPreference;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            SharedPreferences sharedPreferences
                    = getActivity().getSharedPreferences(
                    "sharedPrefs", MODE_PRIVATE);
            final SharedPreferences.Editor editor
                    = sharedPreferences.edit();

            mListPreference = getPreferenceManager().findPreference("theme");
            if (mListPreference != null) {
                mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if ((Boolean) newValue) {
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_YES);

                            editor.putBoolean(
                                    "isDarkModeOn", true);

                        } else {
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_NO);

                            editor.putBoolean(
                                    "isDarkModeOn", false);

                        }
                        editor.apply();
                        getActivity().recreate();

                        return true;
                    }
                });
            }

            SwitchPreferenceCompat mListPreferenceTwo = getPreferenceManager().findPreference("audio_sos");
                if (mListPreferenceTwo != null) {
                    mListPreferenceTwo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Log.e("Settings", "onPreferenceChange: " + newValue  );
                            if ((Boolean) newValue) {
                                editor.putBoolean(
                                        "isSosAudioOn", true);

                            } else {

                                editor.putBoolean(
                                        "isSosAudioOn", false);

                            }
                            editor.apply();
                            return true;
                        }
                    });
                }

            SwitchPreferenceCompat mListPreferenceThree = getPreferenceManager().findPreference("torch_sos");
                if (mListPreferenceThree != null) {
                    mListPreferenceThree.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Log.e("Settings", "onPreferenceChange: " + newValue  );
                            if ((Boolean) newValue) {
                                editor.putBoolean(
                                        "isSosTorchOn", true);

                            } else {

                                editor.putBoolean(
                                        "isSosTorchOn", false);

                        }
                            editor.apply();
                            return true;
                        }
                    });
                }


            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}