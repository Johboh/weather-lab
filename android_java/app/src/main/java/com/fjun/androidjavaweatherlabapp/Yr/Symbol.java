package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Attribute;

public class Symbol {
    @Attribute(name = "number", required = false)
    public Integer number;

    public Symbol() {
    }
}
