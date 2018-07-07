package com.fjun.androidjavaweatherlabapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fjun.androidjavaweatherlabapp.Yr.Symbol;
import com.fjun.androidjavaweatherlabapp.Yr.Temperature;
import com.fjun.androidjavaweatherlabapp.Yr.Yr;

import java.util.Locale;

import static com.fjun.androidjavaweatherlabapp.Yr.YrWeatherService.IMAGE_URL;

/**
 * Presenter for main activity. Presents temperature and icon.
 * Listens for location changes and update weather when location has changed.
 */
public class MainActivityPresenter implements LifecycleObserver {

    private final GpsLocationListener mGpsLocationListener;
    private final Yr mYr;

    @Nullable
    private MainActivityViewBinder mViewBinder;

    MainActivityPresenter(GpsLocationListener gpsLocationListener, Yr yr) {
        mGpsLocationListener = gpsLocationListener;
        mYr = yr;
    }

    /**
     * Set the view binder for this presenter.
     */
    public void attachView(MainActivityViewBinder mainActivityViewBinder) {
        mViewBinder = mainActivityViewBinder;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        mGpsLocationListener.registerListener(location -> mYr.requestWeather(location, new Yr.Callback() {
            @Override
            public void onTemperature(@NonNull Temperature temperature, @Nullable Symbol symbol) {
                mViewBinder.setTemperature(String.format(Locale.US, "%s %s", temperature.value, temperature.unit));
                if (symbol != null) {
                    mViewBinder.setImageUrl(String.format(Locale.US, IMAGE_URL, symbol.number));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                mViewBinder.setTemperature(":( " + throwable.getMessage());
            }
        }));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mGpsLocationListener.unregisterListener();
    }
}