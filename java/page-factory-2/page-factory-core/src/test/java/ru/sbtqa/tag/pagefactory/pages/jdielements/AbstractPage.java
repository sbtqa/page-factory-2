package ru.sbtqa.tag.pagefactory.pages.jdielements;

import com.epam.jdi.uitests.web.selenium.elements.common.Button;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.JDIUtils;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;

public class AbstractPage extends Page {
    
    @ElementTitle("Contact")
    @FindBy(xpath = "//a[text()='Contact']")
    private Button contactButton;
    
    @ElementTitle("Home")
    @FindBy(xpath = "//a[text()='Home']")
    private Button homeButton;
    
    public AbstractPage() {
        JDIUtils.initElementsOnPage(this);
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
