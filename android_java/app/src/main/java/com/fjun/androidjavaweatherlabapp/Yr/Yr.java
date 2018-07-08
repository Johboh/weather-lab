package com.fjun.androidjavaweatherlabapp.Yr;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Helper for requesting data from YR weather service, given a location.
 */
public class Yr {

    private final YrWeatherService mYrWeatherService;

    @Inject
    public Yr(YrRetrofit yrRetrofit) {
        mYrWeatherService = yrRetrofit.createYrWeatherService();
    }

    /**
     * Request weather given a location.
     */
    public void requestWeather(Location location, Callback callback) {
        // Convert latitude and longitude to two decimal numbers with . as separator
        final DecimalFormat formatter = new DecimalFormat("#0.00");
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(custom);

        mYrWeatherService.forecast(formatter.format(location.getLatitude()), formatter.format(location.getLongitude())).enqueue(new retrofit2.Callback<Weatherdata>() {
            @Override
            public void onResponse(@NonNull Call<Weatherdata> call, @NonNull Response<Weatherdata> response) {
                final Weatherdata weatherdata = response.body();
                if (weatherdata == null || weatherdata.getProduct() == null || weatherdata.getProduct().getTimes() == null) {
                    callback.onError(new Throwable("Weatherdata is null or missing children"));
                    return;
                }

                // Find first temperature and symbol.
                // Symbol and temperature are not located within the same <time>
                Temperature temperature = null;
                Symbol symbol = null;
                final List<Time> times = weatherdata.getProduct().getTimes();
                for (Time time : times) {
                    final com.fjun.androidjavaweatherlabapp.Yr.Location timeLocation = time.getLocation();
                    if (timeLocation != null) {
                        if (symbol == null) {
                            symbol = timeLocation.getSymbol();
                        }
                        if (temperature == null) {
                            temperature = timeLocation.getTemperature();
                        }
                    }
                    // Found both of them?
                    if (symbol != null && temperature != null) {
                        break;
                    }
                }

                if (temperature == null) {
                    callback.onError(new Throwable("Unable to find a temperature."));
                    return;
                }

                callback.onTemperature(temperature, symbol);
            }

            @Override
            public void onFailure(@NonNull Call<Weatherdata> call, @NonNull Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }

    public interface Callback {
        void onTemperature(@NonNull Temperature temperature, @Nullable Symbol symbol);

        void onError(@NonNull Throwable throwable);
    }
}
