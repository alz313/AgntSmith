package ua.in.out.agntsmith;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int FILE_SELECT_CODE = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Preference preference = findPreference(getString(R.string.pref_config_file_path_key));
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/xml");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        setConfigFilePathSummary(sharedPreferences);
        setClusterNameSummary(sharedPreferences);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String configPath = uri.getPath();

                    if (configPath != null) {
                        SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.pref_config_file_path_key), configPath);
                        editor.apply();

                    } else {
                        Toast.makeText(getActivity(), "Bad file path.\nTry other file Chooser!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_config_file_path_key))) {
            setConfigFilePathSummary(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_cluster_name_key))) {
            setClusterNameSummary(sharedPreferences);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setConfigFilePathSummary(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.pref_config_file_path_key), getString(R.string.pref_config_file_path_default));
        Preference preference = getPreferenceScreen().findPreference(getString(R.string.pref_config_file_path_key));
        preference.setSummary(value);
    }

    private void setClusterNameSummary(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.pref_cluster_name_key), getString(R.string.pref_cluster_name_default));
        Preference preference = getPreferenceScreen().findPreference(getString(R.string.pref_cluster_name_key));
        preference.setSummary(value);
    }


}
