package com.fjun.android_java_mobius.mobius;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fjun.android_java_mobius.Yr.Symbol;
import com.fjun.android_java_mobius.Yr.Temperature;
import com.fjun.android_java_mobius.Yr.Yr;
import com.fjun.android_java_mobius.Yr.YrResult;
import com.spotify.mobius.Connection;
import com.spotify.mobius.functions.Consumer;

import javax.inject.Inject;

public class EffectHandler {

    private final Yr mYr;

    @Inject
    EffectHandler(Yr yr) {
        mYr = yr;
    }

    public Connection<Effect> effectHandler(Consumer<Event> eventConsumer) {
        return new Connection<Effect>() {
            @Override
            public void accept(@NonNull Effect effect) {
                effect.match(
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
