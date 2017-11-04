package me.wuwenbin.modules.repodata.annotation.field;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/27 at 16:48
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SQLPkRefer {

    Class<?> target();
}
