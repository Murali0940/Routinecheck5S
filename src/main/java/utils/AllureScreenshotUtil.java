package utils;

import java.io.ByteArrayInputStream;

import com.microsoft.playwright.Page;

import io.qameta.allure.Allure;

public final class AllureScreenshotUtil {

    private AllureScreenshotUtil() {
        // Prevent object creation
    }

    public static void allureAttachScreenshot(
            Page page,
            String screenshotName) {

        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions()
                        .setFullPage(true));

        Allure.addAttachment(
                screenshotName,
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png");
    }

}
