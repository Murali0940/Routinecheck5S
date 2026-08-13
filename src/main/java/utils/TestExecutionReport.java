package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestExecutionReport {

    private static final List<String> results = new ArrayList<>();

    private static final List<String> screenshotPaths = new ArrayList<>();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    // ============================================================
    // NORMAL COMPANY RESULT
    // OAKSYSTEM / SANMATSU
    // ============================================================

    public static void addResult(
            String company,
            String day,
            int attributeFileCount,
            int noAttributeFileCount,
            String screenshotPath) {

        String result = "======================================\n" +
                "COMPANY : " + company + "\n" +
                "DAY     : " + day + "\n" +
                "======================================\n" +
                "--------------------------------------\n" +
                "Attribute Icon Files     : " + attributeFileCount + "\n" +
                "No Attribute Icon Files : " + noAttributeFileCount + "\n" +
                "Screenshot              : " + screenshotPath + "\n" +
                "======================================\n";

        results.add(result);

        if (screenshotPath != null) {
            screenshotPaths.add(screenshotPath);
        }
    }

    // ============================================================
    // OHKUMA RESULT
    // ============================================================

    public static void addOhkumaResult(
            String company,
            String day,
            int todayFileCount,
            int folderCount,
            int filesInsideFolderCount,
            int yellowFileCount,
            int nonYellowFileCount,
            String screenshotPath) {

        String result = "========================================\n" +
                "             " + company + " - "
                + day.toUpperCase() + "\n" +
                "========================================\n" +
                "----------------------------------------\n" +
                "Today File Count          : "
                + todayFileCount + "\n" +
                "Folder Count              : "
                + folderCount + "\n" +
                "Files Inside Folder Count : "
                + filesInsideFolderCount + "\n" +
                "Yellow Icon Files         : "
                + yellowFileCount + "\n" +
                "Non-Yellow Icon Files     : "
                + nonYellowFileCount + "\n" +
                "Screenshot                : "
                + screenshotPath + "\n" +
                "========================================\n";

        results.add(result);

        if (screenshotPath != null) {
            screenshotPaths.add(screenshotPath);
        }
    }
    // TWO FOLDERS -> ONE COMPANY RESULT
    // ============================================================

    public static void addNisinoResult(
            String company,
            String day,
            FileCountResult beforeWorkResult,
            String beforeWorkScreenshot,
            FileCountResult completedResult,
            String completedScreenshot) {

        int totalAttribute = beforeWorkResult.getAttributeFileCount()
                + completedResult.getAttributeFileCount();

        int totalNoAttribute = beforeWorkResult.getNoAttributeFileCount()
                + completedResult.getNoAttributeFileCount();

        String result = "======================================\n" +
                "COMPANY : " + company + "\n" +
                "DAY     : " + day + "\n" +
                "======================================\n" +

                "-------------- 作業前 ----------------\n" +

                "Attribute Icon Files     : "
                + beforeWorkResult.getAttributeFileCount()
                + "\n" +

                "No Attribute Icon Files : "
                + beforeWorkResult.getNoAttributeFileCount()
                + "\n" +

                "Screenshot              : "
                + beforeWorkScreenshot
                + "\n" +

                "---------------- 完了 ----------------\n" +

                "Attribute Icon Files     : "
                + completedResult.getAttributeFileCount()
                + "\n" +

                "No Attribute Icon Files : "
                + completedResult.getNoAttributeFileCount()
                + "\n" +

                "Screenshot              : "
                + completedScreenshot
                + "\n" +

                "--------------------------------------\n" +

                "TOTAL ATTRIBUTE FILES   : "
                + totalAttribute
                + "\n" +

                "TOTAL NO ATTRIBUTE FILES: "
                + totalNoAttribute
                + "\n" +

                "======================================\n";

        results.add(result);

        if (beforeWorkScreenshot != null) {
            screenshotPaths.add(beforeWorkScreenshot);
        }

        if (completedScreenshot != null) {
            screenshotPaths.add(completedScreenshot);
        }
    }

    // ============================================================
    // GET COMPLETE REPORT
    // ============================================================

    public static String getReport() {

        StringBuilder report = new StringBuilder();

        report.append(
                "AUTOMATION TEST REPORT\n\n");

        report.append(
                "Execution Time : "
                        + LocalDateTime.now()
                                .format(DATE_TIME_FORMATTER)
                        + "\n\n");

        for (String result : results) {

            report.append(result);

            report.append("\n");
        }

        return report.toString();
    }

    // ============================================================
    // GET SCREENSHOT PATHS
    // ============================================================

    public static List<String> getScreenshotPaths() {
        return new ArrayList<>(screenshotPaths);
    }

    // ============================================================
    // CLEAR REPORT
    // ============================================================

    public static void clear() {

        results.clear();
        screenshotPaths.clear();
    }
}