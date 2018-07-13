package ru.sbtqa.tag.pagefactory.web.support;

public enum BrowserName {

    IE("ie"),
    INTERNET_EXPLORER("internet explorer"),
    IEXPLORE("iexplore"),
    CHROME("chrome"),
    FIREFOX("firefox"),
    OPERA("firefox"),
    SAFARI("safari");

    private String name;

    BrowserName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
