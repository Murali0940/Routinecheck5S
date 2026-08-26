package org.masuda;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import base.BaseDriver;
import fivesservice.Masuda;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.AllureScreenshotUtil;
import utils.ConfigReader;

public class MasudaScanTest extends BaseDriver {

    Masuda masuda;

    @Test(priority = 1, description = "Verify login with valid Company and User credentials")
    public void validLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("masuda.compusername"), ConfigReader.get("masuda.comppassword"));

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.verifyPageURL();

        userPage.userLogin(ConfigReader.get("masuda.username"), ConfigReader.get("masuda.password"));

    }

    @Test(priority = 2, description = "validatingS09Socket")

    public void validatingS09Socket() {

        masuda = new Masuda(page);
        masuda.homePageURL();
        masuda.validateHyperLinkIcon();
        masuda.clickHyperLinkIcon();
        masuda.hyperLinkPageURL();
        masuda.validateScanSocketIcon();
        masuda.clickScanSocketIcon();
        masuda.drawSocketURL();
        masuda.setPagination("100");
        AllureScreenshotUtil.allureAttachScreenshot(page, "S09 Socket");
        masuda.verifyTodayFileCountAndGetScreenshot();
    }

}
