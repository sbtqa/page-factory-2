package ru.sbtqa.tag.stepdefs;

public class HtmlSteps extends HtmlGenericSteps<HtmlSteps> {

    static final ThreadLocal<HtmlSteps> storage = ThreadLocal.withInitial(HtmlSteps::new);

    public static HtmlSteps getInstance() {
        return storage.get();
    }
}
