package me.wuwenbin.modules.repodata.annotation.type;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/30 at 11:59
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataRepo {
}
