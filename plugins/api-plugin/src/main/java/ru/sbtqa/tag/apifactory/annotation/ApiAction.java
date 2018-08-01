package ru.sbtqa.tag.apifactory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.sbtqa.tag.apifactory.ApiEntry;
import ru.sbtqa.tag.apifactory.EmptyApiEntry;
import ru.sbtqa.tag.apifactory.rest.HTTP;

/**
 * This annotation used above ApiEntry class declaration
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiAction {

    /**
     * Api entry path. Relative to baseurl.
     *
     * @return a {@link java.lang.String} object.
     */
    public String path() default "";

    /**
     * Title. Used to find out Api entry by framework.
     *
     * @return a {@link java.lang.String} object.
     */
    public String title();

    /**
     * HTTP method used to execute api method
     *
     * @return a {@link HTTP} object.
     */
    public HTTP method();

    /**
     * Body template
     *
     * @return a {@link java.lang.String} object.
     */
    public String template() default "";

    /**
     * If entry redirects after execution - point target Api Entry
     *
     * @return a {@link java.lang.Class} object.
     */
    public Class<? extends ApiEntry> redirectsTo() default EmptyApiEntry.class;
}
