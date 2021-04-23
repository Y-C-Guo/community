package com.gyc.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
//有效时间，程序运行时有效
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
