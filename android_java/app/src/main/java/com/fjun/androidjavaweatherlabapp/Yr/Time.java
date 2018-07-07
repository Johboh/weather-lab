package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;

public class Time {
    @Element(name = "location", required = false)
    public Location location;

    public Time() {
    }
}
