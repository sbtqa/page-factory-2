package pagefactory.mobile.pages.main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.MobileExpectedConditionsUtils;
import ru.sbtqa.tag.pagefactory.MobilePage;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Главная страница")
public class MainPage extends MobilePage {

    @ElementTitle(value = "Чем помочь?")
    @FindBy(id = "ru.sberbank.sbt.eso:id/frag_main_searchview")
    protected WebElement nptHelper;

    public MainPage(WebDriver driver) {
        super(driver);
        MobileExpectedConditionsUtils.waitUntilElementPresent(nptHelper);
    }
}
