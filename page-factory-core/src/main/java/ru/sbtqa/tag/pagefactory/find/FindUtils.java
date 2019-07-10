package ru.sbtqa.tag.pagefactory.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

import java.lang.reflect.Field;

import static java.lang.String.format;

public class FindUtils implements Find {
    
    public static final Logger LOG = LoggerFactory.getLogger(FindUtils.class);

    @Override
    public <T> T getElementByTitle(Page page, String title) throws PageException {
        for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(page.getClass())) {
            if (Environment.getReflection().isRequiredElement(field, title)) {
                return Environment.getReflection().getElementByField(page, field);
            }
        }
        throw new ElementNotFoundException(format("Element '%s' is not present on current page '%s'", title, page.getTitle()));
    }
}
