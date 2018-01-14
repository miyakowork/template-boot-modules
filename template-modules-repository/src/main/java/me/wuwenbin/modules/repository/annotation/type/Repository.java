package me.wuwenbin.modules.repository.annotation.type;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/10/30 at 11:59
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Repository {

    /**
     * 注入时候的beanName，不指定则默认为类名首字母小写
     *
     * @return
     */
    String value() default "";
}
