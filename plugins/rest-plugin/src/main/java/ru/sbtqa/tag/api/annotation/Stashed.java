package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.sbtqa.tag.api.annotation.strategies.By;

/**
 * Put parameter value in stash
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Stashed {

    By by() default By.NAME;

    String title() default "";
}
