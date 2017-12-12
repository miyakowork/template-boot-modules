package me.wuwenbin.modules.sql.annotation;

import java.lang.annotation.*;

/**
 * @see SQLTable
 * Created by wuwenbin on 2016/10/11.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SQLTable {

    String value() default "";

}
