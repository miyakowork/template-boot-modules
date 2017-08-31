package me.wuwenbin.modules.pagination.query.support.annotation;

import me.wuwenbin.modules.pagination.query.support.operator.Operator;

import java.lang.annotation.*;

/**
 * 用于查询BO对象上的属性上面
 * created by Wuwenbin on 2017/8/30 at 10:16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryColumn {

    /**
     * 如果不注明此字段，则说明类中此属性和数据库中列名是一致的(或者为驼峰和下划线的对应关系)。
     * 如果注明则使用此值来查询
     *
     * @return 列的字段名
     */
    String column() default "";

    /**
     * 因为查询的集合不一定只是一张表，所以此处也可以指定是查询哪张表。
     * 如果不指定，则使用sql语境默认的表，或使用类名上的QueryTable注解的表名/别名-
     *
     * @return 此搜索列所在的表名
     */
    String tableName() default "";

    /**
     * 同理，也可能表也被另外的名字替代，所以需要注明别名。
     * 默认不写，自动为sql语境中的表
     *
     * @return 此搜索列所在的表的别名
     */
    String tableAlias() default "";

    /**
     * 这个字段使用什么条件来查询，默认为like查询
     *
     * @return 字段查询的逻辑条件
     */
    Operator operator() default Operator.LIKE;

    /**
     * 如果当operator为between and的时候，需要指明2个搜索之间的分隔符是什么，默认~
     *
     * @return
     */
    String split() default "~";
}
