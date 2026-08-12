package org.sanmatsu.tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import fivesservice.Sanmatsu;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;
import base.BaseDriver;

public class SanmatsuTest extends BaseDriver {

    Sanmatsu sanmatsu;

    @Test(priority = 1, description = "Verify login with valid Company and User credentials")
    public void validLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);
        UserLoginPage userPage = new UserLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("sanmatsu.compusername"), ConfigReader.get("sanmatsu.comppassword"));

        userPage.verifyPageURL();

        userPage.userLogin(ConfigReader.get("username"), ConfigReader.get("password"));

    }

    @Test(priority = 2, description = "validatingSanmatsuHomePage")

    public void validatingSanmatsuHomePage() {

        sanmatsu = new Sanmatsu(page);
        sanmatsu.homePageURL();
        sanmatsu.validatehyperlinkicon();
        sanmatsu.clickhyperlinkicon();
        sanmatsu.hyperLinkPageURL();
        sanmatsu.validatedrawsocketicon();
        sanmatsu.clickdrawsocketicon();
        sanmatsu.drawSocketURL();
        sanmatsu.setPagination("100");
        sanmatsu.verifyTodayFileCountAndGetScreenshot();
    }

}
