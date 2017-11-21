package me.wuwenbin.modules.repository.util;

import java.util.Date;

public class BeanUtils {

    public static boolean isPrimitive(Object param) {
        return param instanceof Integer || param instanceof String || param instanceof Double || param instanceof Float || param instanceof Long || param instanceof Boolean || param instanceof Date;
    }


}
