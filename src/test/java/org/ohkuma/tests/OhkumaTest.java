package org.ohkuma.tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import fivesservice.Ohkuma;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;
import base.BaseDriver;

public class OhkumaTest extends BaseDriver {

    Ohkuma ohkuma;

    @Test(priority = 1, description = "Verify login with valid Company and User credentials")
    public void validLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("ohkuma.compusername"), ConfigReader.get("ohkuma.comppassword"));

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.userLogin(ConfigReader.get("user.username"), ConfigReader.get("user.password"));

        userPage.verifyPageURL();

    }

    @Test(priority = 2, description = "validatingS09Socket")

    public void validatingS09Socket() {

        ohkuma = new Ohkuma(page);
        ohkuma.homePageURL();
        ohkuma.validateHyperLinkIcon();
        ohkuma.clickHyperLinkIcon();
        ohkuma.hyperLinkPageURL();
        ohkuma.validateS09SocketIcon();
        ohkuma.clickS09SocketIcon();
        ohkuma.drawSocketURL();
        ohkuma.setPagination("100");
        ohkuma.verifyTodayFileCount();
    }

}
