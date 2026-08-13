package org.nisino.tests;

import org.testng.annotations.Test;

import base.BaseDriver;
import fivesservice.Nisinoseikiss;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;
import utils.FileCountResult;
import utils.TestExecutionReport;

public class NisinoScanTest extends BaseDriver {

    @Test(priority = 1, description = "Verify login with valid Company and User credentials.")
    public void validNisinoLoginTest() {

        page.locator("#mySelect").selectOption(ConfigReader.get("selectLanguage"));

        System.out.println("Selected Language: " + ConfigReader.get("selectLanguage"));

        System.out.println("Starting Company Login...");

        CompLoginPage companyPage = new CompLoginPage(page);
        UserLoginPage userPage = new UserLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("nisino.compusername"), ConfigReader.get("nisino.comppassword"));

        userPage.verifyPageURL();
        // ---------------------------------------------------------
        // User Login
        // ---------------------------------------------------------
        System.out.println("Waiting for User Login page...");

        System.out.println("User Login page verified.");

        userPage.userLogin(ConfigReader.get("username"), ConfigReader.get("password"));
        System.out.println("User logged in successfully.");

    }

    @Test(priority = 2, description = "validatingDrawSocketTest")

    public void validatingScanSocketTest() {

        FileCountResult beforeWorkResult;

        FileCountResult completedResult;

        Nisinoseikiss nisinoseikiss = new Nisinoseikiss(page);
        nisinoseikiss.validateHyperLinkIcon();
        nisinoseikiss.clickHyperLinkIcon();
        nisinoseikiss.hyperLinkPageURL();
        nisinoseikiss.validateScanSocketIcon();
        nisinoseikiss.clickScanSocketIcon();
        nisinoseikiss.clickBeforeWorkFolder();
        nisinoseikiss.setPagination("100");
        beforeWorkResult = nisinoseikiss.verifyTodayFileCountAndGetScreenshot("作業前");
        nisinoseikiss.clickHome();
        nisinoseikiss.clickcompletedfolder();
        nisinoseikiss.setPagination("100");
        completedResult = nisinoseikiss.verifyTodayFileCountAndGetScreenshot("完了");
        TestExecutionReport.addNisinoResult(
                "NISINOSEIKISS",
                "Today",
                beforeWorkResult,
                beforeWorkResult.getScreenshotPath(),
                completedResult,
                completedResult.getScreenshotPath());

    }

}
