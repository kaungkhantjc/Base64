package com.jcoder.base64.utils;

import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Str {

    public static byte[] toBytes(String str) throws UnsupportedEncodingException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return str.getBytes(StandardCharsets.UTF_8);
        } else {
            //noinspection CharsetObjectCanBeUsed
            return str.getBytes("UTF-8");
        }
    }

    public static String bytesToStr(byte[] bytes) throws UnsupportedEncodingException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new String(bytes, StandardCharsets.UTF_8);
        } else {
            //noinspection CharsetObjectCanBeUsed
            return new String(bytes, "UTF-8");
        }
    }

}
