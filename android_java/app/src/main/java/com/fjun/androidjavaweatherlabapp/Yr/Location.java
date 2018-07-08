package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;

/**
 * XML node, one per Time
 */
public class Location {
    @Element(name = "temperature", required = false)
    private Temperature temperature;
    @Element(name = "symbol", required = false)
    private Symbol symbol;

    public Temperature getTemperature() {
        return temperature;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Location() {
    }
}
