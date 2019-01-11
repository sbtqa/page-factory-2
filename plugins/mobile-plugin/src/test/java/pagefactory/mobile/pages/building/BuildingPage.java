package pagefactory.mobile.pages.building;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.mobile.MobilePage;
import ru.sbtqa.tag.pagefactory.utils.Wait;

@PageEntry(title = "Здание")
public class BuildingPage extends MobilePage {

    @ElementTitle(value = "Заголовок")
    @FindBy(xpath = "//android.widget.TextView[@text='Здание']")
    protected WebElement lbl;

    @FindBy(id = "ru.sberbank.sbt.eso:id/suggest_list")
    protected WebElement list;

    public BuildingPage() {
        Wait.visibility(lbl);
        Wait.visibility(list);
    }
}
