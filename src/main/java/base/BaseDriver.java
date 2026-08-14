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
    public double timeout;

    @BeforeClass

    public void setup() {

        // ============================================================
        // READ CONFIGURATION
        // ============================================================

        boolean headless = Boolean.parseBoolean(
                ConfigReader.get("headless"));

        double slowMo = Double.parseDouble(
                ConfigReader.get("slowmo"));

        timeout = Double.parseDouble(
                ConfigReader.get("timeout"));

        String baseUrl = ConfigReader.get("base.url");

        // ============================================================
        // START PLAYWRIGHT
        // ============================================================

        playwright = Playwright.create();

        browser = playwright.chromium()

                .launch(new BrowserType.LaunchOptions()

                        .setHeadless(headless)

                        .setSlowMo(slowMo)
                        .setArgs(Arrays.asList("--start-maximized")));
        System.out.println("Browser launched successfully." + browser);

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        page = context.newPage();

        System.out.println("Page created successfully." + page);

        page.navigate(baseUrl);

        System.out.println("Navigated to base URL successfully." + page.url());

        // page.pause(); //playwright inspector will open and you can debug your test

    }

    @AfterClass

    public void teardown() {

        if (context != null) {
            context.close();
            System.out.println("Context closed successfully." + context);
        }

        if (browser != null) {
            browser.close();
            System.out.println("Browser closed successfully." + browser);
        }

        if (playwright != null) {
            playwright.close();
            System.out.println("Playwright closed successfully." + playwright);
        }

    }

    public static String takeScreenshot(Page page, String screenshotName) {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String path = "screenshots/" + screenshotName + "_" + timestamp + ".png";

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(path))
                .setFullPage(true));

        System.out.println("Screenshot saved: " + path);
        return path;
    }

}