package pagefactory.mobile.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.pagefactory.mobile.MobilePage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.utils.ExpectedConditionsUtils;

@PageEntry(title = "Страница авторизации. Ввод PIN")
public class PinPage extends MobilePage {

    @ElementTitle(value = "PIN")
    @FindBy(id = "ru.sberbank.sbt.eso:id/vPin")
    protected WebElement nptPin;

    public PinPage(WebDriver driver) {
        super(driver);
        ExpectedConditionsUtils.waitUntilElementPresent(nptPin);
    }

    @ActionTitle("вводит пин")
    public void enterPin() {
        int pinLength = 5;
        for (int i = 1; i <= pinLength; i++) {
            String xpath = "//android.view.View[@text='" + i + "']";
            WebElement webElement = getDriver().findElement(By.xpath(xpath));
            new MobilePageActions().click(webElement);
        }
    }
}
