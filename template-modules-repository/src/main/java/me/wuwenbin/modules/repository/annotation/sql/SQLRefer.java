package me.wuwenbin.modules.repository.annotation.sql;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/27 at 12:01
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SQLRefer {

    /**
     * 关联的目标对象（为了获取关联的对象的表名）
     *
     * @return
     */
    Class<?> targetClass();

    /**
     * 目标表，即targetCLass对应表的别名
     *
     * @return
     */
    String targetTableAlias() default "";

    /**
     * 目标实体的哪个属性字段对应的此属性
     *
     * @return
     */
    String targetColumn();


    /**
     * 本身被关联的列（数据库中的列名）
     *
     * @return
     */
    String joinColumn();

    /**
     * 关联的列名（是指数据库中的列名）
     *
     * @return
     */
    String referColumn();

    boolean optional() default true;
}
