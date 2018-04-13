package ru.sbtqa.tag.pagefactory.pages.htmlelements.ContactPages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.HTMLPage;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks.MenuBlock;
import ru.yandex.qatools.htmlelements.element.Radio;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

@PageEntry(title = "ContactY")
public class ContactPage extends HTMLPage {
    
    @ElementTitle(value = "menu")
    private MenuBlock menu;
    
    @FindBy(xpath = "//input[@name='first_name']")
    @ElementTitle(value = "first name")
    private TextInput nameInput;

    @FindBy(xpath = "//input[@name='last_name']")
    @ElementTitle(value = "last name")
    private TextInput lastInput;

    @FindBy(xpath = "//select[@name='state']")
    @ElementTitle(value = "state")
    private Select stateSelect;

    @FindBy(xpath = "//input[@name='hosting'][@value='yes']")
    @ElementTitle(value = "hosting yes")
    private Radio hostingYes;

    @FindBy(xpath = "//input[@name='hosting'][@value='no']")
    @ElementTitle(value = "hosting no")
    private Radio hostingNo;

    @FindBy(xpath = "//button[@type='submit']")
    @ElementTitle(value = "send")
    private WebElement sendButton;

    @FindBy(xpath = "//*[@id='error_message']")
    @ElementTitle(value = "error msg")
    private TextBlock errorMsg;
    
    public ContactPage() {
        PageFactory.initElements(new HtmlElementDecorator(
                new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())), this);
    }
    
    @ActionTitle("check that error message contains")
    public void errContains(String msg) {
        Assert.assertTrue(errorMsg.getText().contains(msg));
    }

    @ActionTitle("check that error message not contains")
    public void errNotContains(String msg) {
        Assert.assertFalse(errorMsg.getText().contains(msg));
    }
}
