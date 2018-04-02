package ru.sbtqa.tag.pagefactory;


public class PageContext {

    private static String currentPageTitle;
    private static Page currentPage;

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
    
    public static void resetContext() {
        currentPage = null;
        currentPageTitle = null;
    }
}
