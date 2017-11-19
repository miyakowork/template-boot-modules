package me.wuwenbin.modules.repository.provider.delete.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DeleteSQL {

    /**
     * 根据此处指定的SQL语句来操作
     *
     * @return
     */
    String value();
}
