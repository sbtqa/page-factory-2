package pagefactory.mobile;

import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSteps;

public class JunitTest {

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
