package com.fjun.android_java_mobius.Yr;

import org.simpleframework.xml.Attribute;

/**
 * XML node, zero or one per Location
 */
public class Symbol {
    @Attribute(name = "number", required = false)
    public Integer number;

    public Symbol() {
    }
}
