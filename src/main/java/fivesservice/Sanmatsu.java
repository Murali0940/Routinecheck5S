package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.APIFileUtil;

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
        Allure.step("HyperLink icon clicked");

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

    public void verifyTodayFileCountAndGetScreenshot() {

        Response response = getSocketFiles();
        Allure.step("Get Socket Files API response");

        // System.out.println("======================================");
        // System.out.println("GET SOCKET FILES API RESPONSE");
        // System.out.println("======================================");
        // System.out.println(response.text());
        // System.out.println("======================================");

        APIFileUtil api = new APIFileUtil();
        api.getFilesByDay(response.text(), "Today", "SANMATSU");
        BaseDriver.takeScreenshot(page, "todayFiles");
    }

}
