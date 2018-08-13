package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dependent response. Optional annotation. It will be used to try to initialize
 * endpoint parameter from response of previous executed request to api.
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromResponse {

    Class endpointEntry() default void.class;

    boolean usePrevious() default true;

    String path() default "";

    String header() default "";

    String mask() default "";

    boolean necessity() default true;
}
