package pagefactory.mobile;

import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.MobileSteps;

public class JunitTest {

    @Test
    public void mobileTest() throws PageException {
        MobileSteps.getInstance()
                .openPage("Калькулятор")
                .click("1")
                .click("+")
                .click("2")
                .click("=")
                .checkValueIsEqual("result", "3");
    }

}
