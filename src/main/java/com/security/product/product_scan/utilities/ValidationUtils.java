/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.utilities;


public class ValidationUtils {
    private ValidationUtils(){}
    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static boolean validatePrice(Double price) {
        if (price == null)
            return false;
        return price > 0;
    }

    public static boolean validateQuantity(Integer price) {
        if (price == null)
            return false;
        return (double) price > 0;
    }
}
