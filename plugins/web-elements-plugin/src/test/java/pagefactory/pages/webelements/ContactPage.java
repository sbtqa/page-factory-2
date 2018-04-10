package pagefactory.pages.webelements;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.TestEnvironment;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Contact")
public class ContactPage extends AbstractPage {
    
    @FindBy(xpath = "//input[@name='first_name']")
    @ElementTitle(value = "first name")
    private WebElement nameInput;
    
    @FindBy(xpath = "//input[@name='last_name']")
    @ElementTitle(value = "last name")
    private WebElement lastInput;
    
    @FindBy(xpath = "//select[@name='state']")
    @ElementTitle(value = "state")
    private WebElement stateSelect;
    
    @FindBy(xpath = "//input[@name='hosting'][@value='yes']")
    @ElementTitle(value = "hosting yes")
    private WebElement hostingYes;
    
    @FindBy(xpath = "//input[@name='hosting'][@value='no']")
    @ElementTitle(value = "hosting no")
    private WebElement hostingNo;
    
    
    @FindBy(xpath = "//button[@type='submit']")
    @ElementTitle(value = "send")
    private WebElement sendButton;
    
    @FindBy(xpath = "//*[@id='error_message']")
    @ElementTitle(value = "error msg")
    private WebElement errorMsg;
    
    public ContactPage() {
        WebDriver webDriver = TestEnvironment.getDriverService().getDriver();
        PageFactory.initElements(webDriver, this);
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
