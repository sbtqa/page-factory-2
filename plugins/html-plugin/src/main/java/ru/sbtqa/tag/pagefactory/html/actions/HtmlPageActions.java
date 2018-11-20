package ru.sbtqa.tag.pagefactory.html.actions;

import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.yandex.qatools.htmlelements.element.CheckBox;

public class HtmlPageActions extends WebPageActions {

    @Override
    public void setCheckbox(Object element, boolean state) {
        if (element.getClass().isAssignableFrom(CheckBox.class)) {
            ((CheckBox) element).select();
        } else {
            super.setCheckbox(element, true);
        }
    }
}
