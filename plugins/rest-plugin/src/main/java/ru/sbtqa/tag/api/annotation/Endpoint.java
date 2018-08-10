package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.sbtqa.tag.api.rest.HTTP;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Endpoint {

    /**
     * Title. Used to find out endpoint by framework.
     *
     * @return a {@link java.lang.String} object.
     */
    String title();

    /**
     * endpoint path. Relative to baseurl.
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
