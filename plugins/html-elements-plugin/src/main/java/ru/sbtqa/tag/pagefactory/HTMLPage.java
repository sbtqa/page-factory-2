package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

/**
 * Inherit your html page objects from this class
 */
public abstract class HTMLPage extends WebPage {

    public HTMLPage() {
        super(new CustomHtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())));
    }

    public HTMLPage(FieldDecorator decorator) {
        super(decorator);
    }
}
