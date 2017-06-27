package ru.sbtqa.tag.pagefactory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageEntry {

    /**
     *
     * @return TODO
     */
    public String title();
        
    /**
     *
     * @return TODO
     */
    public String url() default "";
}
