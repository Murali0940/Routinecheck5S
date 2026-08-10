package org.nisino.tests;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import base.BaseDriver;
import fivesservice.Nisinoseikiss;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;

public class NisinoScanTest extends BaseDriver {

    @Test(priority = 1, description = "Verify login with valid Company and User credentials.")
    public void validNisinoLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("nisino.compusername"), ConfigReader.get("nisino.comppassword"));

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.userLogin(ConfigReader.get("nisino.username"), ConfigReader.get("nisino.password"));

        userPage.verifyPageURL();

    }

    @Test(priority = 2, description = "validatingDrawSocketTest")

    public void validatingScanSocketTest() {

        Nisinoseikiss nisinoseikiss = new Nisinoseikiss(page);
        nisinoseikiss.homePageURL();
        nisinoseikiss.validateHyperLinkIcon();
        nisinoseikiss.clickHyperLinkIcon();
        nisinoseikiss.hyperLinkPageURL();
        nisinoseikiss.validateScanSocketIcon();
        nisinoseikiss.clickScanSocketIcon();
        nisinoseikiss.clickBeforeWorkFolder();
        nisinoseikiss.setPagination("100");
        nisinoseikiss.verifyTodayFileCountAndGetScreenshot();
        nisinoseikiss.clickHome();
        nisinoseikiss.clickcompletedfolder();
        nisinoseikiss.setPagination("100");
        nisinoseikiss.verifyTodayFileCountAndGetScreenshot();

    }

}
