package com.androidexperiments.meter;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidexperiments.meter.util.RobotoBoldTypeface;
import com.androidexperiments.meter.util.RobotoLightTypeface;

/**
 * The Main app activity, describes the wallpaper and directs user towards notification settings
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected Button mSettingsButton;
    protected Button mSetWallpaperButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //grab button references
        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mSetWallpaperButton = (Button) findViewById(R.id.choseWallpaperButton);

        Typeface robotoLight = RobotoLightTypeface.getInstance(this);
        Typeface robotoBold = RobotoBoldTypeface.getInstance(this);
        mSetWallpaperButton.setTypeface(robotoBold);

        ((TextView) findViewById(R.id.descriptionTextView)).setTypeface(robotoLight);

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNotificationListenerSettings();
            }
        });

        mSetWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.open_settings:
                moveToNotificationListenerSettings();
                break;

            case R.id.about:
                moveToAbout();
                break;
            case R.id.licenses:
            default:
                moveToLicenses();
        }

        return true;
    }

    /**
     * go to the OS-level notification listener settings
     */
    private void moveToNotificationListenerSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * go to the about section
     */
    private void moveToAbout() {
        Intent intent = new Intent(this, LocalWebActivity.class);
        intent.putExtra(LocalWebActivity.EXTRA_HTML_URI, "html/about.html");
        startActivity(intent);
    }

    /**
     * go to the licenses section
     */
    private void moveToLicenses() {
        //go to Licenses html here
        Intent intent = new Intent(this, LocalWebActivity.class);
        intent.putExtra(LocalWebActivity.EXTRA_HTML_URI, "html/licenses.html");
        startActivity(intent);
    }

}
