package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

/**
 * Page with common action with html-elements
 */
public abstract class HTMLPage extends WebPage {

    public HTMLPage() {
        super(new HtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())));
    }

    public HTMLPage(FieldDecorator decorator) {
        super(decorator);
    }
}
