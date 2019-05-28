package ru.sbtqa.tag.pagefactory.utils;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public class PageUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PageUtils.class);

    public static void verifyPageByDataTestId() {
        List<WebElement> elementsIdentifyingPage = Environment.getDriverService().getDriver()
                .findElements(By.xpath("//*[contains(@data-test-id, 'pageId')]"));
        if (elementsIdentifyingPage.isEmpty()) {
            LOG.info("The check for the expected page was not performed. " +
                    "The data-test-id attribute is not set for the current page");
        } else {
            String pageTitle = PageContext.getCurrentPage().getTitle();
            List<WebElement> elements = TestIdUtils.findElementIdentifyingPage(pageTitle);

            if (elements.isEmpty()) {
                throw new AutotestError(String.format("The page does not match the expected. Was expected: %s", pageTitle));
            }
        }
    }
}
