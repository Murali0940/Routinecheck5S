package pages;

import com.microsoft.playwright.*;

import io.qameta.allure.Allure;
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

    public UserLoginPage companyLogin(String user, String pass) {

        txtUsername.fill(user);
        System.out.println("Company username is: " + user);
        Allure.step("Company username is: " + user);
        logger.info("Company username is: " + user);

        txtPassword.fill(pass);
        System.out.println("Company password is: " + pass);
        Allure.step("Company password is: " + pass);
        logger.info("Company password is: " + pass);

        btnLogin.click();
        System.out.println("Company login button clicked.");
        Allure.step("Company login button clicked.");
        logger.info("Company login button clicked.");

        return new UserLoginPage(page);

    }

}