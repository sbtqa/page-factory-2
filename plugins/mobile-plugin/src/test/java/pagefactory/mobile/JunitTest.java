package pagefactory.mobile;

import org.junit.Ignore;
import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.MobileSteps;

public class JunitTest {

    @Test
    @Ignore
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
