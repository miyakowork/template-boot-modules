package me.wuwenbin.modules.repodata.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * created by Wuwenbin on 2017/10/30 at 12:15
 */
public class BeanUtils {

    /**
     * 强行设置Field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

}
