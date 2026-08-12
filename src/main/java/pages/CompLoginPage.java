package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

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

        txtUsername.fill(user);
        System.out.println("Company username is: " + user);
        Allure.step("Company username is: " + user);
        logger.info("Company username is: " + user);
        page.waitForTimeout(1000);

        txtPassword.fill(pass);
        System.out.println("Company password is: " + pass);
        Allure.step("Company password is: " + pass);
        logger.info("Company password is: " + pass);

        btnLogin.click();
        System.out.println("Company login button clicked.");
        Allure.step("Company login button clicked.");
        logger.info("Company login button clicked.");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        return new UserLoginPage(page);

    }

}