package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.WaitForSelectorState;

import base.BaseDriver;
import io.qameta.allure.Allure;
import utils.APIFileUtil;

public class Nisinoseikiss {

    protected Page page;

    // locators

    private final Locator hyperLinkIcon;

    private final Locator scansocketicon;

    private final Locator homeicon;

    public Nisinoseikiss(Page page) {
        this.page = page;
        this.hyperLinkIcon = page.locator(
                "//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png']");
        this.scansocketicon = page.locator(
                "//h4[text()='図面']/preceding::input[@src='assets/icons/Scan.png']");
        this.homeicon = page.locator("//span[@class='fa fa-home']");
    }

    // methods

    public void homePageURL() {

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

    public void scanSocketURL() {

        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);
        Allure.step("Current URL: " + currentURL);

        if (currentURL.contains("scan")) {
            System.out.println("scansocket page URL is correct..");
            Allure.step("scansocket page URL is correct..");
        } else {
            System.out.println("scansocket page URL is incorrect.");
            Allure.step("scansocket page URL is incorrect.");
        }
    }

    public void setPagination(String expectedValue) {

        Locator selectedValue = page.locator("(//label[contains(@class,'ui-dropdown-label')])[1]");

        String currentValue = selectedValue.textContent().trim();

        if (currentValue.equals(expectedValue)) {
            System.out.println("Pagination already set to " + expectedValue);
            Allure.step("Pagination already set to " + expectedValue);
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

    // Actions

    public void clickHyperLinkIcon() {
        hyperLinkIcon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));
        hyperLinkIcon.click();
        page.waitForTimeout(2000);
        page.waitForLoadState();
        Allure.step("HyperLink icon clicked");

    }

    public void clickScanSocketIcon() {
        scansocketicon.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));
        scansocketicon.click();
        page.waitForTimeout(2000);
        page.waitForLoadState();
        Allure.step("Scan Socket icon clicked");

    }

    public void clickBeforeWorkFolder() {
        page.waitForTimeout(2000);
        page.waitForLoadState();
        Locator beforeWorkFolder = page
                .locator("//label[@title='作業前']/preceding::div[contains(@class,'imageDivSmall')][1]");
        beforeWorkFolder.scrollIntoViewIfNeeded();
        beforeWorkFolder.dblclick();
        Allure.step("Before Work folder double clicked");
        System.out.println("clicked 作業前 folder");
    }

    // click homeicon
    public void clickHome() {
        page.waitForTimeout(2000);
        homeicon.click();
        page.waitForLoadState();
        Allure.step("Home icon clicked");
    }

    public void clickcompletedfolder() {
        page.waitForTimeout(2000);
        page.waitForLoadState();
        Locator completedFolder = page
                .locator("//label[@title='完了']/preceding::div[contains(@class,'imageDivSmall')][1]");
        completedFolder.scrollIntoViewIfNeeded();
        completedFolder.dblclick();
        Allure.step("Completed folder double clicked");
        System.out.println("clicked 完了 folder");
    }

    // validations

    public void validateHyperLinkIcon() {
        page.waitForTimeout(1000);
        if (hyperLinkIcon.isVisible()) {
            System.out.println("HyperLink icon is displayed.");
            Allure.step("HyperLink icon is displayed.");
        } else {
            System.out.println("HyperLink icon is not displayed.");
            Allure.step("HyperLink icon is not displayed.");
        }
    }

    public void validateScanSocketIcon() {
        if (scansocketicon.isVisible()) {
            System.out.println("Scan Socket icon is displayed.");
            Allure.step("Scan Socket icon is displayed.");
        } else {
            System.out.println("Scan Socket icon is not displayed.");
            Allure.step("Scan Socket icon is not displayed.");
        }
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
        api.getFilesByDay(response.text(), "Today", "NISINOSEIKISS");
        BaseDriver.takeScreenshot(page, "todayFiles");
    }

    public Response getSocketFiles() {
        Allure.step("Waiting for Get Socket Files API response");

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                () -> {

                    // Trigger the API
                    page.reload();

                });
        Allure.step("Get Socket Files API response");
        return response;

    }

}
