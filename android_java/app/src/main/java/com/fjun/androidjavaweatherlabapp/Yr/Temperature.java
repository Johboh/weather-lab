package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Attribute;

public class Temperature {
    @Attribute(name = "unit", required = false)
    public String unit;
    @Attribute(name = "value", required = false)
    public String value;

    public Temperature() {
    }
}
