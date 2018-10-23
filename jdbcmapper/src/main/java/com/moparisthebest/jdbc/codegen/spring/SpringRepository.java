package com.moparisthebest.jdbc.codegen.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This puts a @org.springframework.stereotype.Repository annotation on the generated class
 * <p>
 * If put on a package, applies to all classes generated under that package, if put on a
 * class or interface only applies to that class/interface, most specific class or package wins.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface SpringRepository {
    /**
     * Used for @org.springframework.stereotype.Repository.value
     */
    String value() default "";
}
