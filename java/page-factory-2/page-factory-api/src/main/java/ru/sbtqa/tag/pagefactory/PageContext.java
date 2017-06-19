package ru.sbtqa.tag.pagefactory;


/**
 * Contain current Page instance
 */
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
    
    /**
     * Set current page and currentTitle
     * @param currentPage
     */
    public static void setCurrentPage(Page currentPage) {
        PageContext.currentPage = currentPage;
        PageContext.setCurrentPageTitle(currentPage.getPageTitle());
    }
}
