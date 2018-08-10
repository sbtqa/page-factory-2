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

    /**
     * endpoint class which response use to
     *
     * @return a {@link java.lang.Class} object.
     */
    Class endpointEntry() default void.class;

    /**
     * Use data from previous response. Use without 'endpointEntry'
     *
     * @return a {@link java.lang.Boolean} object.
     */
    boolean usePrevious() default true;

    /**
     * Path to search. Useful for body of response.
     *
     * @return a {@link java.lang.String} object.
     */
    String path() default "";

    /**
     * Header name. Take value of this name
     *
     * @return {@link java.lang.String} object.
     */
    String header() default "";

    /**
     * Regex mask on return value
     *
     * @return {@link java.lang.String} object.
     */
    String mask() default "";

    /**
     * Body of necessity the annotated field. If set false, then annotated
     * parameter will set to null when it was found in document. If set true,
     * then annotated parameter will be set to equal value from endpointEntry
     * answer, but if parameter wasn't found in document error exception will be
     * thrown and tests finished
     *
     * @return a {@link java.lang.String} object.
     */
    boolean necessity() default true;
}
