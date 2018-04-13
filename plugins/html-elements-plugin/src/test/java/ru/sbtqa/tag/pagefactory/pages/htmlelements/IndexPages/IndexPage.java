package ru.sbtqa.tag.pagefactory.pages.htmlelements.IndexPages;

import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.HTMLPage;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.pages.htmlelements.blocks.MenuBlock;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

@PageEntry(title = "MainY")
public class IndexPage extends HTMLPage {
    
    @ElementTitle(value = "menu")
    private MenuBlock menu;
    
    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private TextBlock sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private TextBlock licenseText;
    
    public IndexPage() {
        PageFactory.initElements(new HtmlElementDecorator(
                new HtmlElementLocatorFactory(Environment.getDriverService().getDriver())), this);
    }
}
