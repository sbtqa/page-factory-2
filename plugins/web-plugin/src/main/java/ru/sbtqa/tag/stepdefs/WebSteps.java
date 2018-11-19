package ru.sbtqa.tag.stepdefs;

public class WebSteps extends WebGenericSteps<WebSteps> {

    private static WebSteps instance;

    public static WebSteps getInstance() {
        if (instance == null) {
            instance = new WebSteps();
        }
        return instance;
    }

}
