package com.fjun.android_java_mobius.di;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import com.fjun.android_java_mobius.MainActivity;
import com.fjun.android_java_mobius.MainActivityViewBinder;

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
