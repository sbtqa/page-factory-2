package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated by this, field will be set with content by some of previous response
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromResponse {

    /**
     * Search response for this endpoint entry
     * @return endpoint entry
     */
    Class endpoint() default void.class;

    /**
     * Just use response of previous executed endpoint entry
     * @return true if need to use previous response (last)
     */
    boolean previous() default true;

    /**
     * If you need get some string from response body, fill it with some xpath (jpath, or smth)
     */
    String path() default "";

    /**
     * If you need get header value, fill it with header name
     */
    String header() default "";

    /**
     * If you need to apply some mask on received value, fill it with regex
     */
    String mask() default "";

    /**
     * Parameter of optional the annotated field. If set false, then annotated
     * parameter will set to null when it was not found in document. If set true,
     * then annotated parameter will be set to equal value from responseEntry
     * answer, but if parameter wasn't found in document error exception will be
     * thrown and tests finished
     */
    boolean optional() default false;
}
