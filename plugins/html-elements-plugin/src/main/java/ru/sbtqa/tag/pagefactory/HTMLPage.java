package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

/**
 * Page with common action with html-elements
 */
public class HTMLPage extends WebPage {

    public HTMLPage(WebDriver driver) {
        super(driver, new HtmlElementDecorator(
                new HtmlElementLocatorFactory(driver)));
    }
}
