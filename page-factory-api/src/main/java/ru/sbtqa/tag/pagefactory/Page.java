package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

/**
 * Entry point to the page-factory
 */
public interface Page {

    /**
     * Get title of current page object
     *
     * @return the title
     */
    default String getTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }

    /**
     * Get title of current page object
     *
     * @return the title
     */
    default String getUrl() {
        return this.getClass().getAnnotation(PageEntry.class).url();
    }
}
