package fivesservice;

import java.io.ByteArrayInputStream;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;

public class AttributeProcessPage {

    private final Page page;

    // ============================================================
    // LOCATORS
    // ============================================================

    private final Locator hyperLinkIcon;

    private final Locator drawSocketIcon;

    private final Locator selectedPagination;

    private final Locator paginationDropdown;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public AttributeProcessPage(Page page) {

        this.page = page;

        this.hyperLinkIcon = page.locator(
                "//h4[normalize-space()='HyperLink']" + "/preceding::input[@src='assets/icons/hyperLink.png'][1]");

        this.drawSocketIcon = page.locator("//h4[text()='Drawing Manager']/preceding::input[@src='assets/icons/Drawing.png']");

        this.selectedPagination = page.locator("(//label[contains(@class,'ui-dropdown-label')])[1]");

        this.paginationDropdown = page.locator("(//span[contains(@class,'ui-dropdown-trigger-icon')])[1]");
    }

    // ============================================================
    // HYPERLINK
    // ============================================================

    public void clickHyperLinkIcon() {

        Allure.step("Clicking HyperLink icon");

        waitForPageToBeReady();

        waitUntilVisible(hyperLinkIcon, "HyperLink icon");

        validateEnabled(hyperLinkIcon, "HyperLink icon");

        System.out.println("HyperLink locator count: " + hyperLinkIcon.count());

        hyperLinkIcon.scrollIntoViewIfNeeded();

        hyperLinkIcon.click();

        System.out.println("HyperLink icon clicked successfully");

        Allure.step("HyperLink icon clicked successfully");

        attachScreenshot("HyperLink icon clicked");
    }

    // ============================================================
    // DRAW SOCKET
    // ============================================================

    public void clickDrawSocketIcon() {

        Allure.step("Clicking Draw Socket icon");

        waitForPageToBeReady();

        waitUntilVisible(drawSocketIcon, "Draw Socket icon");

        validateEnabled(drawSocketIcon, "Draw Socket icon");

        drawSocketIcon.scrollIntoViewIfNeeded();

        drawSocketIcon.click();

        System.out.println("Draw Socket icon clicked successfully");

        Allure.step("Draw Socket icon clicked successfully");

        attachScreenshot("Draw Socket icon clicked");
    }

    // ============================================================
    // HOME PAGE URL VALIDATION
    // ============================================================

    public void verifyHomePageURL() {

        waitForPageToBeReady();

        String currentURL = page.url();

        System.out.println("Current URL: " + currentURL);

        Allure.step("Current URL: " + currentURL);

        if (!currentURL.contains("home")) {

            Allure.step("Home page URL validation failed: " + currentURL);

            throw new AssertionError(
                    "Expected Home page URL to contain 'home', " + "but actual URL was: " + currentURL);
        }

        System.out.println("Home page URL is correct.");

        Allure.step("Home page URL is correct.");

    }

    // ============================================================
    // HYPERLINK ICON VALIDATION
    // ============================================================

    public void validateHyperLinkIcon() {

        Allure.step("Validating HyperLink icon");

        waitUntilVisible(hyperLinkIcon, "HyperLink icon");

        boolean enabled = hyperLinkIcon.isEnabled();

        Allure.step("HyperLink icon visible and enabled: " + enabled);

        if (!enabled) {

            throw new AssertionError("HyperLink icon is visible but not enabled.");
        }

        System.out.println(
                "HyperLink icon is visible and enabled.");

    }

    // ============================================================
    // DRAW SOCKET ICON VALIDATION
    // ============================================================

    public void validateDrawSocketIcon() {

        Allure.step("Validating Draw Socket icon");

        waitUntilVisible(drawSocketIcon, "Draw Socket icon");

        boolean enabled = drawSocketIcon.isEnabled();

        Allure.step("Draw Socket icon visible and enabled: " + enabled);

        if (!enabled) {

            throw new AssertionError("Draw Socket icon is visible but not enabled.");
        }

        System.out.println("Draw Socket icon is visible and enabled.");

    }

    // ============================================================
    // HYPERLINK PAGE URL
    // ============================================================

