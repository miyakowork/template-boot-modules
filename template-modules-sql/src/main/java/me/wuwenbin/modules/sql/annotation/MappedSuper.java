package me.wuwenbin.modules.sql.annotation;

import java.lang.annotation.*;

/**
 * @see SQLMappedSuper
 * created by Wuwenbin on 2017/10/27 at 10:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Deprecated
public @interface MappedSuper {

    /**
     * 默认不反射父类的字段，仅映射本身类的字段
     *
     * @return
     */
    boolean value() default true;
}
