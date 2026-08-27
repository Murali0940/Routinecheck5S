package base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.Dimension;
import java.awt.Toolkit;

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

    // ============================================================
    // TEST CLASS TIMESTAMP
    // ============================================================

    protected String testStartTimestamp;

    // ============================================================
    // VIDEO DIRECTORY
    // ============================================================

    protected Path videoDir;

    // ============================================================
    // STORE ALL VIDEO PATHS
    //
    // Every class adds its video here.
    // EmailReportService will read this list.
    // ============================================================

    private static final List<String> videoPaths = Collections.synchronizedList(new ArrayList<>());

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    int screenWidth = (int) screenSize.getWidth();
    int screenHeight = (int) screenSize.getHeight();

    // ============================================================
    // BEFORE CLASS
    // ============================================================

    @BeforeClass
    public void setup() {

        // ============================================================
        // TIMESTAMP
        // ============================================================

        testStartTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        System.out.println("=================  =============================");

        System.out.println("TEST CLASS STARTED");

        System.out.println("Class : " + this.getClass().getSimpleName());

        System.out.println("Time  : " + testStartTimestamp);

        System.out.println("==============================================");

        // ============================================================
        // CREATE VIDEO DIRECTORY
        // ============================================================

        videoDir = Paths.get("test-videos");

        try {

            Files.createDirectories(videoDir);

        } catch (IOException e) {

            throw new RuntimeException("Unable to create video directory", e);
        }

        // ============================================================
        // READ CONFIGURATION
        // ============================================================

        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

        double slowMo = Double.parseDouble(ConfigReader.get("slowmo"));

        timeout = Double.parseDouble(ConfigReader.get("timeout"));

        String baseUrl = ConfigReader.get("base.url");

        // ============================================================
        // START PLAYWRIGHT
        // ============================================================

        playwright = Playwright.create();

        // ============================================================
        // LAUNCH BROWSER
        // ============================================================

        browser = playwright.chromium()
                .launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(headless)
                                .setSlowMo(slowMo)
                                .setArgs(Arrays.asList(
                                        "--start-maximized",
                                        "--window-position=0,0",
                                        "--window-size="
                                                + screenWidth
                                                + ","
                                                + screenHeight)));

        System.out.println("Browser launched successfully.");

        // ============================================================
        // CREATE BROWSER CONTEXT
        //
        // Video recording starts here.
        // ============================================================

        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(screenWidth, screenHeight)
                        .setRecordVideoDir(videoDir)
                        .setRecordVideoSize(screenWidth, screenHeight));

        System.out.println("Browser context created successfully.");

        // ============================================================
        // CREATE PAGE
        // ============================================================

        page = context.newPage();

        System.out.println("Page created successfully.");

        // ============================================================
        // SET DEFAULT TIMEOUT
        // ============================================================

        page.setDefaultTimeout(timeout);

        // ============================================================
        // NAVIGATE TO BASE URL
        // ============================================================

        page.navigate(baseUrl);

        System.out.println("Navigated to base URL successfully.");

        System.out.println("Current URL: " + page.url());

        System.out.println("==============================================");

        System.out.println("VIDEO RECORDING STARTED");

        System.out.println("==============================================");
    }

    // ============================================================
    // AFTER CLASS
    // ============================================================

    @AfterClass
    public void teardown() {

        System.out.println("==============================================");

        System.out.println("TEST CLASS FINISHED");

        System.out.println("Class : " + this.getClass().getSimpleName());

        System.out.println("==============================================");

        Path originalVideoPath = null;

        // ============================================================
        // CLOSE CONTEXT FIRST
        //
        // IMPORTANT:
        // Playwright finalizes the video when the context closes.
        // ============================================================

        if (context != null) {

            try {

                context.close();

                System.out.println("Context closed successfully.");

            } catch (Exception e) {

                System.out.println("Error while closing context.");

                e.printStackTrace();
            }
        }

        // ============================================================
        // GET VIDEO PATH AFTER CONTEXT CLOSE
        // ============================================================

        if (page != null && page.video() != null) {

            try {

                originalVideoPath = page.video().path();

                System.out.println("Original video path: " + originalVideoPath);

            } catch (Exception e) {

                System.out.println("Unable to get video path.");

                e.printStackTrace();
            }
        }

        // ============================================================
        // CLOSE BROWSER
        // ============================================================

        if (browser != null) {

            try {

                browser.close();

                System.out.println("Browser closed successfully.");

            } catch (Exception e) {

                System.out.println("Error while closing browser.");

                e.printStackTrace();
            }
        }

        // ============================================================
        // RENAME VIDEO
        // ============================================================

        if (originalVideoPath != null) {

            String className = this.getClass().getSimpleName();

            String newFileName = className + "_" + testStartTimestamp + ".webm";

            Path newVideoPath = videoDir.resolve(newFileName);

            try {

                Files.move(originalVideoPath, newVideoPath, StandardCopyOption.REPLACE_EXISTING);

                // ====================================================
                // ADD VIDEO TO VIDEO LIST
                // ====================================================

                addVideoPath(newVideoPath.toAbsolutePath().toString());

                System.out.println("==============================================");

                System.out.println("VIDEO SAVED SUCCESSFULLY");

                System.out.println("Video : " + newVideoPath.toAbsolutePath());

                System.out.println("Added to email attachment list.");

                System.out.println("==============================================");

            } catch (IOException e) {

                System.out.println("Unable to rename/move video.");

                e.printStackTrace();
            }
        }

        // ============================================================
        // CLOSE PLAYWRIGHT
        // ============================================================

        if (playwright != null) {

            try {

                playwright.close();

                System.out.println("Playwright closed successfully.");

            } catch (Exception e) {

                System.out.println("Error while closing Playwright.");

                e.printStackTrace();
            }
        }
    }

    // ============================================================
    // ADD VIDEO PATH
    // ============================================================

    private static void addVideoPath(
            String videoPath) {

        if (videoPath != null
                && !videoPath.isBlank()) {

            videoPaths.add(videoPath);

            System.out.println("Video added for email: " + videoPath);
        }
    }

    // ============================================================
    // GET ALL VIDEO PATHS
    //
    // EmailReportService calls this method.
    // ============================================================

    public static List<String> getVideoPaths() {

        synchronized (videoPaths) {

            return new ArrayList<>(
                    videoPaths);
        }
    }

    // ============================================================
    // CLEAR VIDEO PATHS
    //
    // Useful after email has been sent.
    // ============================================================

    public static void clearVideoPaths() {

        videoPaths.clear();

        System.out.println("Video attachment list cleared.");
    }

    // ================================================================
    // SCREENSHOT METHOD
    // ================================================================

    public static String takeScreenshot(
            Page page,
            String screenshotName) {

        String timestamp = LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String path = "screenshots/"
                + screenshotName
                + "_"
                + timestamp
                + ".png";

        try {

            Files.createDirectories(
                    Paths.get("screenshots"));

        } catch (IOException e) {

            throw new RuntimeException("Unable to create screenshot directory", e);
        }

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));

        System.out.println("Screenshot saved: " + path);

        return path;
    }
}