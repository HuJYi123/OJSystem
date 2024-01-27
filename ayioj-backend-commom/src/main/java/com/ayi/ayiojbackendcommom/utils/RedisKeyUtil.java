package com.ayi.ayiojbackendcommom.utils;

public class RedisKeyUtil {

    public static final String METADATA_SERVICE = "invest";

    public static final String PREFIX_QUESTION = "AYIOJ";
    public static final String KEY_FIELD_COLON  = ":";

    public static String assemblyServiceKey(String type, String extra) {
        return METADATA_SERVICE + KEY_FIELD_COLON + type + KEY_FIELD_COLON + extra;
    }

    public static String getKey(String type, Long id) {
        return PREFIX_QUESTION + KEY_FIELD_COLON + type + KEY_FIELD_COLON + id;
    }
    public static String assemblyGenericIdKey(String type) {
        return METADATA_SERVICE + KEY_FIELD_COLON + type;
    }
}
