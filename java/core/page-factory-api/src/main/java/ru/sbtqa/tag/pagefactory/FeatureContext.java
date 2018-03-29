package ru.sbtqa.tag.pagefactory;

import java.util.Locale;

public class FeatureContext {

    private static Locale locale;

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        FeatureContext.locale = locale;
    }
}
