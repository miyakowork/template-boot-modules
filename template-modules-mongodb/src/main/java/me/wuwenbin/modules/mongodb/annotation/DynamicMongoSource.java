package me.wuwenbin.modules.mongodb.annotation;

import java.lang.annotation.*;

/**
 * 切换动态数据源的注解
 * Created by wuwenbin on 2017/4/22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicMongoSource {

    String key() default "";

    String db() default "";
}
