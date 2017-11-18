package me.wuwenbin.modules.repository.annotation.field;

import me.wuwenbin.tools.sqlgen.constant.Router;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Routers {

    /**
     * 根据此处指定的SQL语句来操作
     *
     * @return
     */
    int[] value() default Router.DEFAULT;
}
