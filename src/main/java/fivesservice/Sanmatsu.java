package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.APIFileUtil;
import utils.FileCountResult;
import utils.TestExecutionReport;

public class Sanmatsu {

    private Page page;

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

    }

    // actions

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

    public void homePageURL() {

        page.waitForTimeout(1000);

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

    public void validatehyperlinkicon() {

        hyperLinkIcon.isVisible();

    }

    public void validatedrawsocketicon() {

        drawSocketIcon.isVisible();

    }

    public void hyperLinkPageURL() {

        page.waitForTimeout(1000);

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

        page.waitForTimeout(1000);
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

    public String getSocketFiles() {

        Allure.step("Waiting for Get Socket Files API response");

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                page::reload);

        String responseBody = response.text();

        if (responseBody == null || responseBody.isBlank()) {
            throw new RuntimeException(
                    "Get Socket Files API returned an empty response");
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode files = mapper.readTree(responseBody);

            if (!files.isArray()) {
                throw new RuntimeException(
                        "Get Socket Files API response is not a JSON array");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid Get Socket Files API response",
                    e);
        }

        Allure.step("Get Socket Files API response received successfully");

        return responseBody;
    }

    public void verifyTodayFileCountAndGetScreenshot() {

        page.waitForLoadState(LoadState.NETWORKIDLE);

        String responseBody = getSocketFiles();

        Allure.step("Get Socket Files API response");

        APIFileUtil apiFileUtil = new APIFileUtil();

        FileCountResult result = apiFileUtil.getTodayFileCount(
                responseBody,
                "SANMATSU");

        System.out.println("Total : " + result.getTotalFileCount());
        System.out.println("Attribute : " + result.getAttributeFileCount());
        System.out.println("Non-Attribute : " + result.getNonAttributeFileCount());

        String screenshotPath = BaseDriver.takeScreenshot(
                page,
                "SANMATSU_todayFiles");

        TestExecutionReport.addResult(
                "SANMATSU",
                "Today",
                result.getTotalFileCount(),
                result.getAttributeFileCount(),
                result.getNonAttributeFileCount(),
                screenshotPath);
    }

}
