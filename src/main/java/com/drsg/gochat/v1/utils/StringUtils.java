package com.drsg.gochat.v1.utils;

/**
 * @author YXs
 */
public class StringUtils {
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
