package pagefactory.mobile.pages.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.MobileExpectedConditionsUtils;
import ru.sbtqa.tag.pagefactory.MobilePage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Страница авторизации. Ввод PIN")
public class PinPage extends MobilePage {

    @ElementTitle(value = "PIN")
    @FindBy(id = "ru.sberbank.sbt.eso:id/vPin")
    protected WebElement nptPin;

    public PinPage(WebDriver driver) {
        super(driver);
	MobileExpectedConditionsUtils.waitUntilElementPresent(nptPin);
    }

    @ActionTitle("вводит пин")
    public void enterPin() {
	int pinLength = 5;
	for (int i = 1; i <= pinLength; i++) {
	    String xpath = "//android.view.View[@text='" + i + "']";
//	    clickWebElement(PageFactory.getDriver().findElement(By.xpath(xpath)));
	}
    }
}
