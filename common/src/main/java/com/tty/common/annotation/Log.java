package com.tty.common.annotation;


import java.lang.annotation.*;

/**
 * 日志注解
 */
@Target(ElementType.METHOD)  //  作用在哪里 方法
@Retention(RetentionPolicy.RUNTIME) //  运行时有效
@Documented
public @interface Log {

    String value() default "";
}
