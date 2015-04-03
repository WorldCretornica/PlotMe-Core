package com.worldcretornica.plotme_core.utils;

import static java.lang.Double.doubleToLongBits;

/**
 * Contains code that is in Java 8 and is useful to us but we can't use since we can't use Java 8 just yet...
 */
public class DoubleHelper {

    public static int hashCode(double value) {
        long bits = doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }
}
