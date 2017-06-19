package ru.sbtqa.tag.pagefactory.annotations;


import ru.sbtqa.tag.pagefactory.Page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Points to page where browser will go after control clicked
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RedirectsTo {

    /**
     * Title text
     *
     * @return a {@link Class} object.
     */
    public Class <? extends Page> page();
    }
