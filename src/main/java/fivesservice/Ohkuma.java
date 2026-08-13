package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.OhkumaAPIFileUtil;

public class Ohkuma {

    private Page page;

    // locators

    private final Locator hyperLinkIcon;
    private final Locator adSocketS09Icon;

    // methods

    public Ohkuma(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.adSocketS09Icon = page.locator(
                "(//input[@id='image' and @src='assets/transparent.png'])[3]");

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

        if (currentURL.contains("s09")) {
            System.out.println("s09 page URL is correct..");
            Allure.step("s09 page URL is correct..");
        } else {
            System.out.println("s09 page URL is incorrect.");
            Allure.step("s09 page URL is incorrect.");
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
                new Page.WaitForResponseOptions()
                        .setTimeout(60000),
                () -> {
                    page.reload();
                });

        String responseBody = response.text();

        Allure.step(
                "Get Socket Files API response received successfully");

        return responseBody;
    }

    public void verifyTodayFileCountAndGetScreenshot() {

        page.waitForLoadState(LoadState.NETWORKIDLE);

        String responseBody = getSocketFiles();

        Allure.step("Get Socket Files API response");

        String screenshotPath = BaseDriver.takeScreenshot(
                page,
                "OHKUMA_todayFiles");

        OhkumaAPIFileUtil api = new OhkumaAPIFileUtil();

        api.ohkumaGetTodayFiles(responseBody, screenshotPath);

        // Add OHKUMA report here if your API utility
        // returns/stores the required counts.
    }

    // Actions

    public void clickHyperLinkIcon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
        Allure.step("HyperLink icon clicked");
    }

    public void clickS09SocketIcon() {
        page.waitForLoadState();
        page.waitForTimeout(2000);
        adSocketS09Icon.click();
        Allure.step("S09 Socket icon clicked");
    }

    // Validations

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

    public void validateS09SocketIcon() {
        page.waitForTimeout(2000);
        if (adSocketS09Icon.isVisible()) {
            System.out.println("S09 Socket icon is displayed.");
            Allure.step("S09 Socket icon is displayed.");
        } else {
            System.out.println("S09 Socket icon is not displayed.");
            Allure.step("S09 Socket icon is not displayed.");
        }
    }

}
