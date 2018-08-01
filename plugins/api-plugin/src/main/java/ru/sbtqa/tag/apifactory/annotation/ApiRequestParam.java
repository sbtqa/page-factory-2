package ru.sbtqa.tag.apifactory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Api request parameter title.
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ApiRequestParam {

    /**
     * By this title parameter will be searched by framework
     *
     * @return a {@link java.lang.String} object.
     */
    public String title();
}
