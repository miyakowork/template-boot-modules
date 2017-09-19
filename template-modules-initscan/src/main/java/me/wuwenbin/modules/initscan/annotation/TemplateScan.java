package me.wuwenbin.modules.initscan.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/9/19 at 9:46
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TemplateScan {
}
