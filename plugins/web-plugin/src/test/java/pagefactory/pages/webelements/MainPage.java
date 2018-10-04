package pagefactory.pages.webelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Main")
public class MainPage extends AbstractPage {
    
    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private WebElement sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private WebElement licenseText;
}
