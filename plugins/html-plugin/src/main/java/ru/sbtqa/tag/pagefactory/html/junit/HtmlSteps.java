package ru.sbtqa.tag.pagefactory.html.junit;

public class HtmlSteps extends HtmlStepsImpl<HtmlSteps> {

    static final ThreadLocal<HtmlSteps> storage = ThreadLocal.withInitial(HtmlSteps::new);

    public static HtmlSteps getInstance() {
        return storage.get();
    }
}
