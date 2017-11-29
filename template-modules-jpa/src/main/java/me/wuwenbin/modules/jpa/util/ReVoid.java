package me.wuwenbin.modules.jpa.util;

/**
 * created by Wuwenbin on 2017/11/29 at 19:44
 *
 * @param <T> 类对象
 * @author wuwen
 */
public interface ReVoid<T> {

    /**
     * 执行调用方法
     *
     * @param bean
     */
    void execute(T bean);
}
