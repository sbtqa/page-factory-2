package ru.sbtqa.tag.api.annotation;

import ru.sbtqa.tag.api.utils.EmptyClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate field to perform mutator for it
 * for example mutator can use previous value, which was set with {@link FromResponse} or {@link Stashed}
 * also, can be used to make Authorization header with concatenating " %authType% : " + previous value
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mutator {

    /**
     * Class name to search in
     */
    Class clazz() default EmptyClass.class;

    /**
     * Method name to call
     */
    String method();
}
