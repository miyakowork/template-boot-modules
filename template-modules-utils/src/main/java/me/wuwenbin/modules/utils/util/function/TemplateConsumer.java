package me.wuwenbin.modules.utils.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * created by Wuwenbin on 2017/12/26 at 14:23
 *
 * @author wuwenbin
 */
@FunctionalInterface
public interface TemplateConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws Exception
     */
    void accept(T t) throws Exception;

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TemplateConsumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
