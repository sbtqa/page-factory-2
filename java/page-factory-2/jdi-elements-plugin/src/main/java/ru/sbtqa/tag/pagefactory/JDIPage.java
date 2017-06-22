package ru.sbtqa.tag.pagefactory;


import com.epam.jdi.uitests.web.selenium.elements.complex.Dropdown;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;

/**
 * Created by Liza on 22.06.17.
 */
public class JDIPage extends Page {

   @ActionTitle("ru.sbtqa.tag.pagefactory.select.dropdown.value")
    public void selectDropdownValue(String elementTitle, String value) throws PageException {
        Object element = JDIUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
       ((Dropdown)element).select(value);
    }
}
