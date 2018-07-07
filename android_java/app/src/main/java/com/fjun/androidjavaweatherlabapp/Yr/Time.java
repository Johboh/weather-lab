package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;

/**
 * XML node, many per Product
 */
public class Time {
    @Element(name = "location", required = false)
    public Location location;

    public Time() {
    }
}
