package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Attribute;

/**
 * XML node, zero or one per Location
 */
public class Temperature {
    @Attribute(name = "unit", required = false)
    private String unit;
    @Attribute(name = "value", required = false)
    private String value;

    public String getUnit() {
        return unit;
    }

    public String getValue() {
        return value;
    }

    public Temperature() {
    }
}
