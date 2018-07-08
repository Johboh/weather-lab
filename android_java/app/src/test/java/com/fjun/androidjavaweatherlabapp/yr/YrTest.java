package com.fjun.androidjavaweatherlabapp.yr;

import android.location.Location;

import com.fjun.androidjavaweatherlabapp.Yr.Product;
import com.fjun.androidjavaweatherlabapp.Yr.Symbol;
import com.fjun.androidjavaweatherlabapp.Yr.Temperature;
import com.fjun.androidjavaweatherlabapp.Yr.Time;
import com.fjun.androidjavaweatherlabapp.Yr.Weatherdata;
import com.fjun.androidjavaweatherlabapp.Yr.Yr;
import com.fjun.androidjavaweatherlabapp.Yr.YrRetrofit;
import com.fjun.androidjavaweatherlabapp.Yr.YrWeatherService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class YrTest {

    @Mock
    private YrWeatherService mYrWeatherService;
    @Mock
    private Location mLocation;
    @Mock
    private Yr.Callback mCallback;
    @Mock
    private Call<Weatherdata> mRetrofitCall;
    @Mock
    private Response<Weatherdata> mResponse;
    @Captor
    private ArgumentCaptor<retrofit2.Callback<Weatherdata>> mEnqueueCaptor;

    @Before
    public void setUp() {
        initMocks(this);
        when(mLocation.getLatitude()).thenReturn(12.123456);
        when(mLocation.getLongitude()).thenReturn(30.789010);
        when(mYrWeatherService.forecast(any(), any())).thenReturn(mRetrofitCall);
    }

    @Test
    public void longitudeLatitude() {
        final Yr yr = createYr();
        yr.requestWeather(mLocation, mCallback);
        verify(mYrWeatherService).forecast("12.12", "30.79");
        verify(mRetrofitCall).enqueue(any());
    }

    @Test
    public void onEnqueueFailure() {
        final Yr yr = createYr();
        yr.requestWeather(mLocation, mCallback);
        verify(mRetrofitCall).enqueue(mEnqueueCaptor.capture());
        final Throwable throwable = new Throwable(":(");
        mEnqueueCaptor.getValue().onFailure(mRetrofitCall, throwable);
        verify(mCallback).onError(throwable);
    }

    @Test
    public void onNullWeatherdata() {
        setupRetrofitReply(null);
        verify(mCallback).onError(any());
    }

    @Test
    public void onNullProduct() {
        final Weatherdata weatherdata = mock(Weatherdata.class);
        when(weatherdata.getProduct()).thenReturn(null);
        setupRetrofitReply(weatherdata);
        verify(mCallback).onError(any());
    }

    @Test
    public void onNullTimes() {
        final Weatherdata weatherdata = mock(Weatherdata.class);
        final Product product = mock(Product.class);
        when(weatherdata.getProduct()).thenReturn(product);
        when(product.getTimes()).thenReturn(null);
        setupRetrofitReply(weatherdata);
        verify(mCallback).onError(any());
    }

    @Test
    public void onEmptyList() {
        setupRetrofitReply(createWeatherdata(Collections.emptyList()));
        verify(mCallback).onError(any());
    }

    @Test
    public void onNoTemperature() {
        setupRetrofitReply(createWeatherdata(Collections.singletonList(createTime(null, mock(Symbol.class)))));
        verify(mCallback).onError(any());
    }

    @Test
    public void onTemperature() {
        setupRetrofitReply(createWeatherdata(Collections.singletonList(createTime(mock(Temperature.class), null))));
        verify(mCallback).onTemperature(any(), isNull());
    }

    @Test
    public void onTemperatureAndSymbol() {
        setupRetrofitReply(createWeatherdata(Collections.singletonList(createTime(mock(Temperature.class), mock(Symbol.class)))));
        verify(mCallback).onTemperature(any(), any());
    }

    @Test
    public void takeFirstTemperature() {
        final List<Time> times = new ArrayList<>();
        final Temperature temperature1 = mock(Temperature.class);
        final Temperature temperature2 = mock(Temperature.class);
        times.add(createTime(temperature1, null));
        times.add(createTime(temperature2, null));
        setupRetrofitReply(createWeatherdata(times));
        verify(mCallback).onTemperature(temperature1, null);
    }

    @Test
    public void takeFirstSymbol() {
        final List<Time> times = new ArrayList<>();
        final Symbol symbol1 = mock(Symbol.class);
        final Symbol symbol2 = mock(Symbol.class);
        times.add(createTime(mock(Temperature.class), symbol1));
        times.add(createTime(mock(Temperature.class), symbol2));
        setupRetrofitReply(createWeatherdata(times));
        verify(mCallback).onTemperature(any(), eq(symbol1));
    }

    private void setupRetrofitReply(Weatherdata weatherdata) {
        final Yr yr = createYr();
        yr.requestWeather(mLocation, mCallback);
        verify(mRetrofitCall).enqueue(mEnqueueCaptor.capture());
        when(mResponse.body()).thenReturn(weatherdata);
        mEnqueueCaptor.getValue().onResponse(mRetrofitCall, mResponse);
    }

    private Weatherdata createWeatherdata(List<Time> times) {
        final Weatherdata weatherdata = mock(Weatherdata.class);
        final Product product = mock(Product.class);
        when(weatherdata.getProduct()).thenReturn(product);
        when(product.getTimes()).thenReturn(times);
        return weatherdata;
    }

    private Time createTime(Temperature temperature, Symbol symbol) {
        final Time time = mock(Time.class);
        final com.fjun.androidjavaweatherlabapp.Yr.Location location = mock(com.fjun.androidjavaweatherlabapp.Yr.Location.class);
        when(location.getTemperature()).thenReturn(temperature);
        when(location.getSymbol()).thenReturn(symbol);
        when(time.getLocation()).thenReturn(location);
        return time;
    }

    private Yr createYr() {
        final YrRetrofit yrRetrofit = mock(YrRetrofit.class);
        when(yrRetrofit.createYrWeatherService()).thenReturn(mYrWeatherService);
        return new Yr(yrRetrofit);
    }
}
