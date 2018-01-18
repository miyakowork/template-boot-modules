package me.wuwenbin.modules.repository.function;

/**
 * created by Wuwenbin on 2017/12/17 at 17:47
 * @author wuwenbin
 */
@FunctionalInterface
public interface BiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws Exception
     */
    R apply(T t, U u) throws Exception;
}
