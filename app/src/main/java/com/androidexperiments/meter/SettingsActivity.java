package com.androidexperiments.meter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.androidexperiments.meter.util.Theme;
import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.color_battery_background)
    ImageView color_battery_background;

    @Bind(R.id.color_background_decharge)
    ImageView color_background_decharge;

    @Bind(R.id.color_foreground_decharge)
    ImageView color_foreground_decharge;

    @Bind(R.id.color_background_charging)
    ImageView color_background_charging;

    @Bind(R.id.color_foreground_charging)
    ImageView color_foreground_charging;

    @Bind(R.id.color_foreground_critical)
    ImageView color_foreground_critical;

    @Bind(R.id.color_background_critical)
    ImageView color_background_critical;

    private ChromaDialog chromaDialog;

    Theme theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(SettingsActivity.this);

        theme = new Theme(getBaseContext());
        /**
         * Setting colors initially
         */
        color_battery_background.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_battery_background))));
        color_background_decharge.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_background_decharge))));
        color_foreground_decharge.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_foreground_decharge))));
        color_background_charging.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_background_charging))));
        color_foreground_charging.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_foreground_charging))));
        color_foreground_critical.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_foreground_critical))));
        color_background_critical.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(Theme.KEY_color_background_critical))));

    }

    @OnClick(R.id.color_battery_background)
    public void onClick1() {
        showColorPickerDialog(Theme.KEY_color_battery_background, color_battery_background);
    }

    @OnClick(R.id.color_background_decharge)
    public void onClick2() {
        showColorPickerDialog(Theme.KEY_color_background_decharge, color_background_decharge);
    }

    @OnClick(R.id.color_foreground_decharge)
    public void onClick3() {
        showColorPickerDialog(Theme.KEY_color_foreground_decharge, color_foreground_decharge);
    }

    @OnClick(R.id.color_background_charging)
    public void onClick4() {
        showColorPickerDialog(Theme.KEY_color_background_charging, color_background_charging);
    }

    @OnClick(R.id.color_foreground_charging)
    public void onClick5() {
        showColorPickerDialog(Theme.KEY_color_foreground_charging, color_foreground_charging);
    }

    @OnClick(R.id.color_foreground_critical)
    public void onClick6() {
        showColorPickerDialog(Theme.KEY_color_foreground_critical, color_foreground_critical);
    }

    @OnClick(R.id.color_background_critical)
    public void onClick7() {
        showColorPickerDialog(Theme.KEY_color_background_critical, color_background_critical);
    }


    private void showColorPickerDialog(final String KEY, final ImageView imageView) {
        chromaDialog = new ChromaDialog.Builder()
                .initialColor(Color.parseColor(theme.getString(KEY)))
                .colorMode(ColorMode.ARGB)
                .indicatorMode(IndicatorMode.HEX)
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(@ColorInt int color) {
                        String hexColor = String.format("#%06X", (0xFFFFFFFF & color));
                        theme.save(KEY, hexColor);
                        imageView.setImageDrawable(new ColorDrawable(Color.parseColor(theme.getString(KEY))));
                        chromaDialog.dismiss();
                    }
                })
                .create();
        chromaDialog.show(getSupportFragmentManager(), "ChromaDialog");
    }
}