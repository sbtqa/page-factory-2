package pagefactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;
import ru.sbtqa.tag.stepdefs.WebSteps;
import setting.JettySettings;

public class JunitTest {

    private static WebSteps web;
    private static JettySettings server = new JettySettings();

    @BeforeClass
    public static void before() throws Exception {
        web = WebSteps.getInstance();
        server.startJetty();
    }

    @Test
    public void webTestTitles() throws PageException, NoSuchMethodException {
        web.openPage("Main")
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
                .action("checks checkbox", "true")

                .click("alert")
                .acceptAlert("Alert text")
                .click("alert")
                .dismissAlert("Alert text");
    }

    @AfterClass
    public static void after() {
        CoreSetupSteps.tearDown();
    }
}
