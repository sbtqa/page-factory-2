package ru.sbtqa.tag.stepdefs;

public class HtmlSteps extends HtmlGenericSteps<HtmlSteps> {

    private static HtmlSteps instance;

    public static HtmlSteps getInstance() {
        if (instance == null) {
            instance = new HtmlSteps();
        }
        return instance;
    }
}
