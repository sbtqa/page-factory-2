package ru.sbtqa.tag.pagefactory.pages.jdielements;

import com.epam.jdi.uitests.web.selenium.elements.common.Button;
import com.epam.jdi.uitests.web.selenium.elements.common.Text;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "MainJ")
public class IndexPage extends AbstractPage {

    @ElementTitle("Sample text")
    @FindBy(xpath = "//*[@id='samples']/p[@class='lead']")
    private Text sampleText;

    @ElementTitle("License text")
    @FindBy(xpath = "//*[@id='license']/p[@class='lead']")
    private Text licenseText;

    @ElementTitle("Contact")
    @FindBy(xpath = "//a[contains(@href, 'contact')]")
    private Button contact;

    public IndexPage(WebDriver driver) {
        super(driver);
    }

}
