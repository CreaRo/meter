package com.androidexperiments.meter.drawers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.BatteryManager;

import com.androidexperiments.meter.R;
import com.androidexperiments.meter.util.Theme;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class BatteryCircleDrawer extends Drawer {
    private final String TAG = this.getClass().getSimpleName();

    private float batteryPct;
    private float _batteryPct = -1;

    private double colorTransitionToCharged = 0;
    private double _colorTransitionToCharged = 0;

    private double colorTransitionToCritical = 0;
    private double _colorTransitionToCritical = 0;

    double circleSize = 0.7 * 0.5;

    // Colors
    private final String color_battery_background;
    private final String color_background_decharge;
    private final String color_foreground_decharge;

    private final String color_background_charging;
    private final String color_foreground_charging;

    private final String color_background_critical;
    private final String color_foreground_critical;

    private Paint paint = new Paint();

    // Movement
    Vector2D pos, _pos;
    Vector2D vel;

    Theme theme;

    public BatteryCircleDrawer(Context mContext) {
        super(mContext);

        // Register a receiver for battery state changes
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(batteryLevelReceiver, ifilter);

        // Read initial battery status
        batteryPct = getBatteryPct(batteryStatus);

        if (getBatteryIsCharging(batteryStatus)) {
            _colorTransitionToCharged = colorTransitionToCharged = 1.0;
        } else {
            _colorTransitionToCharged = colorTransitionToCharged = 0.0;
            if (batteryPct <= 0.15) _colorTransitionToCritical = colorTransitionToCritical = 1.0;
        }

        theme = new Theme(mContext);

        // Load the colors
        color_battery_background = theme.getString(Theme.KEY_color_battery_background);

        color_background_decharge = theme.getString(Theme.KEY_color_background_decharge);
        color_foreground_decharge = theme.getString(Theme.KEY_color_foreground_decharge);

        color_background_charging = theme.getString(Theme.KEY_color_background_charging);
        color_foreground_charging = theme.getString(Theme.KEY_color_foreground_charging);

        color_foreground_critical = theme.getString(Theme.KEY_color_foreground_critical);
        color_background_critical = theme.getString(Theme.KEY_color_background_critical);

        this.textColor = mContext.getResources().getColor(R.color.battery_text);
    }

    /**
     * The battery level change receiver
     */
    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryPct = getBatteryPct(intent);

            // Are we charging?
            if (getBatteryIsCharging(intent)) {
                colorTransitionToCharged = 1.0;
                colorTransitionToCritical = 0.0;
            } else {
                colorTransitionToCharged = 0.0;

                if (batteryPct <= 0.15) {
                    colorTransitionToCritical = 1.0;
                } else {
                    colorTransitionToCritical = 0.0;
                }
            }
        }
    };

    /**
     * Function that determines if the canvas needs to be redrawn
     */
    public boolean shouldDraw() {
        boolean redraw = super.shouldDraw();

        if (vel == null) vel = new Vector2D(0, 0);
        if (pos == null) pos = new Vector2D(0, 0);
        if (_pos == null) _pos = new Vector2D(0, 0);

        Vector2D a = new Vector2D(mOrientation[2] * 0.01, -mOrientation[1] * 0.01);
        a = a.add(pos.scalarMultiply(-0.01));

        vel = vel.scalarMultiply(0.9);
        vel = vel.add(a);
        pos = pos.add(vel);

        float dist = (float) pos.distance(new Vector2D(0, 0));
        float maxDist = (float) (circleSize - circleSize * batteryPct);
        if (dist > maxDist) {
            Vector2D n = pos.normalize().scalarMultiply(-1);
            Vector2D reflection = vel.subtract(n.scalarMultiply(2 * vel.dotProduct(n)));

            pos = n.scalarMultiply(-maxDist);

            vel = reflection.scalarMultiply(0.5);
        }

        if (_colorTransitionToCharged != colorTransitionToCharged || _colorTransitionToCritical != _colorTransitionToCritical) {
            redraw = true;
        }
        if (_batteryPct != batteryPct) {
            redraw = true;
        }
        if (_textAlpha != textAlpha) {
            redraw = true;
        }
        if (_pos.distance(pos) > 0.001) {
            redraw = true;
        }

        if (redraw) {
            // Circle color
            _colorTransitionToCharged = animateValue(_colorTransitionToCharged, colorTransitionToCharged, 0.03);
            _colorTransitionToCritical = animateValue(_colorTransitionToCritical, colorTransitionToCritical, 0.03);
            _pos = pos;

            _batteryPct = batteryPct;
            return true;
        }

        return false;
    }

    /**
     * Draw the circle to the canvas
     */
    public void draw(Canvas c) {
        super.draw(c);

        paint.setAntiAlias(true);

        // Background
        paint.setColor(getColor(color_battery_background));
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), paint);

        int x = c.getWidth() / 2;
        int y = c.getHeight() / 2 - (int) (30f * pixelDensity);
        float _circleSize = (float) (c.getWidth() * circleSize);

        // Outer circle
        int bgCircleColor = interpolateColor(getColor(color_background_decharge), getColor(color_background_charging), (float) lerp(_colorTransitionToCharged));
        bgCircleColor = interpolateColor(bgCircleColor, getColor(color_background_critical), (float) lerp(_colorTransitionToCritical));
        paint.setColor(bgCircleColor);
        c.drawCircle(x, y, _circleSize, paint);

        // Inner circle
        int fgCircleColor = interpolateColor(getColor(color_foreground_decharge), getColor(color_foreground_charging), (float) lerp(_colorTransitionToCharged));
        fgCircleColor = interpolateColor(fgCircleColor, getColor(color_foreground_critical), (float) lerp(_colorTransitionToCritical));
        paint.setColor(fgCircleColor);
        c.drawCircle((float) (x + c.getWidth() * pos.getX()), (float) (y + c.getWidth() * pos.getY()), _circleSize * batteryPct, paint);

        // Text
        String label1 = "Battery " + Integer.toString(Math.round(batteryPct * 100)) + "%";
        drawText(label1, "", x, (int) (y + circleSize * c.getWidth()), c);
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

    private int getColor(String color) {
        return Color.parseColor(color);
    }
}
