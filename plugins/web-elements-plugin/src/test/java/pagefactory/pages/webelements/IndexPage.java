package pagefactory.pages.webelements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.TestEnvironment;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Main")
public class IndexPage extends AbstractPage {
    
    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private WebElement sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private WebElement licenseText;

    @ActionTitle("go to page from Main")
    public void goToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home" : homeButton.click();
                break;

            case "contact" : contactButton.click();
                break;
        }
    }
    
    public IndexPage() {
        WebDriver webDriver = TestEnvironment.getDriverService().getDriver();
        PageFactory.initElements(webDriver, this);
    }
}
