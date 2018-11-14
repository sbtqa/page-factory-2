package ru.sbtqa.tag.pagefactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;
import ru.sbtqa.tag.stepdefs.HtmlSteps;

public class JunitTest {

    private static HtmlSteps html;

    @Before
    public void before() {
        html = HtmlSteps.getInstance();
    }

    @Test
    public void webTestTitles() throws PageException, NoSuchMethodException {
        html.openPage("Test Automation Gears")
                .fill("Search repositories...", "page-factory-2-example")
                .click("page-factory-2-example")

                .openPage("Page-factory-2 example")
                .action("выбирает ветку", "for-example")
                .click("example.txt")

                .openPage("Example")
                .checkValueIsEqual("Text", "Тестовый текст для примера");
    }

    @After
    public void after() {
        CoreSetupSteps.tearDown();
    }
}
