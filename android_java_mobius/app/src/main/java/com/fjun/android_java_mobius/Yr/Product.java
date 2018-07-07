package com.fjun.android_java_mobius.Yr;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * XML node, one per Weatherdata
 */
public class Product {
    @ElementList(name = "time", required = false, inline = true)
    public List<Time> time;

    public Product() {
    }
}
