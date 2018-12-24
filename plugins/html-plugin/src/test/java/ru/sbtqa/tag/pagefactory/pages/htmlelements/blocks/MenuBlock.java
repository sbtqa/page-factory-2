package ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks;

import static java.lang.String.format;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Link;

@FindBy(xpath = "//nav")
public class MenuBlock extends HtmlElement {

    @ElementTitle("Contact")
    @FindBy(xpath = ".//a[text()='Contact']")
    private Link contact;

    @ElementTitle("Home")
    @FindBy(xpath = ".//a[text()='Home']")
    private Link home;
    
    @ActionTitle("go to page")
    public void goToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home":
                home.click();
                break;
            case "contact":
                contact.click();
                break;
            default:
                throw new AutotestError(format("Tab '%s' not found.", pageName));
        }
    }
}
