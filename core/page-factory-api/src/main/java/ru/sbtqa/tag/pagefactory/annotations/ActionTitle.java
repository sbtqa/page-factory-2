package ru.sbtqa.tag.pagefactory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//import java.lang.annotation.Repeatable;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionTitle {

    /**
     * Title text
     *
     * @return a {@link String} object.
     */
    public String value();
}
