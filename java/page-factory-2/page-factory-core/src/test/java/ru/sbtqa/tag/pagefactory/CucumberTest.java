package ru.sbtqa.tag.pagefactory;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import ru.sbtqa.tag.cucumber.TagCucumber;

@RunWith(TagCucumber.class)
@CucumberOptions(monochrome = true, format = {"pretty"},
        glue = {"ru.sbtqa.tag.pagefactory.stepdefs", "ru.sbtqa.tag.pagefactory.htmlstepdefs", "setting",
        "ru.sbtqa.tag.pagefactory.pages.jdielements"},
        features = {"src/test/resources/tests"},
        tags = {"@jdiElementsCheck"})
public class CucumberTest {}