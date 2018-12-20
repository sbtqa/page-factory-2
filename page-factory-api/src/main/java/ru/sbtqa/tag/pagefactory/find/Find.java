package ru.sbtqa.tag.pagefactory.find;

import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;

public interface Find {

    /**
     * Find specified Object by title annotation among current page fields
     *
     * @param page the page on which the method is executing
     * @param title title of the element to search
     * @param <T> supposed type of the field. if field cannot be cast into this
     * type, it will fail
     * @return Object found by corresponding title
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if failed to
     * find corresponding element or element type is set incorrectly
     */
    <T> T getElementByTitle(Page page, String title) throws PageException;
}
