package pagefactory;

import cucumber.api.java.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pagefactory.pages.webelements.MainPage;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.junit.CoreSetupSteps;
import ru.sbtqa.tag.pagefactory.web.junit.WebSetupSteps;
import ru.sbtqa.tag.pagefactory.web.junit.WebSteps;
import setting.JettySettings;

public class JunitTest {

    private static JettySettings server = new JettySettings();

    @BeforeClass
    public static void beforeClass() throws Exception {
        server.startJetty();
    }

    @Before
    public void setup() {
        CoreSetupSteps.preSetUp();
        WebSetupSteps.initWeb();
        CoreSetupSteps.setUp();
    }

    @Test
    public void webTest() throws PageException, NoSuchMethodException {
        WebSteps.getInstance()

                .openPage("Main")
                .click("Contact")

                .openPage("Contact")
                .reInitPage()
                .backPage()
                .click("Contact")
                .openPage("Contact")
                .checkEmpty("first name")
                .fill("first name", "Alex")
                .checkNotEmpty("first name")
                .checkValueIsEqual("first name", "Alex")
                .checkValueIsNotEqual("first name", "Billy")

                .action("clears all of the fields")

                .click("send")
                .action("check that error message contains", "Please specify your first name")

                .fill("first name", "Alex")
                .click("send")
                .action("check that error message not contains", "Please specify your first name")

                .action("clears all of the fields")
                .pressKey("Enter", "send")
                .action("check that error message contains", "Please specify your first name")
                .fill("first name", "Alex")
                .pressKey("Enter", "send")
                .action("check that error message not contains", "Please specify your first name")

                .select("state", "Novosibirsk")
                .checkValueIsEqual("state", "Novosibirsk")

                .setCheckBox("checkbox")
                .action("checks checkbox", "true");

//                .click("alert")
//                .acceptAlert("Alert text")
//                .click("alert")
//                .dismissAlert("Alert text");
    }

    @Test
    public void objectedTest() throws PageInitializationException {
        WebSteps.getInstance()
                .openPage(MainPage.class)
                .pageStep()
                .pageStep2();
    }

    @After
    public void dispose() {
        WebSetupSteps.disposeWeb();
        CoreSetupSteps.tearDown();
    }
}
