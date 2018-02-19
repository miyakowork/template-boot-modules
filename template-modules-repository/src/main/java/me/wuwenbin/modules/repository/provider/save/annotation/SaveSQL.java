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
     *此属性和下面的routers将会叠加在一起作为insert语句的插入字段
     * @return
     */
    String[] columns() default {};

    /**
     * 插入的列名所在的router
     * 此属性和上面的columns将会叠加在一起作为insert语句的插入字段
     *
     * @return
     */
    int[] routers() default {};
    /**
     * @return
     */
    Parametric type() default Parametric.Colon;
}
