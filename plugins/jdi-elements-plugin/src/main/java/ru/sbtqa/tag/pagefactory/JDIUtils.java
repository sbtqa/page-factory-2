package ru.sbtqa.tag.pagefactory;

import com.epam.jdi.uitests.core.logger.JDILogger;
import com.epam.jdi.uitests.web.selenium.elements.WebCascadeInit;
import com.epam.jdi.uitests.web.settings.WebSettings;
import java.io.IOException;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;

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
}
