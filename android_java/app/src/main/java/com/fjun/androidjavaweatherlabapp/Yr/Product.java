package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class Product {
    @ElementList(name = "time", required = false, inline = true)
    public List<Time> time;

    public Product() {
    }
}
