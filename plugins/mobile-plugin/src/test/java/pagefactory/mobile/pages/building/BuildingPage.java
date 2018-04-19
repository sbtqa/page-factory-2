package pagefactory.mobile.pages.building;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.mobile.utils.SwipeUtils;
import ru.sbtqa.tag.pagefactory.mobile.MobilePage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.utils.ExpectedConditionsUtils;

@PageEntry(title = "Здание")
public class BuildingPage extends MobilePage {

    @ElementTitle(value = "Заголовок")
    @FindBy(xpath = "//android.widget.TextView[@text='Здание']")
    protected WebElement lbl;

    @FindBy(id = "ru.sberbank.sbt.eso:id/suggest_list")
    protected WebElement list;

    public BuildingPage(WebDriver driver) {
        super(driver);
        ExpectedConditionsUtils.waitUntilElementPresent(lbl);
        ExpectedConditionsUtils.waitUntilElementPresent(list);
    }

    @ActionTitle("выбирает случайное значение из списка")
    public void getRandomItem() {
//	    clickWebElement(Utils.getRandomItem("ru.sberbank.sbt.eso:id/suggest_item_value"));
    }
}
