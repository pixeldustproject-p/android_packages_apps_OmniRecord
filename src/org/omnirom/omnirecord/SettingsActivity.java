/*
 *  Copyright (C) 2018 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.omnirom.omnirecord;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String PREF_RECORD_MODE = "record_mode";
    private SharedPreferences mPrefs;
    private ListPreference mRecordMode;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        addPreferencesFromResource(R.xml.settings);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mRecordMode = (ListPreference) findPreference(PREF_RECORD_MODE);
        mRecordMode.setOnPreferenceChangeListener(this);
        int idx = mRecordMode.findIndexOfValue(mPrefs.getString(PREF_RECORD_MODE,
                mRecordMode.getEntryValues()[1].toString()));
        mRecordMode.setValueIndex(idx);
        mRecordMode.setSummary(mRecordMode.getEntries()[idx]);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mRecordMode) {
            String value = (String) newValue;
            int idx = mRecordMode.findIndexOfValue(value);
            mRecordMode.setSummary(mRecordMode.getEntries()[idx]);
            mRecordMode.setValueIndex(idx);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
