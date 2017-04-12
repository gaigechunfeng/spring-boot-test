package com.wk.boot.annotation;

import java.lang.annotation.*;

/**
 * Created by 005689 on 2017/4/12.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    String tableName() default "";
}
