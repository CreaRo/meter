package com.androidexperiments.meter.drawers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.androidexperiments.meter.R;


public class BatteryTriangleDrawer extends TriangleFillDrawer {
    private final String TAG = this.getClass().getSimpleName();

    public BatteryTriangleDrawer(Context context) {
        super(
                context,
                context.getResources().getColor(R.color.notifications_color_3),
                context.getResources().getColor(R.color.wifi_triangle_background),
                context.getResources().getColor(R.color.wifi_triangle_foreground),
                context.getResources().getColor(R.color.wifi_triangle_critical)
        );

        this.label1 = "Battery";

        // Register a receiver for battery state changes
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(batteryLevelReceiver, ifilter);

        percent = getBatteryPct(batteryStatus);
        _percent = (float) (percent - 0.001);
    }

    /**
     * The battery level change receiver
     */
    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            percent = getBatteryPct(intent);
            label2 = context.getString(R.string.battery_label);
            _percent = (float) (percent - 0.001);

            // Are we charging?
            if (getBatteryIsCharging(intent)) {

            } else {

            }
        }
    };

    /**
     * Parse wifi strength from intent
     */
    public float getWifiStrength(Intent intent) {
        float level = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -1);
        level = WifiManager.calculateSignalLevel((int) level, 100);
        level /= 100.0;
        return level;
    }

    /**
     * Parse wifi connection status from intent
     */
    public boolean getWifiConnected(Intent intent) {
        NetworkInfo info = (NetworkInfo) intent.getExtras().get(WifiManager.EXTRA_NETWORK_INFO);

        if (info == null) return false;
        return info.getState().equals(NetworkInfo.State.CONNECTED);
    }

    /**
     * Parse the battery percentage from the battery change intent
     */
    public float getBatteryPct(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return level / (float) scale;
    }

    /**
     * Parse the battery charging status from the battery change intent
     */
    public boolean getBatteryIsCharging(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }
}