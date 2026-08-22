package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.FileCountResult;
import utils.SanmatsuAPIFileUtil;
import utils.TestExecutionReport;

public class Sanmatsu {

    private Page page;
    private String latestSocketFilesJson = "";

    // locators
    private final Locator hyperLinkIcon;
    private final Locator drawSocketIcon;

    // constructor

    public Sanmatsu(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.drawSocketIcon = page.locator(
                "//h4[text()='ドキュワークス図面']/preceding::input[@type='image' and @src='assets/icons/Drawing.png']");

        // Automatically capture getSocketFiles API response in real-time
        this.page.onResponse(res -> {
            if (res.url().contains("getSocketFiles") && res.status() == 200) {
                try {
                    String text = res.text();
                    if (text != null && !text.isBlank() && text.trim().startsWith("[")) {
                        this.latestSocketFilesJson = text;
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    // actions

    public void clickhyperlinkicon() {

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
        Allure.step("HyperLink icon clicked.");

    }

    public void clickdrawsocketicon() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        drawSocketIcon.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        Allure.step("Drawsocket icon clicked");

    }

    // validations

    public void homePageURL() {

        page.waitForTimeout(1000);

        page.waitForLoadState(LoadState.NETWORKIDLE);
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

    public void validatehyperlinkicon() {

        hyperLinkIcon.isVisible();

    }

    public void validatedrawsocketicon() {

        drawSocketIcon.isVisible();

    }

    public void hyperLinkPageURL() {

        page.waitForTimeout(1000);

        page.waitForLoadState(LoadState.NETWORKIDLE);
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

        page.waitForTimeout(1000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
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

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        Locator selectedValue = page.locator("(//label[contains(@class,'ui-dropdown-label')])[1]");

        String currentValue = selectedValue.textContent().trim();

        if (currentValue.equals(expectedValue)) {
            System.out.println("Pagination already set to " + expectedValue);
            return;
        }

        page.locator("(//span[contains(@class,'ui-dropdown-trigger-icon')])[1]").click();
        page.waitForTimeout(500);

        page.locator("//span[text()='" + expectedValue + "']").click();

        page.waitForCondition(() -> selectedValue.textContent().trim().equals(expectedValue));

        System.out.println("Pagination changed to " + expectedValue);
        Allure.step("Pagination changed to " + expectedValue);

        page.waitForLoadState(LoadState.NETWORKIDLE);

        page.waitForTimeout(4000);
    }

    public String getSocketFiles() {

        Allure.step("Waiting for Get Socket Files API response");

        // If response was already captured via onResponse listener, return it
        if (latestSocketFilesJson != null && !latestSocketFilesJson.isBlank()) {
            Allure.step("Using captured Get Socket Files API response");
            return latestSocketFilesJson;
        }

        try {
            Response response = page.waitForResponse(
                    res -> res.url().contains("getSocketFiles")
                            && res.status() == 200,
                    new Page.WaitForResponseOptions().setTimeout(60000),
                    () -> {
                        page.reload();
                    });

            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(2000);

            if (latestSocketFilesJson != null && !latestSocketFilesJson.isBlank()) {
                return latestSocketFilesJson;
            }

            return response.text();

        } catch (Exception e) {
            if (latestSocketFilesJson != null && !latestSocketFilesJson.isBlank()) {
                return latestSocketFilesJson;
            }
            throw new RuntimeException("Failed to get Socket Files API response for Sanmatsu", e);
        }
    }

    // private static LocalDate parseDate(String dateStr) {
    // if (dateStr == null || dateStr.isBlank()) {
    // return null;
    // }
    // String cleaned = dateStr.trim();
    // java.time.format.DateTimeFormatter[] formatters = new
    // java.time.format.DateTimeFormatter[] {
    // java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
    // java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
    // java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
    // java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
    // java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"),
    // java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"),
    // java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // };
    // for (java.time.format.DateTimeFormatter dtf : formatters) {
    // try {
    // if (cleaned.length() > 10 && (cleaned.contains(":") ||
    // cleaned.contains("T"))) {
    // return java.time.LocalDateTime.parse(cleaned, dtf).toLocalDate();
    // } else {
    // return java.time.LocalDate.parse(cleaned, dtf);
    // }
    // } catch (Exception ignored) {
    // }
    // }
    // try {
    // return java.time.LocalDate.parse(cleaned);
    // } catch (Exception ignored) {
    // }
    // return null;
    // }

    public void verifyTodayFileCountAndGetScreenshot() {

        // Wait until page is completely loaded
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        /*
         * Take screenshot of SANMATSU page.
         */
        String screenshotPath = BaseDriver.takeScreenshot(
                page,
                "SANMATSU_todayFiles");

        /*
         * ============================================================
         * SANMATSU API UTILITY
         * ============================================================
         */
        SanmatsuAPIFileUtil sanmatsuAPI = new SanmatsuAPIFileUtil();

        /*
         * Overall counts from all pages.
         */
        int totalFileCount = 0;
        int yellowIconFileCount = 0;
        int nonYellowIconFileCount = 0;

        /*
         * Get first page API response.
         */
        String currentResponseBody = getSocketFiles();

        int pageNumber = 1;

        /*
         * ============================================================
         * PROCESS ALL PAGES
         * ============================================================
         */
        while (true) {

            System.out.println(
                    "[SANMATSU] Processing page "
                            + pageNumber + "...");

            /*
             * Pass current page API response to
             * SanmatsuAPIFileUtil.
             *
             * The utility handles:
             *
             * 1. guid check
             * 2. filetype = 1
             * 3. modifieddate UTC -> India (+05:30)
             * 4. Today check
             * 5. attribute = 1 -> Yellow
             * 6. attribute = 0 -> Non Yellow
             */
            FileCountResult pageResult = sanmatsuAPI.getTodayFileCount(
                    currentResponseBody);

            /*
             * Add current page counts to overall counts.
             */
            totalFileCount += pageResult.getTotalFileCount();

            yellowIconFileCount += pageResult.getAttributeFileCount();

            nonYellowIconFileCount += pageResult.getNonAttributeFileCount();

            /*
             * Number of valid today's files on this page.
             */
            int todayFilesInThisPage = pageResult.getTotalFileCount();

            /*
             * ============================================================
             * GET CURRENT PAGE RECORD COUNT
             * ============================================================
             */
            int totalRecordsInThisPage = 0;

            try {

                ObjectMapper mapper = new ObjectMapper();

                JsonNode files = mapper.readTree(
                        currentResponseBody);

                if (files.isArray()) {

                    totalRecordsInThisPage = files.size();
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to read SANMATSU page "
                                + pageNumber
                                + " response",
                        e);
            }

            /*
             * ============================================================
             * PAGE LOG
             * ============================================================
             */
            System.out.println(
                    "[SANMATSU] Page "
                            + pageNumber
                            + " total records: "
                            + totalRecordsInThisPage
                            + ", valid today's files: "
                            + todayFilesInThisPage);

            /*
             * ============================================================
             * NO TODAY'S FILES
             * ============================================================
             */
            if (todayFilesInThisPage == 0) {

                System.out.println(
                        "[SANMATSU] No today's files found "
                                + "on page "
                                + pageNumber
                                + ". Stopping pagination.");

                break;
            }

            /*
             * ============================================================
             * OLDER RECORDS FOUND
             * ============================================================
             *
             * If the current page contains fewer valid today's files
             * than total records, older/non-file records have started.
             *
             * Today's files on this page are already counted.
             */
            if (todayFilesInThisPage < totalRecordsInThisPage) {

                System.out.println(
                        "[SANMATSU] Reached older/non-matching "
                                + "records on page "
                                + pageNumber
                                + " ("
                                + todayFilesInThisPage
                                + "/"
                                + totalRecordsInThisPage
                                + ").");

                System.out.println(
                        "[SANMATSU] All today's valid files "
                                + "on this page have been counted.");

                break;
            }

            /*
             * ============================================================
             * NEXT PAGE BUTTON
             * ============================================================
             */
            Locator nextButton = page.locator(
                    "//a[contains(@class,'ui-paginator-next')]");

            /*
             * Check whether Next button is available.
             */
            if (nextButton.isVisible()
                    && !nextButton
                            .getAttribute("class")
                            .contains("ui-state-disabled")) {

                pageNumber++;

                System.out.println(
                        "[SANMATSU] All "
                                + todayFilesInThisPage
                                + " records on previous page "
                                + "were valid today's files.");

                System.out.println(
                        "[SANMATSU] Moving to page "
                                + pageNumber
                                + "...");

                try {

                    /*
                     * Wait for the next getSocketFiles API response
                     * while clicking Next.
                     */
                    Response nextResponse = page.waitForResponse(
                            res -> res.url()
                                    .contains(
                                            "getSocketFiles")
                                    && res.status() == 200,

                            new Page.WaitForResponseOptions()
                                    .setTimeout(30000),

                            () -> nextButton.click());

                    /*
                     * Wait for page update.
                     */
                    page.waitForLoadState(
                            LoadState.NETWORKIDLE);

                    page.waitForTimeout(1500);

                    /*
                     * Get next page API response.
                     */
                    currentResponseBody = nextResponse.text();

                } catch (Exception e) {

                    System.out.println(
                            "[SANMATSU] Could not load "
                                    + "next page response: "
                                    + e.getMessage());

                    break;
                }

            } else {

                /*
                 * No next page.
                 */
                System.out.println(
                        "[SANMATSU] Reached last page ("
                                + pageNumber
                                + ").");

                break;
            }
        }

        /*
         * ============================================================
         * FINAL RESULT
         * ============================================================
         */
        System.out.println();

        System.out.println(
                "========================================");

        System.out.println(
                "     SANMATSU - ALL PAGES TODAY TOTAL");

        System.out.println(
                "========================================");

        System.out.println(
                "Today's Total File Count     : "
                        + totalFileCount);

        System.out.println(
                "Today's Yellow Icon Files    : "
                        + yellowIconFileCount);

        System.out.println(
                "Today's No Yellow Icon Files : "
                        + nonYellowIconFileCount);

        System.out.println(
                "========================================");

        /*
         * ============================================================
         * ALLURE
         * ============================================================
         */
        Allure.step(
                "Today's Total File Count: "
                        + totalFileCount);

        Allure.step(
                "Today's Yellow Icon Files: "
                        + yellowIconFileCount);

        Allure.step(
                "Today's No Yellow Icon Files: "
                        + nonYellowIconFileCount);

        /*
         * ============================================================
         * TEST EXECUTION REPORT
         * ============================================================
         */
        TestExecutionReport.addResult(
                "SANMATSU",
                "Today",
                totalFileCount,
                yellowIconFileCount,
                nonYellowIconFileCount,
                screenshotPath);
    }

}
