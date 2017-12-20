package me.wuwenbin.modules.utils.support;

/**
 * created by Wuwenbin on 2017/12/20 at 下午4:28
 *
 * @author wuwenbin
 */
@FunctionalInterface
public interface MySupplier<T> {

    /**
     * 执行对应的方法获取结果
     *
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
