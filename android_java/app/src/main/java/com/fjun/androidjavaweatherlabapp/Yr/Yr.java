package com.fjun.androidjavaweatherlabapp.Yr;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.NodeMap;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Yr {

    @Inject
    public Yr() {
    }

    /**
     * Request weather given a location.
     */
    public void requestWeather(Location location, Callback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YrWeatherService.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister(new TreeStrategyWithoutClass())))
                .build();

        // Convert latitude and longitude to two decimal numbers with . as separator
        YrWeatherService yrWeatherService = retrofit.create(YrWeatherService.class);
        final DecimalFormat formatter = new DecimalFormat("#0.00");
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(custom);
        yrWeatherService.forecast(formatter.format(location.getLatitude()), formatter.format(location.getLongitude())).enqueue(new retrofit2.Callback<Weatherdata>() {
            @Override
            public void onResponse(Call<Weatherdata> call, Response<Weatherdata> response) {
                final Weatherdata weatherdata = response.body();
                if (weatherdata == null || weatherdata.product == null || weatherdata.product.time == null) {
                    callback.onError(new Throwable("Weatherdata is null or missing children"));
                    return;
                }

                // Find first temperature and symbol.
                // Symbol and temperature are not located within the same <time>
                Temperature temperature = null;
                Symbol symbol = null;
                final List<Time> times = weatherdata.product.time;
                for (Time time : times) {
                    final com.fjun.androidjavaweatherlabapp.Yr.Location timeLocation = time.location;
                    if (timeLocation != null) {
                        if (symbol == null) {
                            symbol = timeLocation.symbol;
                        }
                        if (temperature == null) {
                            temperature = timeLocation.temperature;
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
            public void onFailure(Call<Weatherdata> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface Callback {
        void onTemperature(@NonNull Temperature temperature, @Nullable Symbol symbol);

        void onError(Throwable throwable);
    }

    // Ignore class attributes in xml, and use own named classes instead.
    private class TreeStrategyWithoutClass extends TreeStrategy {
        @Override
        public Value read(Type type, NodeMap node, Map map) {
            return null;
        }
    }
}
