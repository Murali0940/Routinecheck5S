package fivesservice;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.FileCountResult;
import utils.MasudaAPIFileUtil;
import utils.TestExecutionReport;

public class Masuda {

    private Page page;

    // locators

    private final Locator hyperLinkIcon;
    private final Locator scanSocketIcon;

    // methods

    public Masuda(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.scanSocketIcon = page.locator(
                "//input[@type='image' and @src='assets/icons/Scan.png']");
    }

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

        if (currentURL.contains("scan")) {
            System.out.println("scan page URL is correct..");
            Allure.step("scan page URL is correct..");
        } else {
            System.out.println("scan page URL is incorrect.");
            Allure.step("scan page URL is incorrect.");
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

    public String getSocketFiles() {

        Allure.step("Waiting for Get Socket Files API response");

        final String[] responseBody = { null };

        page.waitForResponse(
                res -> {

                    if (res.url().contains("getSocketFiles")
                            && res.status() == 200) {

                        try {

                            responseBody[0] = res.text();

                            return true;

                        } catch (Exception e) {

                            System.out.println(
                                    "[MASUDA] Failed to read "
                                            + "getSocketFiles response: "
                                            + e.getMessage());

                            return false;
                        }
                    }

                    return false;
                },

                new Page.WaitForResponseOptions()
                        .setTimeout(60000),

                () -> {
                    page.reload();
                });

        if (responseBody[0] == null
                || responseBody[0].isBlank()) {

            throw new RuntimeException(
                    "[MASUDA] getSocketFiles API response "
                            + "was empty or could not be read.");
        }

        Allure.step(
                "Get Socket Files API response received successfully");

        return responseBody[0];
    }

    // ============================================================
    // PUBLIC ACTIONS
    // ============================================================

    public void clickHyperLinkIcon() {
        hyperLinkIcon.scrollIntoViewIfNeeded();
        page.waitForLoadState();
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
        Allure.step("HyperLink icon clicked");
    }

    public void clickScanSocketIcon() {

        scanSocketIcon.scrollIntoViewIfNeeded();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        scanSocketIcon.click();
        Allure.step("Scan Socket icon clicked");
    }

    // ============================================================
    // VALIDATIONS
    // ============================================================

    public void validateHyperLinkIcon() {
        page.waitForTimeout(2000);
        if (hyperLinkIcon.isVisible()) {
            System.out.println("HyperLink icon is displayed.");
            Allure.step("HyperLink icon is displayed.");
        } else {
            System.out.println("HyperLink icon is not displayed.");
            Allure.step("HyperLink icon is not displayed.");
        }
    }

    public void validateScanSocketIcon() {
        page.waitForTimeout(2000);
        if (scanSocketIcon.isVisible()) {
            System.out.println("Scan Socket icon is displayed.");
            Allure.step("Scan Socket icon is displayed.");
        } else {
            System.out.println("Scan Socket icon is not displayed.");
            Allure.step("Scan Socket icon is not displayed.");
        }
    }

    // ============================================================
    // VERIFY TODAY FILE COUNT — LOOP ALL TODAY FOLDERS
    // ============================================================

    public void verifyTodayFileCountAndGetScreenshot() {

        page.waitForLoadState(LoadState.NETWORKIDLE);

        // -------------------------------------------------------
        // Take screenshot of the S09 folders page
        // -------------------------------------------------------
        String folderScreenshotPath = BaseDriver.takeScreenshot(
                page,
                "MASUDA_SCAN_Folders");

        // -------------------------------------------------------
        // STEP 1: Get the top-level API response to find
        // today's folders (items with NO guid).
        // -------------------------------------------------------

        String topLevelJson = getSocketFiles();

        MasudaAPIFileUtil api = new MasudaAPIFileUtil();

        List<String> todayFolders = api.getTodayFolderNames(topLevelJson);

        if (todayFolders.isEmpty()) {
            System.out.println("[MASUDA] No today's folders found in S09 socket.");
            Allure.step("[MASUDA] No today's folders found.");

            TestExecutionReport.addResult(
                    "MASUDA",
                    "Today",
                    0,
                    0,
                    0,
                    folderScreenshotPath);
            return;
        }

        System.out.println("[MASUDA] Today's folders to process: " + todayFolders);
        Allure.step("[MASUDA] Today's folders: " + todayFolders);

        int grandTotalFiles = 0;
        int grandTotalYellowFiles = 0;
        int grandTotalNonYellowFiles = 0;

        // -------------------------------------------------------
        // STEP 2: Loop over each today folder.
        // -------------------------------------------------------

        for (int i = 0; i < todayFolders.size(); i++) {

            String folderName = todayFolders.get(i);

            System.out
                    .println("[MASUDA] Processing folder [" + (i + 1) + "/" + todayFolders.size() + "]: " + folderName);
            Allure.step("Processing folder: " + folderName);

            // ---------------------------------------------------
            // STEP 2a: Double-click the folder.
            // ---------------------------------------------------

            doubleClickTodayFolder(folderName);

            // ---------------------------------------------------
            // STEP 2b: Get API response inside the folder,
            // count today's total / yellow / non-yellow files.
            // ---------------------------------------------------

            String folderJson = getSocketFiles();

            FileCountResult result = api.countFilesInFolder(
                    folderJson,
                    null,
                    folderName);

            grandTotalFiles += result.getTotalFileCount();
            grandTotalYellowFiles += result.getAttributeFileCount();
            grandTotalNonYellowFiles += result.getNonAttributeFileCount();

            // ---------------------------------------------------
            // STEP 2c: Click Home to return to S09 folder list
            // (only if more folders remain).
            // ---------------------------------------------------

            if (i < todayFolders.size() - 1) {
                clickHome();
            }
        }

        // -------------------------------------------------------
        // STEP 3: Print Grand Totals & Add Single Report Entry
        // -------------------------------------------------------

        System.out.println();
        System.out.println("========================================");
        System.out.println("     MASUDA - ALL TODAY FOLDERS TOTAL");
        System.out.println("========================================");
        System.out.println("Today's Total File Count     : " + grandTotalFiles);
        System.out.println("Today's Yellow Icon Files    : " + grandTotalYellowFiles);
        System.out.println("Today's No Yellow Icon Files : " + grandTotalNonYellowFiles);
        System.out.println("========================================");

        Allure.step("Grand Total Today Files: " + grandTotalFiles);
        Allure.step("Grand Total Yellow Files: " + grandTotalYellowFiles);
        Allure.step("Grand Total Non-Yellow Files: " + grandTotalNonYellowFiles);

        TestExecutionReport.addResult(
                "MASUDA",
                "Today",
                grandTotalFiles,
                grandTotalYellowFiles,
                grandTotalNonYellowFiles,
                folderScreenshotPath);

        System.out.println("[MASUDA] All today's folders processed.");
        Allure.step("[MASUDA] All today's folders processed.");
    }

    // ============================================================
    // DOUBLE-CLICK A TODAY FOLDER BY ITS TITLE
    // ============================================================

    /**
     * Finds the folder whose label/title exactly matches folderName
     * and double-clicks it.
     */
    private void doubleClickTodayFolder(String folderName) {

        Allure.step("Double-clicking folder: " + folderName);

        // Locate the small image-div (folder icon) preceding the label with this title.
        Locator folderIcon = page.locator(
                "//label[@title='" + folderName + "']/preceding::div[contains(@class,'imageDivSmall')][1]");

        folderIcon.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        folderIcon.scrollIntoViewIfNeeded();

        folderIcon.dblclick();

        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("[MASUDA] Double-clicked folder: " + folderName);
        Allure.step("Folder double-clicked: " + folderName);
    }

    // ============================================================
    // CLICK HOME ICON (Returns to S09 folder list)
    // ============================================================

    public void clickHome() {

        page.waitForTimeout(2000);

        page.locator("//span[@class='fa fa-home']").click();

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        System.out.println("[MASUDA] Home icon clicked - returned to S09 folder list.");
        Allure.step("Home icon clicked - returned to S09 folder list.");
    }
}
