package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;

/**
 * XML node, one per Time
 */
public class Location {
    @Element(name = "temperature", required = false)
    public Temperature temperature;
    @Element(name = "symbol", required = false)
    public Symbol symbol;

    public Location() {
    }
}
