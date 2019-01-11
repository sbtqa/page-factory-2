package ru.sbtqa.tag.pagefactory.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ActionTitles.class)
public @interface ActionTitle {

    /**
     * Title text
     *
     * @return a {@link String} object.
     */
    String value();
}