    public void verifyHyperLinkPageURL() {

        waitForPageToBeReady();

        String currentURL = page.url();

        System.out.println("Current URL: " + currentURL);

        Allure.step("Current URL: " + currentURL);

        if (!currentURL.contains("adsocket")) {

            Allure.step("HyperLink page URL validation failed: " + currentURL);

            throw new AssertionError("Expected HyperLink page URL to contain 'adsocket', but actual URL was: "
                    + currentURL);
        }

        System.out.println("HyperLink page URL is correct.");

        Allure.step("HyperLink page URL is correct.");

    }

    // ============================================================
    // DRAW SOCKET PAGE URL
    // ============================================================

    public void verifyDrawSocketURL() {

        waitForPageToBeReady();

        String currentURL = page.url();

        System.out.println("Current URL: " + currentURL);

        Allure.step("Current URL: " + currentURL);

        if (!currentURL.contains("draw")) {

            Allure.step("Draw Socket page URL validation failed: " + currentURL);

            throw new AssertionError("Expected Draw Socket page URL to contain 'draw', but actual URL was: "
                    + currentURL);
        }

        System.out.println("Draw Socket page URL is correct.");

        Allure.step("Draw Socket page URL is correct.");

    }

    // ============================================================
    // PAGINATION
    // ============================================================

    public void setPagination(String expectedValue) {

        Allure.step("Setting pagination to " + expectedValue);

        waitForPageToBeReady();

        waitUntilVisible(selectedPagination, "Selected pagination");

        String currentValue = selectedPagination.textContent().trim();

        System.out.println("Current pagination: " + currentValue);

        // --------------------------------------------------------
        // Already set
        // --------------------------------------------------------

        if (currentValue.equals(expectedValue)) {

            System.out.println("Pagination already set to " + expectedValue);

            Allure.step("Pagination already set to " + expectedValue);

            return;
        }

        // --------------------------------------------------------
        // Open pagination dropdown
        // --------------------------------------------------------

        waitUntilVisible(paginationDropdown, "Pagination dropdown");

        validateEnabled(paginationDropdown, "Pagination dropdown");

        paginationDropdown.click();

        // --------------------------------------------------------
        // Locate requested option
        // --------------------------------------------------------

        Locator paginationOption = page.locator(
                "//span[normalize-space()='"
                        + expectedValue
                        + "']");

        waitUntilVisible(
                paginationOption,
                "Pagination option "
                        + expectedValue);

        validateEnabled(
                paginationOption,
                "Pagination option "
                        + expectedValue);

        paginationOption.click();

        // --------------------------------------------------------
        // Verify pagination changed
        // --------------------------------------------------------

        page.waitForCondition(
                () -> selectedPagination
                        .textContent()
                        .trim()
                        .equals(expectedValue));

        System.out.println(
                "Pagination changed to "
                        + expectedValue);

        Allure.step(
                "Pagination changed to "
                        + expectedValue);

        attachScreenshot(
                "Pagination set to "
                        + expectedValue);

        waitForPageToBeReady();
    }

    // ============================================================
    // COMMON WAIT
    // ============================================================

    private void waitForPageToBeReady() {

        try {

            page.waitForLoadState(
                    LoadState.DOMCONTENTLOADED);

        } catch (Exception e) {

            System.out.println(
                    "Page load state wait completed with: "
                            + e.getMessage());
        }
    }

    // ============================================================
    // WAIT FOR VISIBLE
    // ============================================================

    private void waitUntilVisible(
            Locator locator,
            String elementName) {

        try {

            locator.waitFor(
                    new Locator.WaitForOptions()
                            .setState(
                                    WaitForSelectorState.VISIBLE));

        } catch (Exception e) {

            throw new AssertionError(
                    elementName
                            + " was not visible. "
                            + "Locator: "
                            + locator,
                    e);
        }
    }

    // ============================================================
    // ENABLED VALIDATION
    // ============================================================

    private void validateEnabled(
            Locator locator,
            String elementName) {

        if (!locator.isEnabled()) {

            throw new AssertionError(
                    elementName
                            + " is visible but not enabled.");
        }
    }

    // ============================================================
    // ALLURE SCREENSHOT
    // ============================================================

    private void attachScreenshot(
            String screenshotName) {

        try {

            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setFullPage(true));

            Allure.addAttachment(
                    screenshotName,
                    "image/png",
                    new ByteArrayInputStream(
                            screenshot),
                    ".png");

            System.out.println(
                    "Screenshot attached to Allure: "
                            + screenshotName);

        } catch (Exception e) {

            System.out.println(
                    "Failed to attach screenshot to Allure: "
                            + e.getMessage());
        }
    }
}