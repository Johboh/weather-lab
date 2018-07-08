package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * XML node, one per Weatherdata
 */
public class Product {
    @ElementList(name = "time", required = false, inline = true)
    private List<Time> time;

    public List<Time> getTimes() {
        return time;
    }

    public Product() {
    }
}
