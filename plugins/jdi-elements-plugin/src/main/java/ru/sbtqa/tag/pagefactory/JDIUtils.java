package ru.sbtqa.tag.pagefactory;

import com.epam.jdi.uitests.core.logger.JDILogger;
import com.epam.jdi.uitests.web.selenium.elements.WebCascadeInit;
import com.epam.jdi.uitests.web.settings.WebSettings;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Utils methods for jdi-plugin
 */
public class JDIUtils {
    
    private static String driverName;
    
    public static void setJDIConfig(Supplier<WebDriver> driverSupplier)  {
        try {
            WebSettings.initFromProperties();
            WebSettings.logger = new JDILogger();
        } catch (IOException e) {
            throw new FactoryRuntimeException("Exception occurred during setting jdi", e);
        }
        driverName = WebSettings.useDriver(driverSupplier);
    }
    
    public static void initElementsOnPage(Object page) {
        new WebCascadeInit().initElements(page, driverName);
    }

    public static Object getElementByTitle(Page page, String title) throws PageException {
        for (Field field : FieldUtilsExt.getDeclaredFieldsWithInheritance(page.getClass())) {
            if (PageFactoryUtils.isRequiredElement(field, title)) {
                return PageFactoryUtils.getElementByField(page, field);
            }
        }

        throw new ElementNotFoundException(String.format("Element '%s' is not present on current page '%s''", title, page.getTitle()));
    }

}
