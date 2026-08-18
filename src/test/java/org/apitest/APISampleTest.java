package org.apitest;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Response;

import base.BaseDriver;
import fivesservice.Sanmatsu;
import io.qameta.allure.Allure;
import pages.CompLoginPage;
import pages.UserLoginPage;
import utils.ConfigReader;

public class APISampleTest extends BaseDriver {

    private Sanmatsu sanmatsu;

    // ============================================================
    // TEST 1 - LOGIN
    // ============================================================

    @Test(priority = 1, description = "Verify login with valid Company and User credentials")
    public void validLoginTest() {

        // --------------------------------------------------------
        // Select Language
        // --------------------------------------------------------

        Locator languageDropdown = page.locator("#mySelect");

        languageDropdown.selectOption(
                ConfigReader.get("selectLanguage"));

        // --------------------------------------------------------
        // Company Login
        // --------------------------------------------------------

        CompLoginPage companyPage = new CompLoginPage(page);

        companyPage.companyLogin(
                ConfigReader.get("sanmatsu.compusername"),
                ConfigReader.get("sanmatsu.comppassword"));

        // --------------------------------------------------------
        // User Login
        // --------------------------------------------------------

        UserLoginPage userPage = new UserLoginPage(page);

        userPage.verifyPageURL();

        userPage.userLogin(
                ConfigReader.get("username"),
                ConfigReader.get("password"));
    }

    // ============================================================
    // TEST 2 - SANMATSU + GET SOCKET FILES API
    // ============================================================

    @Test(priority = 2, description = "Validate Sanmatsu Home Page and Get Socket Files API")
    public void validatingSanmatsuHomePage() {

        // --------------------------------------------------------
        // Create Sanmatsu Page Object
        // --------------------------------------------------------

        sanmatsu = new Sanmatsu(page);

        // --------------------------------------------------------
        // Home Page Validation
        // --------------------------------------------------------

        sanmatsu.homePageURL();

        // --------------------------------------------------------
        // HyperLink
        // --------------------------------------------------------

        sanmatsu.validatehyperlinkicon();

        sanmatsu.clickhyperlinkicon();

        sanmatsu.hyperLinkPageURL();

        // --------------------------------------------------------
        // Drawing Socket
        // --------------------------------------------------------

        sanmatsu.validatedrawsocketicon();

        sanmatsu.clickdrawsocketicon();

        sanmatsu.drawSocketURL();

        // --------------------------------------------------------
        // Set Pagination
        // --------------------------------------------------------

        sanmatsu.setPagination("100");

        // --------------------------------------------------------
        // Capture getSocketFiles API
        // --------------------------------------------------------

        Response response = getSocketFiles();

        // --------------------------------------------------------
        // Convert API response to JSON
        // --------------------------------------------------------

        JsonNode jsonResponse = convertResponseToJson(response);

        // --------------------------------------------------------
        // Print individual file information
        // --------------------------------------------------------

        printFileInformation(jsonResponse);
    }

    // ============================================================
    // GET SOCKET FILES API
    // ============================================================

    public Response getSocketFiles() {

        Allure.step("Getting socket files API response");

        System.out.println();
        System.out.println("========================================");
        System.out.println("       WAITING FOR SOCKET FILES API");
        System.out.println("========================================");

        /*
         * Wait for the getSocketFiles API response.
         *
         * page.reload() triggers the application API call.
         */

        Response response = page.waitForResponse(

                res -> res.url().contains("getSocketFiles")
                        && res.status() == 200,

                () -> {

                    page.reload();

                });

        // ========================================================
        // API INFORMATION
        // ========================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println("       SOCKET FILES API RESPONSE");
        System.out.println("========================================");

        System.out.println(
                "URL    : "
                        + response.url());

        System.out.println(
                "Method : "
                        + response.request().method());

        System.out.println(
                "Status : "
                        + response.status());

        System.out.println("========================================");

        Allure.step(
                "Socket Files API Status: "
                        + response.status());

        return response;
    }

    // ============================================================
    // CONVERT RESPONSE TO JSON
    // ============================================================

    public JsonNode convertResponseToJson(
            Response response) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            /*
             * response.text() gives us the API response
             * as a String.
             *
             * Jackson converts that String into JsonNode.
             */

            JsonNode jsonResponse = mapper.readTree(
                    response.text());

            // ====================================================
            // PRINT PRETTY JSON
            // ====================================================

            System.out.println();
            System.out.println("========================================");
            System.out.println("             JSON RESPONSE");
            System.out.println("========================================");

            String prettyJson = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(
                            jsonResponse);

            System.out.println(prettyJson);

            System.out.println("========================================");

            Allure.step(
                    "Socket Files API response converted to JSON");

            return jsonResponse;

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Unable to parse API response as JSON.");

            System.out.println();
            System.out.println("Raw API Response:");

            System.out.println(
                    response.text());

            throw new RuntimeException(
                    "Failed to convert Socket Files API response to JSON",
                    e);
        }
    }

    // ============================================================
    // PRINT FILE INFORMATION
    // ============================================================

    private void printFileInformation(
            JsonNode jsonResponse) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("          FILE INFORMATION");
        System.out.println("========================================");

        /*
         * Check whether API returned an array.
         */

        if (!jsonResponse.isArray()) {

            System.out.println(
                    "API response is not a JSON array.");

            return;
        }

        System.out.println(
                "Total API Records: "
                        + jsonResponse.size());

        System.out.println("========================================");

        // ========================================================
        // PROCESS EACH FILE
        // ========================================================

        int recordNumber = 1;

        for (JsonNode file : jsonResponse) {

            String filename = file.path("filename")
                    .asText("");

            String guid = file.path("guid")
                    .asText("");

            int attribute = file.path("attribute")
                    .asInt(0);

            String modifiedDate = file.path("modifieddate")
                    .asText("");

            String date = file.path("date")
                    .asText("");

            int id = file.path("id")
                    .asInt(0);

            int rootId = file.path("rootid")
                    .asInt(0);

            // ====================================================
            // PRINT
            // ====================================================

            System.out.println();
            System.out.println(
                    "Record #" + recordNumber);

            System.out.println(
                    "Filename      : "
                            + filename);

            System.out.println(
                    "GUID          : "
                            + guid);

            System.out.println(
                    "Attribute     : "
                            + attribute);

            System.out.println(
                    "Date          : "
                            + date);

            System.out.println(
                    "Modified Date : "
                            + modifiedDate);

            System.out.println(
                    "ID            : "
                            + id);

            System.out.println(
                    "Root ID       : "
                            + rootId);

            System.out.println("----------------------------------------");

            recordNumber++;
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println(
                "Processed Records: "
                        + (recordNumber - 1));
        System.out.println("========================================");
    }
}