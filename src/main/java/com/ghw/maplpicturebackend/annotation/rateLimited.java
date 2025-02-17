package com.ghw.maplpicturebackend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ghost
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface rateLimited {

    /**
     * 指定某一段时间，默认60m秒
     * @return
     */
    long value() default 60;

    /**
     * 指定时间内的最大操作数量
     * @return
     */
    int limit() default 6;
}