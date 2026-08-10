package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;
import base.BaseDriver;

public class Homepage {

    private Page page;
    private static final Logger logger = LogManager.getLogger(Homepage.class);

    // Locators

    private Locator logo;
    private Locator searchInput;

    public Homepage(Page page) {
        this.page = page;
        this.logo = page.locator("//img[@src='assets/icons/logo.png']");
        this.searchInput = page.getByPlaceholder("Search");

    }

    public String getTitle() {
        return page.title();
    }

    public void searchoptions(String option) {

        Locator filename = page.getByText("Filename");
        Locator attribute = page.getByText("Attributes");
        Locator content = page.getByText("Content");

        if (filename.isChecked() && attribute.isChecked() && content.isChecked()) {
            filename.uncheck();
            attribute.uncheck();
            content.uncheck();
            System.out.println("All search options unchecked.");
            logger.info("All search options unchecked.");
        } else {
            System.out.println("Not all search options are checked. Current states - Filename: " + filename.isChecked()
                    + ", Attributes: " + attribute.isChecked() + ", Content: " + content.isChecked());
            logger.info("Not all search options are checked. Current states - Filename: " + filename.isChecked()
                    + ", Attributes: " + attribute.isChecked() + ", Content: " + content.isChecked());
        }

        switch (option.toLowerCase()) {
            case "filename":
                if (!filename.isChecked()) {
                    attribute.uncheck();
                    content.uncheck();
                    System.out.println("Filename option selected..");
                    logger.info("Filename option selected..");
                } else {
                    System.out.println("Filename option is already selected..");
                    logger.info("Filename option is already selected..");
                }
                break;
            case "attributes":
                if (!attribute.isChecked()) {
                    filename.uncheck();
                    content.uncheck();
                    System.out.println("Attributes option selected.");
                    logger.info("Attributes option selected.");
                } else {
                    System.out.println("Attributes option is already selected.");
                    logger.info("Attributes option is already selected.");
                }
                break;
            case "content":
                if (!content.isChecked()) {
                    filename.uncheck();
                    attribute.uncheck();
                    System.out.println("Content option selected.");
                    logger.info("Content option selected.");
                } else {
                    System.out.println("Content option is already selected.");
                    logger.info("Content option is already selected.");
                }
                break;
            default:
                System.out.println("Invalid search option: " + option);
                logger.warn("Invalid search option: " + option);
        }

    }

    // Click Search Button
    private void clickSearchButton() {
        page.locator("//img[@src='assets/ai-search.png']").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        page.locator("//img[@src='assets/ai-search.png']").click();
    }

    // Search File

    private void searchFile(String searchTerm, String type) {

        searchInput.click();
        searchInput.fill(searchTerm);
        Allure.step("Test entered in search bar.");
        logger.info("Test entered in search bar.");

        page.getByText("ui-btn").click();
        selectType(type);
        Allure.step("Type is selected.");
        logger.info("Type is selected.");

        clickSearchButton();

        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("Search completed : " + searchTerm);
        Allure.step("Search completed : " + searchTerm);
        logger.info("Search completed : " + searchTerm);
    }

    // Open First Search Result
    private Page openFirstSearchResult(String type) {

        return page.context().waitForPage(() -> {

            if (type.equalsIgnoreCase("File")) {

                Locator file = page.locator("div.imageDiv").first();

                file.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

                file.dblclick();
                Allure.step("First file double-clicked in search result.");
                logger.info("First file double-clicked in search result.");

            } else if (type.equalsIgnoreCase("Folder")) {

                Locator folder = page.locator("(//div[@class='imageDivSmall'])[1]").first();

                folder.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

                folder.dblclick();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
                Allure.step("First folder double-clicked.");
                logger.info("First folder double-clicked.");

                Locator fileInsideFolder = page.locator("div.imageDiv").first();

                fileInsideFolder.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

                fileInsideFolder.dblclick();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
                Allure.step("First file inside folder double-clicked.");
                logger.info("First file inside folder double-clicked.");
            } else {
                System.out.println("Invalid type provided: " + type);
                logger.warn("Invalid type provided: " + type);
                Allure.step("Invalid type provided: " + type);
            }

        });
    }

    // Wait Until Viewer Opens
    private void waitForViewer(Page viewerPage) {

        viewerPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
        viewerPage.waitForLoadState(LoadState.NETWORKIDLE);
        Allure.step("waiting for viewer");
        logger.info("waiting for viewer");

    }

    // Capture Screenshot
    private void captureViewerScreenshot(Page viewerPage, String searchTerm) {

        BaseDriver.takeScreenshot(viewerPage, "Search_Result_" + searchTerm);

        Allure.step("screenshot captured");
        logger.info("screenshot captured");

    }

    // Detect Viewer
    private String getViewerName(String url) {

        if (url.contains("pdfviewer"))
            return "PDF Viewer";

        if (url.contains("a3dviewer"))
            return "3D Viewer";

        if (url.contains("csvviewer"))
            return "CSV Viewer";

        if (url.contains("drawing"))
            return "Drawing Viewer";

        return "Unknown Viewer";
    }

