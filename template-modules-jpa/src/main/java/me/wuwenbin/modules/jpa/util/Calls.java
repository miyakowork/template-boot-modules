package me.wuwenbin.modules.jpa.util;

import org.springframework.aop.framework.AopContext;

/**
 * created by Wuwenbin on 2017/11/29 at 19:05
 *
 * @author wuwen
 */
public class Calls {

    /**
     * 无返回值
     *
     * @param reVoid
     * @param <T>
     */
    public static <T> void get(ReVoid<T> reVoid) {
        //noinspection unchecked
        T bean = (T) AopContext.currentProxy();
        reVoid.execute(bean);
    }

    /**
     * 有返回值
     *
     * @param reObject
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R get(ReObject<T, R> reObject) {
        //noinspection unchecked
        T bean = (T) AopContext.currentProxy();
        return reObject.execute(bean);
    }
}
