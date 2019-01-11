package ru.sbtqa.tag.pagefactory.pages.htmlelements.ContactPages;

import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.HTMLPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exception.IncorrectElementTypeError;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks.MenuBlock;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Radio;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

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

    @FindBy(xpath = "//input[@name='check']")
    @ElementTitle(value = "checkbox")
    private CheckBox checkBox;

    @FindBy(xpath = "//button[@type='submit']")
    @ElementTitle(value = "send")
    private WebElement sendButton;

    @FindBy(xpath = "//*[@id='error_message']")
    @ElementTitle(value = "error msg")
    private TextBlock errorMsg;

    @FindBy(xpath = "//input[@type='text']")
    @ElementTitle(value = "input list")
    private List<TextInput> inputList;

    @ActionTitle("check that error message contains")
    public void errContains(String msg) {
        Assert.assertTrue(errorMsg.getText().contains(msg));
    }

    @ActionTitle("check that error message not contains")
    public void errNotContains(String msg) {
        Assert.assertFalse(errorMsg.getText().contains(msg));
    }
    
    @ActionTitle("check find web element")
    public void findWebElement() {
        HtmlFindUtils htmlFindUtils = (HtmlFindUtils) Environment.getFindUtils();
        WebElement webElement = htmlFindUtils.find("send", WebElement.class);
        Assert.assertTrue("Incorrect type", !webElement.getClass().isAssignableFrom(TypifiedElement.class));
        checkType(htmlFindUtils.find("send", Button.class), Button.class);
    }
    
    @ActionTitle("check find block")
    public void findBlock() {
        HtmlFindUtils htmlFindUtils = (HtmlFindUtils) Environment.getFindUtils();
        checkType(htmlFindUtils.find("menu", MenuBlock.class), MenuBlock.class);
        checkType(htmlFindUtils.find("menu", HtmlElement.class), MenuBlock.class);
    }
    
    @ActionTitle("check find typified element")
    public void findTypifiedElement() {
        HtmlFindUtils htmlFindUtils = (HtmlFindUtils) Environment.getFindUtils();
        checkType(htmlFindUtils.find("first name", TypifiedElement.class), TextInput.class);
        checkType(htmlFindUtils.find("first name", TextInput.class), TextInput.class);
        checkType(htmlFindUtils.find("first name", WebElement.class), TextInput.class);
    }
    
     @ActionTitle("check find element with incorrect type")
     public void findElementWithIncorrectType() {
         HtmlFindUtils htmlFindUtils = (HtmlFindUtils) Environment.getFindUtils();
         try {
             htmlFindUtils.find("first name", Button.class);
             throw new AutotestError("Type check failed.");
         } catch (IncorrectElementTypeError e) {
         }
     }

    private <T extends WebElement> void checkType(T element, Class expectedType) {
        Assert.assertTrue("Incorrect type", element.getClass().equals(expectedType));
    }
}
