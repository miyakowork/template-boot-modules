package me.wuwenbin.modules.repository.provider.save.annotation;

import me.wuwenbin.modules.repository.constant.Parametric;

import java.lang.annotation.*;

/**
 * columns与type一起配合使用
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaveSQL {

    /**
     * 插入的列名
     *
     * @return
     */
    String[] columns();

    /**
     * @return
     */
    Parametric type() default Parametric.Colon;
}
