package com.fjun.androidjavaweatherlabapp;

import android.location.Location;

import com.fjun.androidjavaweatherlabapp.Yr.Symbol;
import com.fjun.androidjavaweatherlabapp.Yr.Temperature;
import com.fjun.androidjavaweatherlabapp.Yr.Yr;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MainActivityPresenterTest {

    @Mock
    private MainActivityViewBinder mViewBinder;
    @Mock
    private GpsLocationListener mGpsLocationListener;
    @Mock
    private Yr mYr;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void onStart() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStart();
        verify(mViewBinder).showWaitingForPosition();
        verify(mGpsLocationListener).registerListener(any());
    }

    @Test
    public void requestWeatherOnLocation() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStart();

        final ArgumentCaptor<GpsLocationListener.Callback> gpsCallbackCaptor = ArgumentCaptor.forClass(GpsLocationListener.Callback.class);
        verify(mGpsLocationListener).registerListener(gpsCallbackCaptor.capture());
        final Location location = mock(Location.class);
        gpsCallbackCaptor.getValue().onLocation(location);

        verify(mViewBinder).showWaitingForWeather();
        verify(mYr).requestWeather(eq(location), any());
    }

    @Test
    public void requestWeatherOnError() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStart();

        final ArgumentCaptor<GpsLocationListener.Callback> gpsCallbackCaptor = ArgumentCaptor.forClass(GpsLocationListener.Callback.class);
        verify(mGpsLocationListener).registerListener(gpsCallbackCaptor.capture());
        gpsCallbackCaptor.getValue().onLocation(mock(Location.class));

        final ArgumentCaptor<Yr.Callback> weatherCallbackCaptor = ArgumentCaptor.forClass(Yr.Callback.class);
        verify(mYr).requestWeather(any(), weatherCallbackCaptor.capture());
        weatherCallbackCaptor.getValue().onError(new Throwable(":("));

        verify(mViewBinder).showSomethingWentWrong(":(");
    }

    @Test
    public void requestWeatherTemperature() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStart();

        final ArgumentCaptor<GpsLocationListener.Callback> gpsCallbackCaptor = ArgumentCaptor.forClass(GpsLocationListener.Callback.class);
        verify(mGpsLocationListener).registerListener(gpsCallbackCaptor.capture());
        gpsCallbackCaptor.getValue().onLocation(mock(Location.class));

        final ArgumentCaptor<Yr.Callback> weatherCallbackCaptor = ArgumentCaptor.forClass(Yr.Callback.class);
        verify(mYr).requestWeather(any(), weatherCallbackCaptor.capture());
        final Temperature temperature = new Temperature();
        temperature.unit = "C";
        temperature.value = "10";
        weatherCallbackCaptor.getValue().onTemperature(temperature, null);

        verify(mViewBinder).setTemperature("10 C");
        verify(mViewBinder, never()).setImageUrl(nullable(String.class));
    }

    @Test
    public void requestWeatherImage() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStart();

        final ArgumentCaptor<GpsLocationListener.Callback> gpsCallbackCaptor = ArgumentCaptor.forClass(GpsLocationListener.Callback.class);
        verify(mGpsLocationListener).registerListener(gpsCallbackCaptor.capture());
        gpsCallbackCaptor.getValue().onLocation(mock(Location.class));

        final ArgumentCaptor<Yr.Callback> weatherCallbackCaptor = ArgumentCaptor.forClass(Yr.Callback.class);
        verify(mYr).requestWeather(any(), weatherCallbackCaptor.capture());
        final Symbol symbol = new Symbol();
        symbol.number = 123456789;
        weatherCallbackCaptor.getValue().onTemperature(mock(Temperature.class), symbol);

        ArgumentCaptor<String> imageUri = ArgumentCaptor.forClass(String.class);
        verify(mViewBinder).setImageUrl(imageUri.capture());
        assertTrue(imageUri.getValue().contains(String.valueOf(symbol.number)));
    }

    @Test
    public void onStop() {
        final MainActivityPresenter presenter = createPresenter();
        presenter.onStop();
        verify(mGpsLocationListener).unregisterListener();
    }


    private MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(mViewBinder, mGpsLocationListener, mYr);
    }
}
