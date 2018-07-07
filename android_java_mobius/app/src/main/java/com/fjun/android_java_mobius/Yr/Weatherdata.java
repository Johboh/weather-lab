package com.fjun.android_java_mobius.Yr;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Root XML for locationforecast
 */
@Root(strict = false)
public class Weatherdata {
    @Element(name = "product", type = Product.class)
    public Product product;

    public Weatherdata() { // required
    }
}
