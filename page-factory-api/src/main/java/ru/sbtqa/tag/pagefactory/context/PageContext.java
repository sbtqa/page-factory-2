package ru.sbtqa.tag.pagefactory.context;

import ru.sbtqa.tag.pagefactory.Page;

public class PageContext {

    private static String currentPageTitle;
    private static Page currentPage;

    private PageContext() {
    }

    public static String getCurrentPageTitle() {
        return currentPageTitle;
    }

    private static void setCurrentPageTitle(String currentPageTitle) {
        PageContext.currentPageTitle = currentPageTitle;
    }

    public static Page getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(Page currentPage) {
        PageContext.currentPage = currentPage;
        PageContext.setCurrentPageTitle(currentPage.getTitle());
    }
}
