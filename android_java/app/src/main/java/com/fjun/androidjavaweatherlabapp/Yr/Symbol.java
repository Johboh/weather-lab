package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Attribute;

/**
 * XML node, zero or one per Location
 */
public class Symbol {
    @Attribute(name = "number", required = false)
    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public Symbol() {
    }
}
