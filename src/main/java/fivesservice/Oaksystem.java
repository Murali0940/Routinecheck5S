package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.APIFileUtil;
import utils.FileCountResult;
import utils.OakSystemFileAPIUtil;
import utils.TestExecutionReport;

public class Oaksystem {

    private Page page;

    // locators

    private final Locator hyperLinkIcon;

    private final Locator drawSocketIcon;

    public Oaksystem(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.drawSocketIcon = page.locator(
                "//h4[text()='図面']/preceding::input[@src='assets/icons/Drawing.png']");
    }

    // methods

    public void homePageURL() {

        page.waitForTimeout(2000);

        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);
        Allure.step("Current URL: " + currentURL);

        if (currentURL.contains("home")) {
            System.out.println("Home page URL is correct.");
            Allure.step("Home page URL is correct.");
        } else {
            System.out.println("Home page URL is incorrect.");
            Allure.step("Home page URL is incorrect.");
        }
    }

    public void hyperLinkPageURL() {

        page.waitForTimeout(2000);

        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);
        Allure.step("Current URL: " + currentURL);

        if (currentURL.contains("adsocket")) {
            System.out.println("adsocket page URL is correct.");
            Allure.step("adsocket page URL is correct.");
        } else {
            System.out.println("adsocket page URL is incorrect.");
            Allure.step("adsocket page URL is incorrect.");
        }
    }

    public void drawSocketURL() {

        page.waitForTimeout(2000);
        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);
        Allure.step("Current URL: " + currentURL);

        if (currentURL.contains("draw")) {
            System.out.println("draw socket page URL is correct..");
            Allure.step("draw socket page URL is correct..");
        } else {
            System.out.println("draw socket page URL is incorrect.");
            Allure.step("draw socket page URL is incorrect.");
        }
    }

    public void setPagination(String expectedValue) {
        Allure.step("Setting pagination to " + expectedValue);

        Locator selectedValue = page.locator("(//label[contains(@class,'ui-dropdown-label')])[1]");

        String currentValue = selectedValue.textContent().trim();

        if (currentValue.equals(expectedValue)) {
            System.out.println("Pagination already set to " + expectedValue);
            return;
        }

        page.locator("(//span[contains(@class,'ui-dropdown-trigger-icon')])[1]").click();

        page.locator("//span[text()='" + expectedValue + "']").click();

        page.waitForCondition(() -> selectedValue.textContent().trim().equals(expectedValue));

        System.out.println("Pagination changed to " + expectedValue);
        Allure.step("Pagination changed to " + expectedValue);

        page.waitForLoadState();

        page.waitForTimeout(4000);
    }

    public Response getSocketFiles() {
        Allure.step("Getting socket files");

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                () -> {

                    // Trigger the API
                    page.reload();

                });
        return response;

    }

    // Actions

    public void clickhyperlinkicon() {

        page.waitForLoadState();
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
        Allure.step("HyperLink icon clicked..");

    }

    public void clickdrawsocketicon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        drawSocketIcon.click();
        Allure.step("Drawsocket icon clicked");

    }

    // validations

    public void validatehyperlinkicon() {

        hyperLinkIcon.isVisible();

    }

    public void validatedrawsocketicon() {

        drawSocketIcon.isVisible();

    }

    public void verifyTodayFileCountAndGetScreenshot() {

        page.waitForLoadState(LoadState.NETWORKIDLE);

        /*
         * Get Socket Files API response.
         */
        Response response = getSocketFiles();

        Allure.step("Get Socket Files API response");

        /*
         * ============================================================
         * OAKSYSTEM FILE COUNT
         * ============================================================
         *
         * OakSystem class will:
         *
         * 1. Read modifieddate
         * 2. Treat modifieddate as UTC
         * 3. Convert UTC + 05:30 (India)
         * 4. Check whether converted date is TODAY
         * 5. Count today's files
         * 6. Count Yellow files
         * 7. Count Non Yellow files
         */
        OakSystemFileAPIUtil okSystemFileAPIUtil = new OakSystemFileAPIUtil();

        FileCountResult result = okSystemFileAPIUtil.getTodayFileCount(
                response.text());

        /*
         * ============================================================
         * TAKE SCREENSHOT
         * ============================================================
         */
        String screenshotPath = BaseDriver.takeScreenshot(
                page,
                "OAKSYSTEM_todayFiles");

        /*
         * ============================================================
         * ADD RESULT TO TEST EXECUTION REPORT
         * ============================================================
         */
        TestExecutionReport.addResult(
                "OAKSYSTEM",
                "Today",
                result.getTotalFileCount(),
                result.getAttributeFileCount(),
                result.getNonAttributeFileCount(),
                screenshotPath);

        /*
         * Small wait after processing.
         */
        page.waitForTimeout(3000);
    }

}
