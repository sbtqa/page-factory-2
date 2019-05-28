package ru.sbtqa.tag.pagefactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.Find;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.html.actions.HtmlPageActions;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.sbtqa.tag.pagefactory.html.properties.HtmlConfiguration;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.reflection.Reflection;
import ru.sbtqa.tag.pagefactory.utils.PageUtils;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

/**
 * Inherit your html page objects from this class
 */
public abstract class HTMLPage extends WebPage {

    private static final Logger LOG = LoggerFactory.getLogger(HTMLPage.class);

    private static PageActions pageActions = new HtmlPageActions();
    private static Reflection reflection = new HtmlReflection();
    private static Find find = new HtmlFindUtils();
    private static final HtmlConfiguration PROPERTIES = HtmlConfiguration.create();

    public HTMLPage() {
        super(new CustomHtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())));
        applyEnvironment();
        if (PROPERTIES.getVerifyPage()) {
            PageUtils.verifyPageByDataTestId();
        }
    }

    public HTMLPage(FieldDecorator decorator) {
        super(decorator);
        applyEnvironment();
        if (PROPERTIES.getVerifyPage()) {
            PageUtils.verifyPageByDataTestId();
        }
    }

    private void applyEnvironment() {
        Environment.setPageActions(pageActions);
        Environment.setReflection(reflection);
        Environment.setFindUtils(find);
    }

    public boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            LOG.debug("The element not found", e);
            return false;
        }
    }

    public boolean isElementPresent(String elementTitle) {
            WebElement typifiedElement = ((HtmlFindUtils) Environment.getFindUtils()).find(elementTitle, false);
            return isElementPresent(typifiedElement);
    }

    private boolean isStringMatch(String mask, String str) {
        Pattern pattern = Pattern.compile(mask);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    public void checkInputMask(String field, String mask) {
        WebElement element = ((HtmlFindUtils) Environment.getFindUtils()).find(field);
        String expectedText = element.getText();
        Assert.assertTrue("Text does not match the mask " + mask
                + ". The field contains the text: " + expectedText, isStringMatch(mask, expectedText));
    }
}
