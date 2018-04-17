package ru.sbtqa.tag.pagefactory.pages.jdielements;

import com.epam.jdi.uitests.core.interfaces.complex.interfaces.ICell;
import com.epam.jdi.uitests.core.interfaces.complex.interfaces.ITable;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.objects.JTable;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

@PageEntry(title = "DonationsJ")
public class DonationsPage extends AbstractPage {

    public static final Logger LOG = LoggerFactory.getLogger(DonationsPage.class);

    public DonationsPage(WebDriver driver) {
        super(driver);
    }

    @JTable(root = @FindBy(css = ".table-bordered"))
    @ElementTitle("Table")
    public ITable table;


    @ActionTitle("read table")
    public void readTable() {
        List<ICell> cells = table.getCells();
        for (int i = 0; i < cells.size(); i = i + 5) {
            LOG.info("Values: {} {} {} {} {}",
                    cells.get(i).getValue(),
                    cells.get(i + 1).getValue(),
                    cells.get(i + 2).getValue(),
                    cells.get(i + 3).getValue(),
                    cells.get(i + 4).getValue());
        }
    }
}
