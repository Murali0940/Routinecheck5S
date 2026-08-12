package pages;

import com.microsoft.playwright.*;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

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

        page.getByPlaceholder("Username").fill(user);
        Allure.step("User username is: " + user);
        logger.info("User username entered" + user);
        System.out.println("username is: " + user);
        page.waitForTimeout(1000);

        page.getByPlaceholder("Password").fill(pass);
        Allure.step("User password entered");
        logger.info("User password entered");
        System.out.println("User password entered");
        page.waitForTimeout(1000);

        btnLogin1.click();
        Allure.step("User login button clicked.");
        logger.info("User login button clicked.");
        System.out.println("Login button clicked.");
        page.waitForTimeout(1000);

        return new Homepage(page);

    }

    public void userLogout() {
        btnLogout1.click();
    }

    public void verifyPageURL() {

        String currentURL = page.url();

        System.out.println(
                "URL after User Login: " + currentURL);
    }

}