package org.attribute;

import java.io.IOException;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;

import base.BaseDriver;
import pages.AttributeProcess;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;

public class AttributeTest extends BaseDriver {

    @Test(priority = 1, description = "Verify login with valid Company and User credentials.")
    public void validLoginTest() {

        Locator languageDropdown = page.locator("#mySelect");
        languageDropdown.selectOption(ConfigReader.get("selectLanguage"));

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(ConfigReader.get("testpro.compusername"), ConfigReader.get("testpro.comppassword"));

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.verifyPageURL();

        userPage.userLogin(ConfigReader.get("username"), ConfigReader.get("password"));

    }

    @Test(priority = 2, description = "setting the attribute for the file", enabled = false)
    public void setAttributeTest() throws IOException {
        AttributeProcess attributeProcess = new AttributeProcess(page);
        attributeProcess.clickHyperlinkIcon();
        attributeProcess.clickScanIcon();
        attributeProcess.setAttribute();
        attributeProcess.dragLatestPdfToScanSocket();

    }

    @Test(priority = 3, description = "Set Attribute In Drawing Management", enabled = true)
    public void setAttributeInDrawingManagement() throws IOException {
        AttributeProcess attributeProcess = new AttributeProcess(page);
        attributeProcess.drawingManagementicon();
    }

}
