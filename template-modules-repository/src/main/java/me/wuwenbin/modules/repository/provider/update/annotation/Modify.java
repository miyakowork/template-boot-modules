package me.wuwenbin.modules.repository.provider.update.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Modify {

    /**
     * 更新的字段router
     *
     * @return
     */
    int[] value();
}
