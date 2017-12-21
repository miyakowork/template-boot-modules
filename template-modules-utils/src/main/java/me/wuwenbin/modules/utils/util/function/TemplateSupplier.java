package me.wuwenbin.modules.utils.util.function;

/**
 * 增加了抛出异常处理
 *
 * @param <T> the type of results supplied by this supplier
 * @author wuwenbin
 * @since 1.8
 * created by Wuwenbin on 2017/12/20 at 下午4:28
 */
@FunctionalInterface
public interface TemplateSupplier<T> {

    /**
     * 执行对应的方法获取结果
     *
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
