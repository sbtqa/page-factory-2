package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.sbtqa.tag.api.rest.HTTP;

/**
 * This annotation used above ApiEntry class declaration
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Endpoint {

    /**
     * Title. Used to find out Api entry by framework.
     *
     * @return a {@link java.lang.String} object.
     */
    String title();

    /**
     * Api entry path. Relative to baseurl.
     *
     * @return a {@link java.lang.String} object.
     */
    String path() default "";

    /**
     * HTTP method used to execute api method
     *
     * @return a {@link HTTP} object.
     */
    HTTP method();

    /**
     * Body template
     *
     * @return a {@link java.lang.String} object.
     */
    String template() default "";
}