package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

/**
 * Entry point to the page-factory:rest-plugin
 */
public interface ApiEndpoint {

    /**
     * Get title of current api endpoint
     *
     * @return the title
     */
    default String getTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }

    /**
     * Get url of current api endpoint
     *
     * @return the url
     */
    default String getPath() {
        return this.getClass().getAnnotation(PageEntry.class).url();
    }
}
