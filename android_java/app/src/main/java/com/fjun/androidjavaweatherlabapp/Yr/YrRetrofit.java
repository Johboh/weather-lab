package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.NodeMap;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Create retrofit service for YR.
 */
public class YrRetrofit {

    @Inject
    YrRetrofit() {
    }

    public YrWeatherService createYrWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YrWeatherService.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister(new TreeStrategyWithoutClass())))
                .build();
        return retrofit.create(YrWeatherService.class);
    }


    // Ignore class attributes in xml, and use own named classes instead.
    private class TreeStrategyWithoutClass extends TreeStrategy {
        @Override
        public Value read(Type type, NodeMap node, Map map) {
            return null;
        }
    }
}
