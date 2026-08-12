package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;

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

        Allure.step("setPagination");

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
        Allure.step("Loading data");
        page.waitForTimeout(4000);
    }

    public Response getSocketFiles() {

        Allure.step("getSocketFiles");

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                () -> {

                    // Trigger the API
                    page.reload();
                    Allure.step("API triggered");

                });
        return response;

    }

    // Actions

    public void clickHyperLinkIcon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
        Allure.step("HyperLink icon clicked");
    }

    public void clickDrawSocketIcon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        drawsocketicon.click();
        Allure.step("Draw Socket icon clicked");
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

    // validations

    public void validateHyperLinkIcon() {
        page.waitForTimeout(2000);
        Allure.step("validateHyperLinkIcon");
        if (hyperLinkIcon.isVisible()) {
            System.out.println("HyperLink icon is displayed.");
            Allure.step("HyperLink icon is displayed.");
        } else {
            System.out.println("HyperLink icon is not displayed.");
            Allure.step("HyperLink icon is not displayed.");
        }
    }

    public void validateDrawSocketIcon() {
        page.waitForTimeout(2000);
        Allure.step("validateDrawSocketIcon");
        if (drawsocketicon.isVisible()) {
            System.out.println("Draw Socket icon is displayed.");
            Allure.step("Draw Socket icon is displayed.");
        } else {
            System.out.println("Draw Socket icon is not displayed.");
            Allure.step("Draw Socket icon is not displayed.");
        }
    }

    public void verifyTodayFileCount() {

        Allure.step("verifyTodayFileCount");
        page.reload();
        page.waitForTimeout(2000);
        Response response = getSocketFiles();

        // System.out.println("======================================");
        // Allure.step("GET SOCKET FILES API RESPONSE");
        // System.out.println("======================================");
        // System.out.println(response.text());
        // Allure.step("GET SOCKET FILES API RESPONSE " + response.text());
        System.out.println("======================================");
        APIFileUtil api = new APIFileUtil();
        api.getFilesByDay(response.text(), "Today", "OAKSYSTEM");
        BaseDriver.takeScreenshot(page, "todayFiles");
    }

}
