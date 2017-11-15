package me.wuwenbin.modules.repodata.annotation.sql;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/27 at 16:48
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SQLPkRefer {

    /**
     * 关联的目标对象（为了获取关联的对象的表名），同时根据主键的注解获取主键的名称
     *
     * @return
     */
    Class<?> targetClass();

    /**
     * 目标实体的哪个属性字段对应的此属性
     *
     * @return
     */
    String targetColumn();

    /**
     * 与target()作用相同，此处则直接指明tableName
     *
     * @return
     */
    String table() default "";

    /**
     * 本身被关联的列（数据库中的列名）
     *
     * @return
     */
    String column();
}
