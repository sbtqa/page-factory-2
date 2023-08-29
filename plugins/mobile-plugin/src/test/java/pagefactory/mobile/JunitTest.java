package pagefactory.mobile;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSteps;

@Ignore
public class JunitTest {

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @Test
    public void mobileTest() throws PageException {
        MobileSteps.getInstance()
                .openPage("Calc")
                .click("1")
                .click("+")
                .click("2")
                .click("=")
                .checkValueIsEqual("result", "3");
    }
}
