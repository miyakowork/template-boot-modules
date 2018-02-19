package me.wuwenbin.modules.sql.annotation;

import me.wuwenbin.modules.sql.annotation.support.Condition;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SQLColumn(pk = true)
public @interface SQLPk {

    /**
     * the name of column
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * router条件，默认为int最小值
     *
     * @return {@link Integer[]}
     */
    int[] routers() default Integer.MIN_VALUE;

    /**
     * 默认的条件为 =
     *
     * @return
     */
    Condition condition() default Condition.EQ;

    /**
     * 此列是否需要被select至结果，默认是肯定的
     *
     * @return
     */
    boolean select() default true;

    /**
     * 允许插入这一列
     *
     * @return
     */
    boolean insert() default false;

    /**
     * 允许更新这一列
     *
     * @return
     */
    boolean update() default false;

}
