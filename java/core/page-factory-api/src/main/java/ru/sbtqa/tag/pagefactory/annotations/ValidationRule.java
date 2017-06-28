package ru.sbtqa.tag.pagefactory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation rule. To be executed, it must be directly called from
 * feature.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidationRule {

    /**
     * By this title rule will be searched by framework
     *
     * @return rule title
     */
    String title();
}
