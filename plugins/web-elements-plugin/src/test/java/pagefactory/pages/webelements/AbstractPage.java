package pagefactory.pages.webelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.WebElementsPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.RedirectsTo;

public class AbstractPage extends WebElementsPage {

    @ElementTitle("Contact")
    @FindBy(xpath = "//a[text()='Contact']")
    protected WebElement contactButton;

    @ElementTitle("ContactRedirect")
    @FindBy(xpath = "//a[text()='Contact']")
    @RedirectsTo(page = ContactPage.class)
    private WebElement contactButtonWithRedirect;

    @ElementTitle("Home")
    @FindBy(xpath = "//a[text()='Home']")
    protected WebElement homeButton;

    @ElementTitle("HomeRedirect")
    @FindBy(xpath = "//a[text()='Home']")
    @RedirectsTo(page = IndexPage.class)
    private WebElement homeButtonWithRedirect;
    
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
