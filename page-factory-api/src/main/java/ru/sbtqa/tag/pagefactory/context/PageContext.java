package ru.sbtqa.tag.pagefactory.context;

import ru.sbtqa.tag.pagefactory.Page;

public class PageContext {

    static ThreadLocal<String> currentPageTitle = new ThreadLocal<>();
    static ThreadLocal<Page> currentPage = new ThreadLocal<>();

    private PageContext() {
    }

    public static String getCurrentPageTitle() {
        return currentPageTitle.get();
    }

    private static void setCurrentPageTitle(String currentPageTitle) {
        PageContext.currentPageTitle.set(currentPageTitle);
    }

    public static Page getCurrentPage() {
        return currentPage.get();
    }

    public static void setCurrentPage(Page currentPage) {
        PageContext.currentPage.set(currentPage);
        PageContext.setCurrentPageTitle(currentPage.getTitle());
    }

    public static void clearPageContext() {
        PageContext.currentPage.remove();
        PageContext.currentPageTitle.remove();
    }
}
