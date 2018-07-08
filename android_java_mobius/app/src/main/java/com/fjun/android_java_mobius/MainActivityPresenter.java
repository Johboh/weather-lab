package com.fjun.android_java_mobius;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fjun.android_java_mobius.Yr.Symbol;
import com.fjun.android_java_mobius.Yr.Temperature;
import com.fjun.android_java_mobius.mobius.Effect;
import com.fjun.android_java_mobius.mobius.EffectHandler;
import com.fjun.android_java_mobius.mobius.Event;
import com.fjun.android_java_mobius.mobius.Model;
import com.fjun.android_java_mobius.mobius.UpdateFunction;
import com.spotify.mobius.Connectable;
import com.spotify.mobius.Connection;
import com.spotify.mobius.ConnectionLimitExceededException;
import com.spotify.mobius.Mobius;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.functions.Consumer;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.fjun.android_java_mobius.Yr.YrWeatherService.IMAGE_URL;

/**
 * Presenter for main activity. Presents temperature and icon.
 * Listens for location changes and update weather when location has changed.
 */
public class MainActivityPresenter implements LifecycleObserver {

    private final MainActivityViewBinder mViewBinder;
    private final EffectHandler mEffectHandler;
    private final GpsLocationListener mGpsLocationListener;
    private MobiusLoop<Model, Event, Effect> mMobiusLoop;

    @Inject
    MainActivityPresenter(
            MainActivityViewBinder viewBinder,
            EffectHandler effectHandler,
            GpsLocationListener gpsLocationListener) {
        mViewBinder = viewBinder;
        mEffectHandler = effectHandler;
        mGpsLocationListener = gpsLocationListener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        mMobiusLoop = Mobius.loop(UpdateFunction::update, new Connectable<Effect, Event>() {
            @Nonnull
            @Override
            public Connection<Effect> connect(@NonNull Consumer<Event> eventConsumer) throws ConnectionLimitExceededException {
                return mEffectHandler.effectHandler(eventConsumer);
            }
        }).eventSource(mGpsLocationListener)
                .startFrom(Model.builder().isFetchingLocation(true).build());

        mMobiusLoop.observe(model -> {
            // TODO (johboh): Threading?
            if (model.isFetchingLocation()) {
                mViewBinder.showWaitingForPosition();
            } else if (model.isFetchingWeather()) {
                mViewBinder.showWaitingForWeather();
            } else if (!TextUtils.isEmpty(model.errorString())) {
                mViewBinder.showSomethingWentWrong(model.errorString());
            } else {
                final Temperature temperature = model.temperature();
                final Symbol symbol = model.symbol();
                if (temperature == null) {
                    mViewBinder.showSomethingWentWrong("Null temperature");
                } else {
                    mViewBinder.setTemperature(String.format(Locale.US, "%s %s", temperature.value, temperature.unit));
                    if (symbol != null) {
                        mViewBinder.setImageUrl(String.format(Locale.US, IMAGE_URL, symbol.number));
                    }
                }
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mMobiusLoop.dispose();
    }
}
