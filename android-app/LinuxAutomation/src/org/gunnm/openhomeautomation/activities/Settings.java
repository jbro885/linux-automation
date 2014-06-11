package org.gunnm.openhomeautomation.activities;

import org.gunnm.openhomeautomation.R;
import org.gunnm.openhomeautomation.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
    }


}
