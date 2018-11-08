package ru.sbtqa.tag.pagefactory;

import org.junit.AfterClass;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class Tag {

    private Tag() {}

    @AfterClass
    public static void tearDown() {
        if (!Environment.isDriverEmpty()) {
            Environment.getDriverService().demountDriver();
        }
    }
}
