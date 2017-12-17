package me.wuwenbin.modules.repository.provider.find.annotation;

import java.lang.annotation.*;

/**
 * created by Wuwenbin on 2017/11/2 at 10:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OrderBy {

    String value();

    Order order() default Order.ASC;


    enum Order {
        ASC,
        DESC
    }
}
