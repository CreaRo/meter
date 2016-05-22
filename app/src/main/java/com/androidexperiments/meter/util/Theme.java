package com.androidexperiments.meter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rish on 22/5/16.
 */
public class Theme {

    private static final String DEFAULT_COLOR_MAIN_BACKGROUND = "#330e30";
    private static final String DEFAULT_color_background_decharge = "#481c6b";
    private static final String DEFAULT_color_foreground_decharge = "#e161e5";
    private static final String DEFAULT_color_background_charging = "#481c6b";
    private static final String DEFAULT_color_foreground_charging = "#e7e6e8";
    private static final String DEFAULT_color_foreground_critical = "#ef2074";
    private static final String DEFAULT_color_background_critical = "#7f1838";

    public static final String KEY_color_battery_background = "color_battery_background";
    public static final String KEY_color_background_decharge = "color_background_decharge";
    public static final String KEY_color_foreground_decharge = "color_foreground_decharge";
    public static final String KEY_color_background_charging = "color_background_charging";
    public static final String KEY_color_foreground_charging = "color_foreground_charging";
    public static final String KEY_color_foreground_critical = "color_foreground_critical";
    public static final String KEY_color_background_critical = "color_background_critical";

    private Context mContext;
    private SharedPreferences mPrefs;

    public Theme(Context context) {
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
        if (name.equals(KEY_color_battery_background)) {
            return sharedPreferences().getString(KEY_color_battery_background, DEFAULT_COLOR_MAIN_BACKGROUND);
        } else if (name.equals(KEY_color_background_decharge)) {
            return sharedPreferences().getString(KEY_color_background_decharge, DEFAULT_color_background_decharge);
        } else if (name.equals(KEY_color_foreground_decharge)) {
            return sharedPreferences().getString(KEY_color_foreground_decharge, DEFAULT_color_foreground_decharge);
        } else if (name.equals(KEY_color_background_charging)) {
            return sharedPreferences().getString(KEY_color_background_charging, DEFAULT_color_background_charging);
        } else if (name.equals(KEY_color_foreground_charging)) {
            return sharedPreferences().getString(KEY_color_foreground_charging, DEFAULT_color_foreground_charging);
        } else if (name.equals(KEY_color_foreground_critical)) {
            return sharedPreferences().getString(KEY_color_foreground_critical, DEFAULT_color_foreground_critical);
        } else if (name.equals(KEY_color_background_critical)) {
            return sharedPreferences().getString(KEY_color_background_critical, DEFAULT_color_background_critical);
        } else {
            return DEFAULT_COLOR_MAIN_BACKGROUND;
        }
    }

}
