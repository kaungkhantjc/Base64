package com.jcoder.base64.utils;

import android.util.Base64;

public class Base64Utils {

    public static String encode(String str) throws IllegalArgumentException {
        if (str == null) throw new IllegalArgumentException("Input string is null.");
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT).trim();
    }

    public static String decode(String str) throws IllegalArgumentException {
        if (str == null) throw new IllegalArgumentException("Input string is null.");
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }

}
