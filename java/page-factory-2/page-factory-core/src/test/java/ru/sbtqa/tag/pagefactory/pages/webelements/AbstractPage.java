package ru.sbtqa.tag.pagefactory.pages.webelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.WebElementsPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;

public class AbstractPage extends WebElementsPage {
    
    @ElementTitle("Contact")
    @FindBy(xpath = "//a[text()='Contact']")
    private WebElement contactButton;
    
    @ElementTitle("Home")
    @FindBy(xpath = "//a[text()='Home']")
    private WebElement homeButton;
    
    public AbstractPage() {
        PageFactory.initElements(PageFactory.getDriver(), this);
    }
    
    @ActionTitle("go to page")
    public void goToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home" : homeButton.click();
            break;
            
            case "contact" : contactButton.click();
            break;
        }
    }
}
