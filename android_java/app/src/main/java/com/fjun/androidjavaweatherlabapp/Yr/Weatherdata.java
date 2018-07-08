package com.fjun.androidjavaweatherlabapp.Yr;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Root XML for locationforecast
 */
@Root(strict = false)
public class Weatherdata {
    @Element(name = "product", type = Product.class)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public Weatherdata() { // required
    }
}
