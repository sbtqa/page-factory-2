package ru.sbtqa.tag.pagefactory.pages.jdielements;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "DonationsJ")
public class DonationsPage extends AbstractPage {

    public static final Logger LOG = LoggerFactory.getLogger(DonationsPage.class);

    public DonationsPage(WebDriver driver) {
        super(driver);
    }
}
