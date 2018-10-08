package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

public interface Page {

    /**
     * Get title of current page object
     *
     * @return the title
     */
    default String getTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }
}
