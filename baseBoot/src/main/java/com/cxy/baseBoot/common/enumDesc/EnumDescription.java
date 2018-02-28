package com.cxy.baseBoot.common.enumDesc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 *  Java 枚举类（ENUM）描述
 */
public @interface EnumDescription {
    public String value() default "";
}
