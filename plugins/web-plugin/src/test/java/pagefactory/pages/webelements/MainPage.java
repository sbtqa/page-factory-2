package pagefactory.pages.webelements;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.web.junit.WebSteps;

@PageEntry(title = "Main")
public class MainPage extends AbstractPage {

    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private WebElement sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private WebElement licenseText;

    @Step("Step1 on page")
    public MainPage pageStep() {
        System.out.println(licenseText.getText());
        return this;
    }

    @Step("Step2 on page")
    public MainPage pageStep2() {
        System.out.println(licenseText.getText());
        return this;
    }

    private String nextPage;

    public MainPage() {}

    public MainPage(String nextPage) {
        this.nextPage = nextPage;
    }

    @ActionTitle("go to next page")
    public void GoToNextPage() throws PageException {
        WebSteps.getInstance().click(nextPage);
    }
}
