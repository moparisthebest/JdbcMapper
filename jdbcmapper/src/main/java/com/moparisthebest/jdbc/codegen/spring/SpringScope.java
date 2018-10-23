package com.moparisthebest.jdbc.codegen.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This puts a @org.springframework.context.annotation.Scope annotation on the generated class
 * <p>
 * If put on a package, applies to all classes generated under that package, if put on a
 * class or interface only applies to that class/interface, most specific class or package wins.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface SpringScope {
    /**
     * Used for @org.springframework.context.annotation.Scope.scopeName
     * <p>
     * Defaults to "request", which is, as of Spring 5, the value of:
     * org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST
     */
    String scopeName() default "request";

    /**
     * Used for @org.springframework.context.annotation.Scope.proxyMode
     * <p>
     * If empty string default is set, defaults to setting ScopedProxyMode.INTERFACES
     * when implementing an interface, or ScopedProxyMode.TARGET_CLASS otherwise
     */
    String proxyMode() default "";
}
