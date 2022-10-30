package com.engine.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})//该注解可以加在方法和类上
@Retention(RetentionPolicy.RUNTIME)
public @interface ParseToken {
}
