package ru.sbtqa.tag.pagefactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, format = {"pretty"},
        glue = {"ru.sbtqa.tag.pagefactory.stepdefs", "ru.sbtqa.tag.pagefactory.htmlstepdefs",
                "setting"
        },
        features = {"src/test/resources/features"})
public class CucumberTest {}