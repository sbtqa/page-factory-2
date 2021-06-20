package ru.sbtqa.tag.pagefactory.pages.htmlelements.IndexPages;

import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.HTMLPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks.MenuBlock;
import ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks.MenuBlockListVersion;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.TextBlock;

@PageEntry(title = "MainY")
public class IndexPage extends HTMLPage {

    @ElementTitle(value = "menu")
    private MenuBlock menu;
    
    @ElementTitle(value = "menu with list elements")
    @FindBy(xpath = "//ul[@class='nav navbar-nav']")
    private MenuBlockListVersion menuWithListElements;
    
    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private TextBlock sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private TextBlock licenseText;
    
    @ActionTitle("opened page")
    public void openedPage(String page) throws ElementDescriptionException, ElementNotFoundException{
        HtmlFindUtils htmlFindUtils = (HtmlFindUtils) Environment.getFindUtils();
        List<Link> links = htmlFindUtils.findList(null,"menu with list elements->toolbar");
        String attributeClass = ElementUtils.getElementByText(links, page).findElement(By.xpath("./..")).getAttribute("class");
        Assert.assertEquals("The path to element was compiled incorrectly.", "active", attributeClass);
    }
}
