package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.sbtqa.tag.api.Rest;

/**
 * Annotate class that is the inheritor of {@link ru.sbtqa.tag.api.EndpointEntry}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Endpoint {

    /**
     * You can get this endpoint entry from feature by this title
     * @return title of this endpoint entry
     */
    String title();

    /**
     * This should be look like my/endpoint/path.
     * <p>
     * NOTE: you need to specify base URI in application.properties like
     * api.baseURI = http://localhost:9998
     * @return path to template
     */
    String path() default "";

    /**
     * {@link Rest} method
     * @return {@link Rest} method
     */
    Rest method();

    /**
     * This should be look like templates/YourTemplate.json
     * <p>
     * NOTE: the recursive search will be performed in src/test/resources
     * @return path to template
     */
    String template() default "";
}
