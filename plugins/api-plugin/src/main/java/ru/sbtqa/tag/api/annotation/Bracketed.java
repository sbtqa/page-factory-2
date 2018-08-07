package ru.sbtqa.tag.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Change parameter name
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
// TODO изменить имя на более понятное
// TODO сейчас не работает
public @interface Bracketed {

    /**
     * Change parameter name to this value in time of transmitting parameter on
     * execute
     *
     * @return a {@link java.lang.String} object.
     */
    String value();

}
