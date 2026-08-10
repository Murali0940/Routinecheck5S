package pages;

import com.microsoft.playwright.*;

import io.qameta.allure.Allure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserLoginPage {

    private Page page;
    private static final Logger logger = LogManager.getLogger(UserLoginPage.class);

    private Locator txtUsername1;

    private Locator txtPassword1;

    private Locator btnLogin1;

    private Locator btnLogout1;

    public UserLoginPage(Page page) {

        this.page = page;

        txtUsername1 = page.locator("#username");

        txtPassword1 = page.locator("#password");

        btnLogin1 = page.locator("#login");

        btnLogout1 = page.locator("#logout");

    }

    public Homepage userLogin(String user, String pass) {

        txtUsername1.fill(user);
        Allure.step("User username is: " + user);
        logger.info("User username is: " + user);
        page.waitForTimeout(1000);

        txtPassword1.fill(pass);
        Allure.step("User password is: " + pass);
        logger.info("User password is: " + pass);

        btnLogin1.click();
        Allure.step("User login button clicked.");
        logger.info("User login button clicked.");
        page.waitForTimeout(1000);

        return new Homepage(page);

    }

    public void userLogout() {
        btnLogout1.click();
    }

    public void verifyPageURL() {

        page.waitForLoadState();

        String currentURL = page.url();
        System.out.println("Current URL: " + currentURL);

        if (currentURL.contains("userlogin")) {
            System.out.println("userlogin page URL is correct.");
        } else {
            System.out.println("userlogin page URL is incorrect.");
        }
    }

}