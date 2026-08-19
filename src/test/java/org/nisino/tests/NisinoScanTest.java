package org.nisino.tests;

import org.testng.annotations.Test;

import base.BaseDriver;
import fivesservice.Nisinoseikiss;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.AllureScreenshotUtil;
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
        AllureScreenshotUtil.allureAttachScreenshot(page, "hyperlink");
        nisinoseikiss.validateScanSocketIcon();
        nisinoseikiss.clickScanSocketIcon();
        AllureScreenshotUtil.allureAttachScreenshot(page, "ScanSocket");
        nisinoseikiss.clickBeforeWorkFolder();
        nisinoseikiss.setPagination("100");
        beforeWorkResult = nisinoseikiss.verifyTodayFileCountAndGetScreenshot("BEFORE WORK");
        AllureScreenshotUtil.allureAttachScreenshot(page, "BEFORE WORK");
        nisinoseikiss.clickHome();
        nisinoseikiss.clickcompletedfolder();
        nisinoseikiss.setPagination("100");
        completedResult = nisinoseikiss.verifyTodayFileCountAndGetScreenshot("COMPLETED");
        AllureScreenshotUtil.allureAttachScreenshot(page, "COMPLETED");
        TestExecutionReport.addNisinoResult(
                "NISINOSEIKISS",
                "Today",
                beforeWorkResult,
                beforeWorkResult.getScreenshotPath(),
                completedResult,
                completedResult.getScreenshotPath());

    }

}
