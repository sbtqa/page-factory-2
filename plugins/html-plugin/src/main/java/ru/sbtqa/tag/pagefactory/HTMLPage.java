package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.html.actions.HtmlPageActions;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

/**
 * Inherit your html page objects from this class
 */
public abstract class HTMLPage extends WebPage {

    private static PageActions pageActions = new HtmlPageActions();

    public HTMLPage() {
        super(new CustomHtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())));

        Environment.setPageActions(pageActions);
        Environment.setReflection(new HtmlReflection());
        Environment.setFindUtils(new HtmlFindUtils());
    }

    public HTMLPage(FieldDecorator decorator) {
        super(decorator);
    }
}
