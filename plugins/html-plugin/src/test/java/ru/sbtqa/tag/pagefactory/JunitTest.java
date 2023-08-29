package ru.sbtqa.tag.pagefactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSetupSteps;
import ru.sbtqa.tag.pagefactory.html.junit.HtmlSteps;
import ru.sbtqa.tag.pagefactory.junit.CoreSetupSteps;
import ru.sbtqa.tag.pagefactory.web.junit.WebSetupSteps;
import setting.JettySettings;

@RunWith(JUnit4.class)
public class JunitTest {

    private static final JettySettings server = new JettySettings();

    @BeforeClass
    public static void before() throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        server.startJetty();
    }

    @Before
    public void setup() {
        CoreSetupSteps.preSetUp();
        WebSetupSteps.initWeb();
        HtmlSetupSteps.initHtml();
        CoreSetupSteps.setUp();
    }

    @Test
    public void htmlTest() throws PageException, NoSuchMethodException {
        HtmlSteps.getInstance()
                .openPage("MainY")
                .actionInBlock("menu", "go to page", "Contact")
                .openPage("ContactY")
                .action("check that error message not contains", "Please specify your first name")
                .click("send")
                .action("check that error message contains", "Please specify your first name")
                .fill("first name", "Alex")
                .click("send")
                .action("check that error message not contains", "Please specify your first name")
                .action("check that error message contains", "Please specify your last name")
                .click("send")
                .action("check that error message contains", "Please specify your last name")
                .fill("last name", "Alexeev")
                .click("send")
                .action("check that error message not contains", "Please specify your last name")
                .setCheckBox("checkbox")
                .actionInBlock("menu", "go to page", "Home")
                .openPage("MainY");
    }

    @After
    public void dispose() {
        WebSetupSteps.disposeWeb();
        HtmlSetupSteps.tearDown();
        CoreSetupSteps.tearDown();
    }
}
