package ru.sbtqa.tag.stepdefs;

public class HtmlSteps extends HtmlGenericSteps<HtmlSteps> {

    static final ThreadLocal<HtmlSteps> storage = new ThreadLocal<HtmlSteps>() {
        @Override
        protected HtmlSteps initialValue() {
            return new HtmlSteps();
        }

    };

    public static HtmlSteps getInstance() {
        return storage.get();
    }
}
