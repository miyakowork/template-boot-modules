package me.wuwenbin.modules.repository.provider.find.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PrimitiveCollection {

    /**
     * 指定返回结果集合类型中的泛型类型
     * 仅适用于返回类型为泛型集合
     *
     * @return
     */
    Class<?> value() default String.class;
}
