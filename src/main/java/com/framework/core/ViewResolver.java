package com.framework.core;

public class ViewResolver {

    public static String resoudreChemin(String prefixe, String vue, String suffixe) {
        if (vue == null) {
            return null;
        }
        String p = (prefixe == null) ? "" : prefixe;
        String s = (suffixe == null) ? "" : suffixe;
        return p + vue + s;
    }
}