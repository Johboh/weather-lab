package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Attribute;

/**
 * XML node, zero or one per Location
 */
public class Temperature {
    @Attribute(name = "unit", required = false)
    public String unit;
    @Attribute(name = "value", required = false)
    public String value;

    public Temperature() {
    }
}
