package pagefactory.mobile.pages.search;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.MobileExpectedConditionsUtils;
import ru.sbtqa.tag.pagefactory.MobilePage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "Страница поиска")
public class SearchPage extends MobilePage {

    @ElementTitle(value = "Поиск")
    @FindBy(id = "ru.sberbank.sbt.eso:id/frag_search_searchview")
    protected WebElement nptSearch;

    @FindBy(id = "ru.sberbank.sbt.eso:id/frag_search_recview")
    protected WebElement list;

    @ElementTitle(value = "Ремонт системы отопления")
    @FindBy(xpath = "//android.widget.TextView[@text='Ремонт системы отопления']")
    protected WebElement btnRepairHeatSystem;

    @ElementTitle(value = "Ремонт кровли/водостоков")
    @FindBy(xpath = "//android.widget.TextView[@text='Ремонт кровли/водостоков']")
    protected WebElement btnRepairRoofAndDrain;

    @ElementTitle(value = "Установка программного обеспечения")
    @FindBy(xpath = "//android.widget.TextView[@text='Установка программного обеспечения']")
    protected WebElement btnInstallSoftware;

    public SearchPage(WebDriver driver) {
        super(driver);
        MobileExpectedConditionsUtils.waitUntilElementPresent(nptSearch);
    }
    
    @ActionTitle("ждёт появления списка")
    public void waitListAppears() throws InterruptedException {
        Thread.sleep(1000);
        MobileExpectedConditionsUtils.waitUntilElementPresent(list);
    }
}
