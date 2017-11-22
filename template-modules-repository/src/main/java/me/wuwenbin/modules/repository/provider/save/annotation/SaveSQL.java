package me.wuwenbin.modules.repository.provider.save.annotation;

import me.wuwenbin.modules.repository.constant.Parametric;

import java.lang.annotation.*;

/**
 * sql语句与下面的columns二选一，value此处定义了则下面columns的忽略
 * columns与paramType仪器配合使用
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaveSQL {

    /**
     * 自定义的sql语句
     *
     * @return
     */
    String value() default "";

//===============另一种使用方法============

    /**
     * 插入的列名
     *
     * @return
     */
    String[] columns() default {};

    /**
     * @return
     */
    Parametric paramType() default Parametric.Doubt;
}
