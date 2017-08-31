package me.wuwenbin.modules.pagination.query.support.annotation;

import java.lang.annotation.*;

/**
 * 用于查询BO对象类上的注解
 * created by Wuwenbin on 2017/8/30 at 10:10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryTable {

    /**
     * 如果表格为多个表联合查询的sql语句查出来的，那么查询的时候需要指定是哪个表，
     * 要不然sql语句会的列字段会存在歧义，因为有可能这其中几张表都含有这个字段。
     * 如果不指定，那么则认为不是多个表联合查询，或者多个表没有重复的字段，不会存在歧义，则不需要注明查询的表
     *
     * @return 表名
     */
    String name() default "";

    /**
     * 这个字段表名砸了sql语句中表名已被指定为另外的别名，所以需要另外注明，不使用原来的表名。
     * 使用原来的表名会报找不到的错。
     *
     * @return 表的别名
     */
    String aliasName() default "";
}