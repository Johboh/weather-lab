package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Weatherdata {
    @Element(name = "product", type = Product.class)
    public Product product;

    public Weatherdata() { // required
    }
}
