package org.oaksystem.tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import base.BaseDriver;
import fivesservice.Oaksystem;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;

public class OakSystemDrawTest extends BaseDriver {

    Oaksystem oakSystem;

    @Test(priority = 1, description = "Verify login with valid Company and User credentials")
    public void validLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("oaksystem.username"), ConfigReader.get("oaksystem.password"));

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.verifyPageURL();

        userPage.userLogin(ConfigReader.get("oaksystem.user.username"), ConfigReader.get("oaksystem.user.password"));

    }

    @Test(priority = 2, description = "validatingDrawSocketTest")

    public void validatingDrawSocketTest() {

        oakSystem = new Oaksystem(page);
        oakSystem.homePageURL();
        oakSystem.validateHyperLinkIcon();
        oakSystem.clickHyperLinkIcon();
        oakSystem.hyperLinkPageURL();
        oakSystem.validateDrawSocketIcon();
        oakSystem.clickDrawSocketIcon();
        oakSystem.drawSocketURL();
        oakSystem.setPagination("100");
        oakSystem.verifyTodayFileCount();
    }

}
