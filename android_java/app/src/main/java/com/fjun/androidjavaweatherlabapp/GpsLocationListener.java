package com.fjun.androidjavaweatherlabapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;

/**
 * Wrapper around Location Listener, which also handle check for permissions
 */
public class GpsLocationListener {

    public interface Callback {
        void onLocation(Location location);
    }

    private static final long TIME_BETWEEN_UPDATES_MS = 60 * 1000;
    private final Activity mContext;
    private final LocationManager mLocationManager;
    @Nullable
    private Callback mCallback;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mCallback != null) {
                mCallback.onLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Inject
    GpsLocationListener(Activity context, LocationManager locationManager) {
        this.mContext = context;
        this.mLocationManager = locationManager;
    }

    /**
     * Register for location callbacks given a listener, at max TIME_BETWEEN_UPDATES_MS
     * Will first check that the user have permissions to get location.
     * //TODO (johboh): If the user does not have permission but accepts it, no registration will happen.
     */
    public void registerListener(Callback callback) {
        mCallback = callback;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_BETWEEN_UPDATES_MS, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_BETWEEN_UPDATES_MS, 0, mLocationListener);
        }
    }

    /**
     * Remove a location listener. The listener will no longer receive callbacks.
     */
    public void unregisterListener() {
        mLocationManager.removeUpdates(mLocationListener);
    }
}
