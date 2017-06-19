package ru.sbtqa.tag.pagefactory;


import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

/**
 * Entry point to the page-factory
 */
public class Page {

    /**
     * Get title of current page object
     *
     * @return the title
     */
    public String getPageTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }
}
