package com.fjun.android_java_mobius.mobius;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fjun.android_java_mobius.GpsLocationListener;
import com.fjun.android_java_mobius.Yr.Symbol;
import com.fjun.android_java_mobius.Yr.Temperature;
import com.fjun.android_java_mobius.Yr.Yr;
import com.fjun.android_java_mobius.Yr.YrResult;
import com.spotify.mobius.Connection;
import com.spotify.mobius.functions.Consumer;

import javax.inject.Inject;

public class EffectHandler {

    private final GpsLocationListener mGpsLocationListener;
    private final Yr mYr;

    @Inject
    public EffectHandler(GpsLocationListener gpsLocationListener, Yr yr) {
        mGpsLocationListener = gpsLocationListener;
        mYr = yr;
    }

    public Connection<Effect> effectHandler(Consumer<Event> eventConsumer) {
        return new Connection<Effect>() {
            @Override
            public void accept(@NonNull Effect effect) {
                effect.match(
                        waitForLocation -> {
                            final Location location = mGpsLocationListener.getLastKnownLocation();
                            if (location == null) {
                                eventConsumer.accept(Event.gotError("No location at this time."));
                            } else {
                                eventConsumer.accept(Event.gotLocation(location));
                            }
                        },
                        requestWeather -> mYr.requestWeather(requestWeather.location(), new Yr.Callback() {
                            @Override
                            public void onTemperature(@NonNull Temperature temperature, @Nullable Symbol symbol) {
                                eventConsumer.accept(Event.gotWeather(new YrResult(temperature, symbol)));
                            }

                            @Override
                            public void onError(@NonNull Throwable throwable) {
                                eventConsumer.accept(Event.gotError(throwable.toString()));
                            }
                        }));
            }

            @Override
            public void dispose() {
            }
        };
    }
}
