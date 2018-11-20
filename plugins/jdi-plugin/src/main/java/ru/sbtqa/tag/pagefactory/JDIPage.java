package ru.sbtqa.tag.pagefactory;

import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

/**
 * Inherit your jdi page objects from this class
 */
public abstract class JDIPage implements Page {

    public JDIPage() {
        JDIUtils.initElementsOnPage(this);
    }
}
