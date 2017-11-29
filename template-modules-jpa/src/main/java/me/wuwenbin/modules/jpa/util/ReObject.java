package me.wuwenbin.modules.jpa.util;

/**
 * created by Wuwenbin on 2017/11/29 at 19:58
 * 执行的内部方法有返回值的
 *
 * @param <T> 类对象
 * @param <R> 返回对象
 * @author wuwen
 */
public interface ReObject<T, R> {
    /**
     * 执行调用方法
     *
     * @param bean
     * @return
     */
    R execute(T bean);
}
