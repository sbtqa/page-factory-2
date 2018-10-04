package pagefactory.pages.webelements;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

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

    @FindBy(xpath = "//input[@name='check']")
    @ElementTitle(value = "checkbox")
    private WebElement checkBox;
    
    @FindBy(xpath = "//button[@type='submit']")
    @ElementTitle(value = "send")
    private WebElement sendButton;

    @FindBy(xpath = "//button[@id='alert']")
    @ElementTitle(value = "alert")
    private WebElement alertButton;

    @FindBy(xpath = "//*[@id='error_message']")
    @ElementTitle(value = "error msg")
    private WebElement errorMsg;
    
    @ActionTitle("check that error message contains")
    public void errContains(String message) {
        WebPageChecks checks = new WebPageChecks();
        Assert.assertTrue(checks.checkEquality(errorMsg, message, MatchStrategy.CONTAINS));
    }

    @ActionTitle("check that error message not contains")
    public void errNotContains(String message) {
        WebPageChecks checks = new WebPageChecks();
        Assert.assertFalse(checks.checkEquality(errorMsg, message, MatchStrategy.CONTAINS));
    }

    @ActionTitle("clears all of the fields")
    public void clearsAllTheFields() {
        nameInput.clear();
        lastInput.clear();
    }

    @ActionTitle("checks checkbox")
    public void checksCheckbox(String isChecked) {
        Assert.assertEquals(Boolean.valueOf(isChecked), checkBox.isSelected());
    }
}
