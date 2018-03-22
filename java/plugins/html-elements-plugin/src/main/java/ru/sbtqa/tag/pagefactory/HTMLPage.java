package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;
import ru.yandex.qatools.htmlelements.element.CheckBox;

import static ru.sbtqa.tag.pagefactory.util.PageFactoryUtils.getElementByTitle;

/**
 * Page with common action with html-elements
 */
public class HTMLPage extends WebElementsPage {


    /**
     * Find web element with corresponding title, if it is a check box, select
     * it If it's a WebElement instance, check whether it is already selected,
     * and click if it's not Add corresponding parameter to allure report
     *
     * @param elementTitle WebElement that is supposed to represent checkbox
     *
     * @throws PageException if page was not initialized, or required element
     *                                                           couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select.checkBox")
    public void setCheckBox(String elementTitle) throws PageException {
        WebElement targetElement = getElementByTitle(this, elementTitle);
        if (targetElement.getClass().isAssignableFrom(CheckBox.class)) {
            ((CheckBox) targetElement).select();
        } else {
            setCheckBoxState(targetElement, true);
        }
        PageFactoryUtils.addToReport(elementTitle, " is checked");
    }
    
    /**
     * Check whether specified element is selected, if it isn't, click it
     * isSelected() doesn't guarantee correct behavior if given element is not a
     * selectable (checkbox,dropdown,radiobtn)
     *
     * @param webElement a WebElemet object.
     * @param state a boolean object.
     */
    private void setCheckBoxState(WebElement webElement, Boolean state) {
        if (null != state) {
            if (webElement.isSelected() != state) {
                webElement.click();
            }
        }
    }
}
