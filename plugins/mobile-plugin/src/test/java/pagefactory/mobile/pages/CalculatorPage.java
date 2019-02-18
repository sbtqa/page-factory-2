package pagefactory.mobile.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.mobile.MobilePage;

@PageEntry(title = "Calc")
public class CalculatorPage extends MobilePage {

    @ElementTitle(value = "1")
//    @FindBy(id = "com.android.calculator2:id/digit_1")
    @FindBy(id = "net.ludeke.calculator:id/digit1")
    protected WebElement btn1;

    @ElementTitle(value = "2")
//    @FindBy(id = "com.android.calculator2:id/digit_2")
    @FindBy(id = "net.ludeke.calculator:id/digit2")
    protected WebElement btn2;

    @ElementTitle(value = "+")
//    @FindBy(id = "com.android.calculator2:id/op_add")
    @FindBy(id = "net.ludeke.calculator:id/plus")
    protected WebElement btnPlus;

    @ElementTitle(value = "=")
//    @FindBy(id = "com.android.calculator2:id/eq")
    @FindBy(id = "net.ludeke.calculator:id/equal")
    protected WebElement equalTo;

    @ElementTitle(value = "result")
//    @FindBy(id = "com.android.calculator2:id/result")
    @FindBy(xpath = "//android.widget.ViewSwitcher[@resource-id='net.ludeke.calculator:id/display']/android.widget.EditText")
    protected WebElement result;
}
