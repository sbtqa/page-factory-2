package ru.sbtqa.tag.apifactory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Api response validation rule. To be executed, it must be directly called from
 * feature.
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiValidationRule {

    /**
     * By this title rule will be searched by framework
     *
     * @return {@link java.lang.String} object.
     */
    String title();
}
