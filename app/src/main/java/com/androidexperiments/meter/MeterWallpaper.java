package com.androidexperiments.meter;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.androidexperiments.meter.drawers.BatteryCircleDrawer;
import com.androidexperiments.meter.drawers.BatteryTriangleDrawer;
import com.androidexperiments.meter.drawers.Drawer;
import com.androidexperiments.meter.util.Settings;

/**
 * The Live Wallpaper Service and rendering Engine
 */
public class MeterWallpaper extends WallpaperService {

    private final String TAG = this.getClass().getSimpleName();

    private Drawer mDrawer;

    @Override
    public Engine onCreateEngine() {
        WallpaperEngine engine = new WallpaperEngine(this);

        return engine;
    }


    /**
     * The wallpaper engine that will handle the rendering
     */
    private class WallpaperEngine extends WallpaperService.Engine {

        public Context mContext;

        private boolean mVisible = false;
        private final Handler mHandler = new Handler();

        public WallpaperEngine(Context context) {
            this.mContext = context;
        }

        /**
         * Handle tap commands
         */
        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            //taps work on Nexus devices but not all, for example Samsung
            if (action.equals("android.wallpaper.tap")) {
                if (mDrawer != null) {
                    mDrawer.tap(x, y);
                }
            }
            return super.onCommand(action, x, y, z, extras, resultRequested);
        }

        /**
         * Draw runloop
         */
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        /**
         * Draw function doing the context locking and rendering
         */
        private void draw() {
            if (mDrawer == null) return;

            // Ask the drawer if wants to draw in this frame
            if (mDrawer.shouldDraw()) {
                SurfaceHolder holder = getSurfaceHolder();
                Canvas c = null;
                try {
                    // Lock the drawing canvas
                    c = holder.lockCanvas();
                    if (c != null) {
                        // Let the drawer render to the canvas
                        mDrawer.draw(c);
                    }
                } finally {
                    if (c != null) holder.unlockCanvasAndPost(c);
                }
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                // Wait one frame, and redraw
                mHandler.postDelayed(mUpdateDisplay, 33);
            }
        }


        /**
         * Toggle visibility of the wallpaper
         * In this function we create new drawers every time the wallpaper
         * is visible again, cycling through the available ones
         *
         * @param visible whether the wallpaper is currently visible
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                /**
                 * Check which mode is selected and display that
                 */
                Settings settings = new Settings(mContext);
                if (settings.getString(Settings.KEY_MODE_SELECTED).equals(Settings.MODE_CIRCLE)) {
                    mDrawer = new BatteryCircleDrawer(mContext);
                } else {
                    mDrawer = new BatteryTriangleDrawer(mContext);
                }
                mDrawer.start();

                // Start the drawing loop
                draw();
            } else {
                if (mDrawer != null) {
                    mDrawer.destroy();
                    mDrawer = null;
                }
                mHandler.removeCallbacks(mUpdateDisplay);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }
    }
}