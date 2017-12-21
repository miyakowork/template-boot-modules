package me.wuwenbin.modules.utils.util.function;


/**
 * 表示接受一个参数并产生结果的函数。在JDK8的基础上增加了异常抛出
 * <p>
 * <p>这是一个包含一个方法为 {@link #apply(Object)}的
 * <a href="package-summary.html">函数式接口</a>
 *
 * @param <T> 输入的方法类型
 * @param <R> 返回结果的方法类型
 * @since 1.8
 * <p>
 * created by Wuwenbin on 2017/12/20at 20:22
 */

@FunctionalInterface
public interface TemplateFunction<T, R> {
    /**
     * 将此函数应用于给定的参数。
     *
     * @param t 函数参数
     * @return 函数结果
     * @throws Exception
     */
    R apply(T t) throws Exception;


    /**
     * 返回一个总是返回其输入参数的函数。
     *
     * @param <T> 该函数的输入和输出对象的类型
     * @return 总是返回其输入参数的函数
     */
    static <T> TemplateFunction<T, T> identity() {
        return t -> t;
    }
}
