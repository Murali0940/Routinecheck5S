package fivesservice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import base.BaseDriver;
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

        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);

        if (currentURL.contains("home")) {
            System.out.println("Home page URL is correct.");
        } else {
            System.out.println("Home page URL is incorrect.");
        }
    }

    public void hyperLinkPageURL() {

        page.waitForTimeout(1000);

        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);

        if (currentURL.contains("adsocket")) {
            System.out.println("adsocket page URL is correct.");
        } else {
            System.out.println("adsocket page URL is incorrect.");
        }
    }

    public void drawSocketURL() {

        page.waitForTimeout(1000);
        page.waitForLoadState();
        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);

        if (currentURL.contains("draw")) {
            System.out.println("drawsocket page URL is correct..");
        } else {
            System.out.println("drawsocket page URL is incorrect.");
        }
    }

    public void setPagination(String expectedValue) {

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

        page.waitForLoadState();

        page.waitForTimeout(4000);
    }

    public Response getSocketFiles() {

        Response response = page.waitForResponse(
                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,
                () -> {

                    // Trigger the API
                    page.reload();

                });
        return response;

    }

    // public void verifyTodayFiles() {

    // Locator todayFiles = page.locator("//ad-grid-item");

    // int totalFiles = todayFiles.count();

    // int withYellowIconCount = 0;
    // int withoutYellowIconCount = 0;

    // System.out.println("=================================================");
    // System.out.println("Today's File Analysis (assets/attribute_office.png)");
    // System.out.println("=================================================");

    // for (int i = 0; i < totalFiles; i++) {
    // Locator item = todayFiles.nth(i);

    // // Check if this item has the yellow office icon
    // Locator yellowIcon = item.locator("img[src='assets/attribute_office.png']");
    // boolean hasYellowIcon = yellowIcon.count() > 0;

    // // Get the filename text from the grid item
    // String fileName = item.locator(".ad-grid-filename, [class*='filename'],
    // [class*='file-name'], span")
    // .first()
    // .textContent()
    // .trim();

    // if (hasYellowIcon) {
    // withYellowIconCount++;
    // } else {
    // withoutYellowIconCount++;
    // System.out.println("[NO YELLOW ICON] File " + (i + 1) + " : " + fileName);

    // // Capture a screenshot for each file missing the yellow icon
    // page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
    // .setPath(java.nio.file.Paths.get("screenshots/no_yellow_icon_file_" + (i + 1)
    // + ".png")));
    // }
    // }

    // System.out.println("=================================================");
    // System.out.println("Today's Total Files : " + totalFiles);
    // System.out.println("Files WITH Yellow Icon : " + withYellowIconCount);
    // System.out.println("Files WITHOUT Yellow Icon : " + withoutYellowIconCount);
    // System.out.println("=================================================");
    // }

    // Actions

    public void clickHyperLinkIcon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        hyperLinkIcon.click();
    }

    public void clickDrawSocketIcon() {
        page.waitForLoadState();
        page.waitForTimeout(1000);
        drawsocketicon.click();
    }

    // validations

    public void validateHyperLinkIcon() {
        page.waitForTimeout(1000);
        if (hyperLinkIcon.isVisible()) {
            System.out.println("HyperLink icon is displayed.");
        } else {
            System.out.println("HyperLink icon is not displayed.");
        }
    }

    public void validateDrawSocketIcon() {
        page.waitForTimeout(1000);
        if (drawsocketicon.isVisible()) {
            System.out.println("Draw Socket icon is displayed.");
        } else {
            System.out.println("Draw Socket icon is not displayed.");
        }
    }

    public void verifyTodayFileCount() {

        Response response = getSocketFiles();

        System.out.println("======================================");
        System.out.println("GET SOCKET FILES API RESPONSE");
        System.out.println("======================================");
        System.out.println(response.text());
        System.out.println("======================================");
        APIFileUtil api = new APIFileUtil();
        // Response response = getSocketFiles();
        api.getFilesByDay(response.text(), "Today", "OAKSYSTEM");
        BaseDriver.takeScreenshot(page, "todayFiles");
        // page.reload();
        // page.waitForTimeout(1000);
        // Response response1 = getSocketFiles();
        // api.getFilesByDay(response1.text(), "Yesterday");
    }

}
