package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.AllureScreenshotUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserLoginPage {

    private Page page;

    private static final Logger logger = LogManager.getLogger(UserLoginPage.class);

    private Locator btnLogin1;

    private Locator btnLogout1;

    public UserLoginPage(Page page) {

        this.page = page;
        this.btnLogin1 = page.locator("#login");
        this.btnLogout1 = page.locator("#logout");

    }

    @Step("Login to user login page with username: {user}")
    public Homepage userLogin(String user, String pass) {

        Allure.step("Starting user login");

        AllureScreenshotUtil.allureAttachScreenshot(page, "user login page");

        // Wait for username field
        page.getByPlaceholder("Username").waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        page.getByPlaceholder("Username").fill(user);

        Allure.step("User username entered");
        logger.info("User username entered: {}", user);
        System.out.println("Username entered: " + user);

        // Enter password
        page.getByPlaceholder("Password").waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        page.getByPlaceholder("Password").fill(pass);

        Allure.step("User password entered");
        logger.info("User password entered");
        System.out.println("User password entered");

        // Wait for login button
        btnLogin1.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        if (!btnLogin1.isEnabled()) {
            throw new AssertionError(
                    "User login button is visible but not enabled.");
        }

        AllureScreenshotUtil.allureAttachScreenshot(page, "user login page");

        btnLogin1.click();

        Allure.step("User login button clicked");
        logger.info("User login button clicked");
        System.out.println("Login button clicked");

        return new Homepage(page);
    }

    public void userLogout() {
        btnLogout1.click();
        page.waitForTimeout(3000);
    }

    public void verifyPageURL() {

        String currentURL = page.url();

        System.out.println("URL after User Login: " + currentURL);

        Allure.step("URL after User Login: " + currentURL);
    }

}