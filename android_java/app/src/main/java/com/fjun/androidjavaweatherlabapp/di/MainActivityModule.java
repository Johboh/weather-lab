package com.fjun.androidjavaweatherlabapp.di;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import com.fjun.androidjavaweatherlabapp.MainActivity;
import com.fjun.androidjavaweatherlabapp.MainActivityViewBinder;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    @Provides
    public static Activity provideActivity(MainActivity mainActivity) {
        return mainActivity;
    }

    @Provides
    public static LocationManager provideLocationManager(MainActivity mainActivity) {
        return (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Binds
    public abstract MainActivityViewBinder bindMainActivityViewBinder(MainActivity mainActivity);
}
