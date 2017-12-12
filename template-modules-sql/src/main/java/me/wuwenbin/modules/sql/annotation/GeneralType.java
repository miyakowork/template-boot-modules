package me.wuwenbin.modules.sql.annotation;

import me.wuwenbin.modules.sql.annotation.support.PkGenType;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/30 at 15:00
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GeneralType {

    /**
     * 默认主键为数据库的自增长
     *
     * @return
     */
    PkGenType value() default PkGenType.AUTO;
}
