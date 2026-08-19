package org.attributeprocess;

import org.testng.annotations.Test;

import base.BaseDriver;
import base.CreateDummyPdf;
import fivesservice.AttributeProcessPage;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.AllureScreenshotUtil;
import utils.ConfigReader;

public class AttributeProcessTest extends BaseDriver {

    @Test(priority = 1, description = "Verify login with valid Company and User credentials.")
    public void validNisinoLoginTest() {

        page.locator("#mySelect").selectOption(ConfigReader.get("selectLanguage"));

        System.out.println("Selected Language: " + ConfigReader.get("selectLanguage"));

        System.out.println("Starting Company Login...");

        CompLoginPage companyPage = new CompLoginPage(page);
        UserLoginPage userPage = new UserLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("atkgi.compusername"), ConfigReader.get("atkgi.comppassword"));

        userPage.verifyPageURL();
        // ---------------------------------------------------------
        // User Login
        // ---------------------------------------------------------
        System.out.println("Waiting for User Login page...");

        System.out.println("User Login page verified.");

        userPage.userLogin(ConfigReader.get("username"), ConfigReader.get("password"));
        System.out.println("User logged in successfully.");

    }

    @Test(priority = 2, description = "Verify Attribute Process")
    public void attributeProcessTest() {

        AttributeProcessPage attributeProcessPage = new AttributeProcessPage(page);
        CreateDummyPdf createDummyPdf = new CreateDummyPdf(page);

        attributeProcessPage.clickHyperLinkIcon();
        attributeProcessPage.validateHyperLinkIcon();
        AllureScreenshotUtil.allureAttachScreenshot(page, "Hyperlink Icon");
        attributeProcessPage.setPagination("100");
        AllureScreenshotUtil.allureAttachScreenshot(page, "Pagination Set to 100");
        attributeProcessPage.clickDrawSocketIcon();
        AllureScreenshotUtil.allureAttachScreenshot(page, "Drawsocket Icon");
        attributeProcessPage.validateDrawSocketIcon();
        attributeProcessPage.verifyDrawSocketURL();
        createDummyPdf.createDummypdf();

    }

}
