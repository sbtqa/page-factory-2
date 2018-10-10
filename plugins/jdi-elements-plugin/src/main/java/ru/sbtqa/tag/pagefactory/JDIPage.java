package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with jdi elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class JDIPage extends DefaultPage {

    public JDIPage() {
        JDIUtils.initElementsOnPage(this);
    }
}