    // Return Home
    private void returnToHome(Page viewerPage) {

        viewerPage.close();

        alfadocklogo();

        page.locator("//img[contains(@src,'logo.png')]")
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        page.locator("//img[contains(@src,'logo.png')]").click();

        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("Returned to Home page.");
        Allure.step("return to homepage");
        logger.info("Returned to Home page.");

    }

    private void alfadocklogo() {
        Assert.assertTrue(
                logo.isVisible(),
                "Alfadock logo is not visible on the Home page.");
        System.out.println("Alfadock Logo is visible.");
        Allure.step("Alfadock Logo is visible.");
        logger.info("Alfadock Logo is visible.");
    }

    private void selectType(String type) {

        Locator checkbox = page.locator(
                "//label[text()='" + type + "']/preceding-sibling::div//div[contains(@class,'ui-chkbox-box')]");

        Locator icon = page.locator(
                "//label[text()='" + type + "']/preceding-sibling::div//span[contains(@class,'ui-chkbox-icon')]");

        if (!icon.getAttribute("class").contains("pi-check")) {
            checkbox.click();
            System.out.println(type + " selected.");
            Allure.step(type + " selected.");
            logger.info(type + " selected.");
        } else {
            System.out.println(type + " is already selected.");
            Allure.step(type + " is already selected.");
            logger.info(type + " is already selected.");
        }
    }

    private void click_First_File() {

        page.waitForLoadState();

        Locator file = page.locator("div.imageDiv").first();

        file.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        file.click();
        Allure.step("First file clicked in search result.");
        logger.info("First file clicked in search result.");

    }

    private void click_First_Folder() {

        Locator folder = page.locator("div.imageDivSmall").first();

        folder.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        folder.click();
        Allure.step("First folder clicked in search result.");
        logger.info("First folder clicked in search result.");
    }

    public void search_File_in_SearchBar(String searchTerm, String type) {

        String methodName = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();

        System.out.println(methodName);
        Allure.step("Executing search_File_in_SearchBar method.");
        logger.info("Executing search_File_in_SearchBar method.");

        searchFile(searchTerm, type);

        Page viewerPage = openFirstSearchResult(type);

        waitForViewer(viewerPage);

        String viewerUrl = viewerPage.url();

        System.out.println("Viewer URL : " + viewerUrl);
        Allure.step("Viewer URL : " + viewerUrl);
        logger.info("Viewer URL : " + viewerUrl);

        page.waitForTimeout(5000);

        captureViewerScreenshot(viewerPage, searchTerm);

        System.out.println(getViewerName(viewerUrl) + " is opened.");
        Allure.step(getViewerName(viewerUrl) + " is opened.");
        logger.info(getViewerName(viewerUrl) + " is opened.");

        returnToHome(viewerPage);
    }

    public void search_File_Root_Location(String searchTerm, String filetype) {
        String methodName = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();

        System.out.println(methodName);
        Allure.step("Executing search_File_Root_Location method.");
        logger.info("Executing search_File_Root_Location method.");

        searchFile(searchTerm, filetype);
        searchoptions("Filename");
        System.out.println("Search Term : " + searchTerm);
        System.out.println("Type : " + filetype);

        click_First_File();

        Allure.step("First file clicked in search result.");
        logger.info("First file clicked in search result.");
        Assert.assertTrue(page.getByTitle("File Location").isVisible(), "Root icon is not visible.");
        System.out.println("File Location is visible.");
        Allure.step("Root icon is visible.");
        logger.info("Root icon is visible.");

        page.getByTitle("File Location").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("Clicked on Root icon.");
        Allure.step("Clicked on Root icon.");
        logger.info("Clicked on Root icon.");
        page.waitForTimeout(5000);

        captureViewerScreenshot(page, searchTerm);
        alfadocklogo();
        page.waitForLoadState();

    }

    public void search_Folder_Root_Location(String searchTerm, String filetype) {
        String methodName = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();

        System.out.println(methodName);
        Allure.step("Executing search_Folder_Root_Location method.");
        logger.info("Executing search_Folder_Root_Location method.");

        searchFile(searchTerm, filetype);
        searchoptions("Filename");
        click_First_Folder();
        page.waitForLoadState();
        click_First_File();

        Allure.step("First folder clicked in search result.");
        logger.info("First folder clicked in search result.");
        Assert.assertTrue(page.getByTitle("File Location").isVisible(), "Root icon is not visible.");
        System.out.println("File Location is visible.");
        Allure.step("Root icon is visible.");
        logger.info("Root icon is visible.");

        page.getByTitle("File Location").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("Clicked on Root icon.");
        Allure.step("Clicked on Root icon.");
        logger.info("Clicked on Root icon.");

        page.locator("(//div[@class='imageDiv' or @class='imageDivSmall'])[1]")
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        captureViewerScreenshot(page, searchTerm);
        page.waitForTimeout(2000);
        alfadocklogo();
        page.waitForLoadState();

    }

}