package com.moparisthebest.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER
        //IFJAVA8_START
        , TYPE_USE
        //IFJAVA8_END
})
@Retention(RUNTIME)
public @interface TypeUseAnnotation {
}
