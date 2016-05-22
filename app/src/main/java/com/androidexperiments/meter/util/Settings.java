package com.androidexperiments.meter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rish on 22/5/16.
 */
public class Settings {

    public static final String KEY_MODE_SELECTED = "mode_selected";

    private static final String DEFAULT_MODE_SELECTED = "";

    public static final String MODE_TRIANGLE = "mode_triangle";
    public static final String MODE_CIRCLE = "mode_circle";

    private Context mContext;
    private SharedPreferences mPrefs;

    public Settings(Context context) {
        mContext = context;
    }

    private SharedPreferences sharedPreferences() {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mPrefs;
    }

    public void save(String name, String value) {
        sharedPreferences().edit().putString(name, value).apply();
    }

    public String getString(String name) {
        if (name.equals(KEY_MODE_SELECTED)) {
            return sharedPreferences().getString(name, MODE_CIRCLE);
        } else {
            return "";
        }

    }
}
