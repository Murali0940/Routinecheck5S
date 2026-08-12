package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompLoginPage {

    private Page page;
    private static final Logger logger = LogManager.getLogger(CompLoginPage.class);

    private Locator txtUsername;
    private Locator txtPassword;
    private Locator btnLogin;

    public CompLoginPage(Page page) {

        this.page = page;

        txtUsername = page.locator("#username");

        txtPassword = page.locator("#password");

        btnLogin = page.locator("#logmein");

    }

    @Step("Login to company application with username: {user}")
    public UserLoginPage companyLogin(String user, String pass) {

        Allure.step("Starting company login");

        // Username
        txtUsername.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        txtUsername.fill(user);

        System.out.println("Company username entered: " + user);
        Allure.step("Company username entered");
        logger.info("Company username entered: {}", user);

        // Password
        txtPassword.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        txtPassword.fill(pass);

        System.out.println("Company password entered");
        Allure.step("Company password entered");
        logger.info("Company password entered");

        // Login button
        btnLogin.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        if (!btnLogin.isEnabled()) {
            throw new AssertionError(
                    "Company login button is visible but not enabled.");
        }

        btnLogin.click();

        System.out.println("Company login button clicked.");
        Allure.step("Company login button clicked.");
        logger.info("Company login button clicked.");

        // Wait for the next page to become available
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForTimeout(3000);

        return new UserLoginPage(page);
    }

}