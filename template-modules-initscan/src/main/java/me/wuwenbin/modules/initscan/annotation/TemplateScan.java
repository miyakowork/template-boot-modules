package me.wuwenbin.modules.initscan.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/9/19 at 9:46
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TemplateScan {

    @AliasFor("name")
    String value();

    /**
     * 资源名称
     *
     * @return
     */
    @AliasFor("value")
    String name();

    /**
     * 是否可用，默认可用
     *
     * @return
     */
    boolean enabled() default true;

    /**
     * 排序字段
     *
     * @return
     */
    int orderIndex() default 0;

    /**
     * 资源备注说明
     *
     * @return
     */
    String remark() default "";

}
