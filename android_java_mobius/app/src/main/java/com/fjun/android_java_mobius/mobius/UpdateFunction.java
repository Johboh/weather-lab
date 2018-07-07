package com.fjun.android_java_mobius.mobius;

import android.support.annotation.NonNull;

import com.spotify.dataenum.function.Function;
import com.spotify.mobius.Next;

import java.util.Collections;

import javax.annotation.Nonnull;

public class UpdateFunction {
    @NonNull
    public static Next<Model, Effect> update(Model model, Event event) {
        return event.map(new Function<Event.init, Next<Model, Effect>>() {
            @Nonnull
            @Override
            public Next<Model, Effect> apply(@Nonnull Event.init value) {
                return Next.next(model.toBuilder().isFetchingLocation(true).build(), Collections.singleton(Effect.waitForLocation()));
            }
        }, new Function<Event.gotLocation, Next<Model, Effect>>() {
            @Nonnull
            @Override
            public Next<Model, Effect> apply(@Nonnull Event.gotLocation value) {
                return Next.next(model.toBuilder().isFetchingLocation(false).isFetchingWeather(true).build(), Collections.singleton(Effect.requestWeather(value.location())));
            }
        }, new Function<Event.gotWeather, Next<Model, Effect>>() {
            @Nonnull
            @Override
            public Next<Model, Effect> apply(@Nonnull Event.gotWeather value) {
                return Next.next(Model.builder().temperature(value.yrResult().temperature).symbol(value.yrResult().symbol).build());
            }
        }, new Function<Event.gotError, Next<Model, Effect>>() {
            @Nonnull
            @Override
            public Next<Model, Effect> apply(@Nonnull Event.gotError value) {
                return Next.next(Model.builder().errorString(value.error()).build());
            }
        });
    }
}
