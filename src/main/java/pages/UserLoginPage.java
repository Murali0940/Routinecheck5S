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
        logger.info("User username is: " + user);
        page.waitForTimeout(1000);

        page.getByPlaceholder("Password").fill(pass);
        Allure.step("User password is: " + pass);
        logger.info("User password is: " + pass);
        page.waitForTimeout(1000);

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

        page.waitForCondition(
                () -> page.url().contains("userlogin"),
                new Page.WaitForConditionOptions()
                        .setTimeout(30000));

        String currentURL = page.url();

        System.out.println(
                "URL after User Login: " + currentURL);
    }

}