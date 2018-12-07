package ru.sbtqa.tag.stepdefs;

public class WebSteps extends WebGenericSteps<WebSteps> {

    static final ThreadLocal<WebSteps> storage = new ThreadLocal<WebSteps>() {
        @Override
        protected WebSteps initialValue() {
            return new WebSteps();
        }

    };

    public static WebSteps getInstance() {
        return storage.get();
    }
}
