package ru.sbtqa.tag.pagefactory.pages.jdielements;

import com.epam.jdi.uitests.core.interfaces.complex.IDropDown;
import com.epam.jdi.uitests.web.selenium.elements.common.Button;
import com.epam.jdi.uitests.web.selenium.elements.common.Input;
import com.epam.jdi.uitests.web.selenium.elements.common.Text;
import com.epam.jdi.uitests.web.selenium.elements.complex.RadioButtons;
import com.epam.jdi.uitests.web.selenium.elements.complex.Selector;
import org.junit.Assert;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "ContactJ")
public class ContactPage extends AbstractPage {
    
    @FindBy(xpath = "//input[@name='first_name']")
    @ElementTitle(value = "first name")
    private Input nameInput;
    
    @FindBy(xpath = "//input[@name='last_name']")
    @ElementTitle(value = "last name")
    private Input lastInput;
    
    @FindBy(xpath = "//select[@name='state']")
    @ElementTitle(value = "state")
    private Selector stateSelect;
    
    @FindBy(xpath = "//input[@name='hosting'][@value='yes']")
    @ElementTitle(value = "hosting yes")
    private RadioButtons hostingYes;
    
    @FindBy(xpath = "//input[@name='hosting'][@value='no']")
    @ElementTitle(value = "hosting no")
    private RadioButtons hostingNo;

    @FindBy(xpath = "//button[@type='submit']")
    @ElementTitle(value = "send")
    private Button sendButton;
    
    @FindBy(xpath = "//*[@id='error_message']")
    @ElementTitle(value = "error msg")
    private Text errorMsg;

    @FindBy(xpath = "//*[@name='state']")
    @ElementTitle("City")
    private IDropDown city;

    @FindBy(xpath = "//div[@class='radio']")
    @ElementTitle("To be or not to be")
    private RadioButtons toBe;

    @ActionTitle("check that error message contains")
    public void errContains(String msg) {
        Assert.assertTrue(errorMsg.getText().contains(msg));
    }
    
    @ActionTitle("check that error message not contains")
    public void errNotContains(String msg) {
        Assert.assertFalse(errorMsg.getText().contains(msg));
    }
}
