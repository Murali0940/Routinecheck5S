package base;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.microsoft.playwright.*;

import utils.ConfigReader;

public class BaseDriver {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    public Page page;

    @BeforeClass

    public void setup() {

        playwright = Playwright.create();

        browser = playwright.chromium()

                .launch(new BrowserType.LaunchOptions()

                        .setHeadless(Boolean.parseBoolean(ConfigReader.get("headless")))

                        .setSlowMo(Double.parseDouble(ConfigReader.get("slowmo")))
                        .setArgs(Arrays.asList("--start-maximized")));

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        page = context.newPage();

        page.navigate(ConfigReader.get("base.url"));

        // page.pause(); //playwright inspector will open and you can debug your test

    }

    @AfterClass

    public void teardown() {

        context.close();

        browser.close();

        playwright.close();

    }

    public static void takeScreenshot(Page page, String screenshotName) {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String path = "screenshots/" + screenshotName + "_" + timestamp + ".png";

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(true));

        System.out.println("Screenshot saved: " + path);
    }

    // @AfterMethod
    // public void tearDown(ITestResult result) {

    // if (result.getStatus() == ITestResult.SUCCESS) {
    // takeScreenshot(result.getName());
    // }
    // }

}