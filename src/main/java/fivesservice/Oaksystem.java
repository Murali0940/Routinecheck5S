package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.APIFileUtil;

public class Oaksystem {

    private Page page;

    // locators

    private final Locator hyperLinkIcon;

    private final Locator drawsocketicon;

    public Oaksystem(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.drawsocketicon = page.locator(
                "//h4[text()='図面']/preceding::input[@src='assets/icons/Drawing.png']");
    }

    // methods

    public void homePageURL() {

        page.waitForTimeout(1000);

        Allure.step("Navigate to home page");

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

    public void hyperLinkPageURL() {

        page.waitForTimeout(1000);
        Allure.step("Navigate to hyperLinkPageURL");

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
        Allure.step("Navigate to drawSocketURL");
        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);
        Allure.step("Current URL: " + currentURL);

        if (currentURL.contains("draw")) {
            System.out.println("drawsocket page URL is correct..");
            Allure.step("drawsocket page URL is correct.");
        } else {
            System.out.println("drawsocket page URL is incorrect.");
            Allure.step("drawsocket page URL is incorrect.");
        }
    }

    public void setPagination(String expectedValue) {

        Allure.step("Setting pagination to " + expectedValue);

        Locator selectedValue = page.locator(
                "(//label[contains(@class,'ui-dropdown-label')])[1]");

        // Wait until the pagination dropdown is available
        selectedValue.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        String currentValue = selectedValue.textContent().trim();

        // Already set
        if (currentValue.equals(expectedValue)) {

            System.out.println(
                    "Pagination already set to " + expectedValue);

            Allure.step(
                    "Pagination already set to " + expectedValue);

            return;
        }

        // Open pagination dropdown
        Locator dropdownTrigger = page.locator(
                "(//span[contains(@class,'ui-dropdown-trigger-icon')])[1]");

        dropdownTrigger.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        dropdownTrigger.click();

        // Select requested pagination value
        Locator paginationOption = page.locator(
                "//span[text()='" + expectedValue + "']");

        paginationOption.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        paginationOption.click();

        // Wait until dropdown actually changes
        page.waitForCondition(
                () -> selectedValue.textContent()
                        .trim()
                        .equals(expectedValue));

        System.out.println(
                "Pagination changed to " + expectedValue);

        Allure.step(
                "Pagination changed to " + expectedValue);

        // Wait for the page data to settle
        page.waitForLoadState();

        Allure.step("Pagination data loaded");
    }

    public Response getSocketFiles() {

        Allure.step("Waiting for Get Socket Files API response");

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                () -> {

                    Allure.step("Triggering Get Socket Files API");

                    page.reload(
                            new Page.ReloadOptions()
                                    .setWaitUntil(
                                            WaitUntilState.DOMCONTENTLOADED)
                                    .setTimeout(60000));
                });

        Allure.step("Get Socket Files API response received");

        return response;
    }

    // Actions

    public void clickHyperLinkIcon() {

        Allure.step("Clicking HyperLink icon");

        hyperLinkIcon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        hyperLinkIcon.scrollIntoViewIfNeeded();

        if (!hyperLinkIcon.isEnabled()) {
            throw new AssertionError(
                    "HyperLink icon is visible but not enabled.");
        }

        hyperLinkIcon.click();

        Allure.step("HyperLink icon clicked successfully");
    }

    public void clickDrawSocketIcon() {

        Allure.step("Clicking Draw Socket icon");

        drawsocketicon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        drawsocketicon.scrollIntoViewIfNeeded();

        if (!drawsocketicon.isEnabled()) {
            throw new AssertionError(
                    "Draw Socket icon is visible but not enabled.");
        }

        drawsocketicon.click();

        Allure.step("Draw Socket icon clicked successfully");

        // Wait for the navigation triggered by the click
        page.waitForLoadState();
    }

    // validations

    public void validateHyperLinkIcon() {

        Allure.step("Validating HyperLink icon");

        hyperLinkIcon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        if (hyperLinkIcon.isVisible()) {

            System.out.println("HyperLink icon is displayed.");
            Allure.step("HyperLink icon is displayed.");

        } else {

            System.out.println("HyperLink icon is not displayed.");
            Allure.step("HyperLink icon is not displayed.");

            throw new AssertionError(
                    "HyperLink icon is not displayed.");
        }
    }

    public void validateDrawSocketIcon() {

        Allure.step("Validating Draw Socket icon");

        drawsocketicon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        System.out.println("Draw Socket icon is displayed.");
        Allure.step("Draw Socket icon is displayed.");
    }

    public void verifyTodayFileCount() {

        Allure.step("Verifying today's file count for OAKSYSTEM");

        Response response = getSocketFiles();

        APIFileUtil api = new APIFileUtil();

        api.getFilesByDay(
                response.text(),
                "Today",
                "OAKSYSTEM");

        BaseDriver.takeScreenshot(
                page,
                "todayFiles");
    }

}
