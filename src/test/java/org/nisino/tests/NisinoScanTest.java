package org.nisino.tests;

import org.testng.annotations.Test;

import base.BaseDriver;
import fivesservice.Nisinoseikiss;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;

public class NisinoScanTest extends BaseDriver {

    @Test(priority = 1, description = "Verify login with valid Company and User credentials.")
    public void validNisinoLoginTest() {

        page.locator("#mySelect").selectOption(ConfigReader.get("selectLanguage"));

        System.out.println("Selected Language: " + ConfigReader.get("selectLanguage"));

        System.out.println("Starting Company Login...");

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("nisino.compusername"), ConfigReader.get("nisino.comppassword"));

        // ---------------------------------------------------------
        // User Login
        // ---------------------------------------------------------
        System.out.println("Waiting for User Login page...");

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.verifyPageURL();

        System.out.println("User Login page verified.");

        userPage.userLogin(ConfigReader.get("user.username"), ConfigReader.get("user.password"));
        System.out.println("User logged in successfully.");

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
