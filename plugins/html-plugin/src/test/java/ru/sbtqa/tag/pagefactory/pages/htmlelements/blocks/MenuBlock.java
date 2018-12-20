package ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks;

import static java.lang.String.format;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("menu")
@FindBy(xpath = "//div[@class='navbar-collapse collapse']")
public class MenuBlock extends HtmlElement {

    @ElementTitle("Contact")
    @FindBy(xpath = ".//a[text()='Contact']")
    private Button contactButton;

    @ElementTitle("Home")
    @FindBy(xpath = ".//a[text()='Home']")
    private Button homeButton;

    @ActionTitle("go to page")
    public void goToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home":
                homeButton.click();
                break;

            case "contact":
                contactButton.click();
                break;
                
            default:
                throw new AutotestError(format("Tab '%s' not found.", pageName));
        }
    }
}
