package com.zxh.boot.log.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    //日志类型，1操作日志，2登录日志
    String type() default "1";
    //说明
    String value();
}
