package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestExecutionReport {

    // ============================================================
    // REPORT STORAGE
    // ============================================================

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
            int totalFileCount,
            int attributeFileCount,
            int nonAttributeFileCount,
            String screenshotPath) {

        StringBuilder result = new StringBuilder();

        result.append("""
                <table width="100%"
                       cellpadding="0"
                       cellspacing="0"
                       style="
                           border:1px solid #d9e2ec;
                           border-radius:8px;
                           margin-bottom:20px;
                           background-color:#ffffff;
                       ">

                    <!-- COMPANY HEADER -->
                    <tr>
                        <td colspan="5"
                            style="
                                background-color:#f8fafc;
                                padding:15px;
                                border-bottom:1px solid #e5e7eb;
                            ">

                            <table width="100%"
                                   cellpadding="0"
                                   cellspacing="0">

                                <tr>

                                    <td style="
                                        font-size:18px;
                                        font-weight:bold;
                                        color:#0f172a;
                                    ">
                """);

        result.append(escapeHtml(company));

        result.append("""
                                    </td>

                                    <td align="right">

                                        <span style="
                                            background-color:#dcfce7;
                                            color:#166534;
                                            padding:6px 12px;
                                            border-radius:20px;
                                            font-size:11px;
                                            font-weight:bold;
                                        ">
                                            PASSED
                                        </span>

                                    </td>

                                </tr>

                                <tr>

                                    <td style="
                                        padding-top:5px;
                                        color:#64748b;
                                        font-size:12px;
                                    ">
                """);

        result.append(escapeHtml(day));

        result.append("""
                                    </td>

                                </tr>

                            </table>

                        </td>
                    </tr>

                    <!-- TABLE HEADER -->
                    <tr style="
                        background-color:#2563eb;
                        color:#ffffff;
                    ">

                        <th style="
                            padding:11px;
                            text-align:left;
                            font-size:12px;
                        ">
                            Day
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Total File Count
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Attribute Icon Files
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Non-Attribute Icon Files
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Screenshot
                        </th>

                    </tr>

                    <!-- TABLE DATA -->
                    <tr>

                        <!-- DAY -->
                        <td style="
                            padding:12px;
                            border-bottom:1px solid #e5e7eb;
                            color:#334155;
                            font-size:13px;
                        ">
                """);

        result.append(escapeHtml(day));

        result.append("""
                        </td>

                        <!-- TOTAL FILE COUNT -->
                        <td style="
                            padding:12px;
                            text-align:center;
                            border-bottom:1px solid #e5e7eb;
                            color:#2563eb;
                            font-size:20px;
                            font-weight:bold;
                        ">
                """);

        result.append(totalFileCount);

        result.append("""
                        </td>

                        <!-- ATTRIBUTE ICON FILES -->
                        <td style="
                            padding:12px;
                            text-align:center;
                            border-bottom:1px solid #e5e7eb;
                            color:#15803d;
                            font-size:20px;
                            font-weight:bold;
                        ">
                """);

        result.append(attributeFileCount);

        result.append("""
                        </td>

                        <!-- NON-ATTRIBUTE ICON FILES -->
                        <td style="
                            padding:12px;
                            text-align:center;
                            border-bottom:1px solid #e5e7eb;
                            color:#dc2626;
                            font-size:20px;
                            font-weight:bold;
                        ">
                """);

        result.append(nonAttributeFileCount);

        result.append("""
                        </td>

                        <!-- SCREENSHOT -->
                        <td style="
                            padding:12px;
                            text-align:center;
                            border-bottom:1px solid #e5e7eb;
                        ">
                """);

        if (screenshotPath != null && !screenshotPath.isBlank()) {

            result.append("""
                            <span style="
                                background-color:#eff6ff;
                                color:#2563eb;
                                padding:6px 10px;
                                border-radius:6px;
                                font-size:11px;
                                font-weight:bold;
                            ">
                                Attached
                            </span>
                    """);

        } else {

            result.append("""
                            <span style="
                                background-color:#f1f5f9;
                                color:#64748b;
                                padding:6px 10px;
                                border-radius:6px;
                                font-size:11px;
                            ">
                                Not Available
                            </span>
                    """);
        }

        result.append("""
                        </td>

                    </tr>

                </table>
                """);

        results.add(result.toString());

        if (screenshotPath != null && !screenshotPath.isBlank()) {
            screenshotPaths.add(screenshotPath);
        }
    }

    // ============================================================
    // OHKUMA RESULT
    // ============================================================

    public static void addOhkumaResult(
            String company,
            String day,
            int totalFileCount,
            int attributeIconFileCount,
            int nonAttributeIconFileCount,
            String screenshotPath) {

        StringBuilder result = new StringBuilder();

        result.append("""
                <table width="100%"
                       cellpadding="0"
                       cellspacing="0"
                       style="
                           border:1px solid #d9e2ec;
                           border-radius:8px;
                           margin-bottom:20px;
                           background-color:#ffffff;
                       ">

                    <!-- COMPANY HEADER -->
                    <tr>
                        <td colspan="3"
                            style="
                                background-color:#f8fafc;
                                padding:15px;
                                border-bottom:1px solid #e5e7eb;
                            ">

                            <table width="100%"
                                   cellpadding="0"
                                   cellspacing="0">

                                <tr>

                                    <td style="
                                        font-size:18px;
                                        font-weight:bold;
                                        color:#0f172a;
                                    ">
                """);

        result.append(escapeHtml(company));

        result.append("""
                                    </td>

                                    <td align="right">

                                        <span style="
                                            background-color:#dcfce7;
                                            color:#166534;
                                            padding:6px 12px;
                                            border-radius:20px;
                                            font-size:11px;
                                            font-weight:bold;
                                        ">
                                            PASSED
                                        </span>

                                    </td>

                                </tr>

                                <tr>

                                    <td style="
                                        padding-top:5px;
                                        color:#64748b;
                                        font-size:12px;
                                    ">
                """);

        result.append(escapeHtml(day));

        result.append("""
                                    </td>

                                </tr>

                            </table>

                        </td>
                    </tr>

                    <!-- TABLE HEADER -->
                    <tr style="
                        background-color:#2563eb;
                        color:#ffffff;
                    ">

                        <th style="
                            padding:10px;
                            text-align:left;
                            font-size:12px;
                        ">
                            Metric
                        </th>

                        <th style="
                            padding:10px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Count
                        </th>

                        <th style="
                            padding:10px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Status
                        </th>

                    </tr>
                """);

        /*
         * 1. Total File Count
         */
        result.append(
                metricRow(
                        "Total File Count",
                        totalFileCount,
                        "#2563eb"));

        /*
         * 2. Attribute Icon File Count
         */
        result.append(
                metricRow(
                        "Attribute Icon File Count",
                        attributeIconFileCount,
                        "#16a34a"));

        /*
         * 3. Non-Attribute Icon File Count
         */
        result.append(
                metricRow(
                        "Non-Attribute Icon File Count",
                        nonAttributeIconFileCount,
                        "#dc2626"));

        /*
         * 4. Screenshot
         */
        result.append("""
                <tr>

                    <td style="
                        padding:12px;
                        color:#334155;
                        font-size:13px;
                        border-top:1px solid #e5e7eb;
                    ">
                        Screenshot
                    </td>

                    <td colspan="2"
                        style="
                            padding:12px;
                            text-align:center;
                            border-top:1px solid #e5e7eb;
                        ">
                """);

        if (screenshotPath != null && !screenshotPath.isBlank()) {

            result.append("""
                        <span style="
                            background-color:#eff6ff;
                            color:#2563eb;
                            padding:6px 10px;
                            border-radius:6px;
                            font-size:11px;
                            font-weight:bold;
                        ">
                            Attached
                        </span>
                    """);

        } else {

            result.append("""
                        <span style="
                            background-color:#f1f5f9;
                            color:#64748b;
                            padding:6px 10px;
                            border-radius:6px;
                            font-size:11px;
                        ">
                            Not Available
                        </span>
                    """);
        }

        result.append("""
                        </td>

                    </tr>

                </table>
                """);

        /*
         * Add report result.
         */
        results.add(result.toString());

        /*
         * Add screenshot path.
         */
        if (screenshotPath != null && !screenshotPath.isBlank()) {
            screenshotPaths.add(screenshotPath);
        }
    }

    // ============================================================
    // NISINO RESULT
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

        int totalNoAttribute = beforeWorkResult.getNonAttributeFileCount()
                + completedResult.getNonAttributeFileCount();

        StringBuilder result = new StringBuilder();

        result.append("""
                <table width="100%"
                       cellpadding="0"
                       cellspacing="0"
                       style="
                           border:1px solid #d9e2ec;
                           border-radius:8px;
                           margin-bottom:20px;
                           background-color:#ffffff;
                       ">

                    <!-- COMPANY HEADER -->
                    <tr>
                        <td colspan="4"
                            style="
                                background-color:#f8fafc;
                                padding:15px;
                                border-bottom:1px solid #e5e7eb;
                            ">

                            <table width="100%"
                                   cellpadding="0"
                                   cellspacing="0">

                                <tr>

                                    <td style="
                                        font-size:18px;
                                        font-weight:bold;
                                        color:#0f172a;
                                    ">
                """);

        result.append(escapeHtml(company));

        result.append("""
                                    </td>

                                    <td align="right">

                                        <span style="
                                            background-color:#dcfce7;
                                            color:#166534;
                                            padding:6px 12px;
                                            border-radius:20px;
                                            font-size:11px;
                                            font-weight:bold;
                                        ">
                                            PASSED
                                        </span>

                                    </td>

                                </tr>

                                <tr>

                                    <td style="
                                        padding-top:5px;
                                        color:#64748b;
                                        font-size:12px;
                                    ">
                """);

        result.append(escapeHtml(day));

        result.append("""
                                    </td>

                                </tr>

                            </table>

                        </td>
                    </tr>

                    <!-- FOLDER TABLE HEADER -->
                    <tr style="
                        background-color:#2563eb;
                        color:#ffffff;
                    ">

                        <th style="
                            padding:11px;
                            text-align:left;
                            font-size:12px;
                        ">
                            Folder
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Attribute Files
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            No Attribute Files
                        </th>

                        <th style="
                            padding:11px;
                            text-align:center;
                            font-size:12px;
                        ">
                            Screenshot
                        </th>

                    </tr>
                """);

        result.append(
                nisinoFolderRow(
                        "作業前",
                        beforeWorkResult,
                        beforeWorkScreenshot));

        result.append(
                nisinoFolderRow(
                        "完了",
                        completedResult,
                        completedScreenshot));

        result.append("""
                    <!-- TOTAL -->
                    <tr>

                        <td style="
                            padding:13px;
                            background-color:#f8fafc;
                            font-weight:bold;
                            color:#0f172a;
                            border-top:1px solid #d9e2ec;
                        ">
                            TOTAL
                        </td>

                        <td style="
                            padding:13px;
                            text-align:center;
                            background-color:#f8fafc;
                            font-weight:bold;
                            color:#15803d;
                            border-top:1px solid #d9e2ec;
                            font-size:18px;
                        ">
                """);

        result.append(totalAttribute);

        result.append("""
                        </td>

                        <td style="
                            padding:13px;
                            text-align:center;
                            background-color:#f8fafc;
                            font-weight:bold;
                            color:#ea580c;
                            border-top:1px solid #d9e2ec;
                            font-size:18px;
                        ">
                """);

        result.append(totalNoAttribute);

        result.append("""
                        </td>

                        <td style="
                            padding:13px;
                            text-align:center;
                            background-color:#f8fafc;
                            border-top:1px solid #d9e2ec;
                        ">
                            -
                        </td>

                    </tr>

                </table>
                """);

        results.add(result.toString());

        if (beforeWorkScreenshot != null) {
            screenshotPaths.add(beforeWorkScreenshot);
        }

        if (completedScreenshot != null) {
            screenshotPaths.add(completedScreenshot);
        }
    }

    // ============================================================
    // NORMAL METRIC ROW
    // ============================================================

    private static String metricRow(
            String metricName,
            int value,
            String valueColor) {

        return """
                <tr>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        color:#334155;
                        font-size:13px;
                    ">
                        %s
                    </td>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        text-align:center;
                        font-size:19px;
                        font-weight:bold;
                        color:%s;
                    ">
                        %d
                    </td>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        text-align:center;
                    ">
                        <span style="
                            background-color:#f0fdf4;
                            color:#166534;
                            padding:5px 9px;
                            border-radius:15px;
                            font-size:10px;
                            font-weight:bold;
                        ">
                            COMPLETED
                        </span>
                    </td>

                </tr>
                """.formatted(
                escapeHtml(metricName),
                valueColor,
                value);
    }

    // ============================================================
    // NISINO FOLDER ROW
    // ============================================================

    private static String nisinoFolderRow(
            String folderName,
            FileCountResult result,
            String screenshotPath) {

        String screenshotStatus;

        if (screenshotPath != null) {

            screenshotStatus = """
                    <span style="
                        background-color:#eff6ff;
                        color:#2563eb;
                        padding:6px 10px;
                        border-radius:6px;
                        font-size:11px;
                        font-weight:bold;
                    ">
                        Attached
                    </span>
                    """;

        } else {

            screenshotStatus = """
                    <span style="
                        background-color:#f1f5f9;
                        color:#64748b;
                        padding:6px 10px;
                        border-radius:6px;
                        font-size:11px;
                    ">
                        Not Available
                    </span>
                    """;
        }

        return """
                <tr>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        color:#0f172a;
                        font-size:13px;
                        font-weight:bold;
                    ">
                        %s
                    </td>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        text-align:center;
                        color:#15803d;
                        font-size:19px;
                        font-weight:bold;
                    ">
                        %d
                    </td>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        text-align:center;
                        color:#ea580c;
                        font-size:19px;
                        font-weight:bold;
                    ">
                        %d
                    </td>

                    <td style="
                        padding:12px;
                        border-top:1px solid #e5e7eb;
                        text-align:center;
                    ">
                        %s
                    </td>

                </tr>
                """.formatted(
                escapeHtml(folderName),
                result.getAttributeFileCount(),
                result.getNonAttributeFileCount(),
                screenshotStatus);
    }

    // ============================================================
    // GET COMPLETE HTML REPORT
    // ============================================================

    public static String getReport() {

        StringBuilder report = new StringBuilder();

        String executionTime = LocalDateTime.now()
                .format(DATE_TIME_FORMATTER);

        report.append("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Automation Test Report</title>
                </head>

                <body style="
                    margin:0;
                    padding:0;
                    background-color:#eef2f7;
                    font-family:Arial,Helvetica,sans-serif;
                    color:#1f2937;
                ">

                    <table width="100%"
                           cellpadding="0"
                           cellspacing="0"
                           style="
                               background-color:#eef2f7;
                               padding:25px 0;
                           ">

                        <tr>

                            <td align="center">

                                <table width="900"
                                       cellpadding="0"
                                       cellspacing="0"
                                       style="
                                           background-color:#ffffff;
                                           border-radius:12px;
                                           overflow:hidden;
                                       ">

                                    <!-- MAIN HEADER -->
                                    <tr>

                                        <td style="
                                            background-color:#1d4ed8;
                                            padding:28px 30px;
                                            color:#ffffff;
                                        ">

                                            <div style="
                                                font-size:28px;
                                                font-weight:bold;
                                            ">
                                                Automation Test Report
                                            </div>

                                            <div style="
                                                margin-top:7px;
                                                font-size:13px;
                                                color:#dbeafe;
                                            ">
                                                Files Check
                                            </div>

                                            <div style="
                                                margin-top:8px;
                                                font-size:12px;
                                                color:#dbeafe;
                                            ">
                                                Execution Time:
                """);

        report.append(executionTime);

        report.append("""
                                            </div>

                                        </td>

                                    </tr>

                                    <!-- SUMMARY -->
                                    <tr>

                                        <td style="
                                            padding:20px 25px;
                                            background-color:#f8fafc;
                                            border-bottom:1px solid #e5e7eb;
                                        ">

                                            <table width="100%"
                                                   cellpadding="0"
                                                   cellspacing="0">

                                                <tr>

                                                    <td width="50%"
                                                        style="padding-right:8px;">

                                                        <table width="100%"
                                                               cellpadding="0"
                                                               cellspacing="0"
                                                               style="
                                                                   border:1px solid #e2e8f0;
                                                                   background:#ffffff;
                                                                   border-radius:8px;
                                                               ">

                                                            <tr>

                                                                <td align="center"
                                                                    style="padding:16px;">

                                                                    <div style="
                                                                        font-size:27px;
                                                                        font-weight:bold;
                                                                        color:#2563eb;
                                                                    ">
                """);

        report.append(results.size());

        report.append("""
                                                                    </div>

                                                                    <div style="
                                                                        font-size:12px;
                                                                        color:#64748b;
                                                                        margin-top:4px;
                                                                    ">
                                                                        Companies Tested
                                                                    </div>

                                                                </td>

                                                            </tr>

                                                        </table>

                                                    </td>

                                                    <td width="50%"
                                                        style="padding-left:8px;">

                                                        <table width="100%"
                                                               cellpadding="0"
                                                               cellspacing="0"
                                                               style="
                                                                   border:1px solid #e2e8f0;
                                                                   background:#ffffff;
                                                                   border-radius:8px;
                                                               ">

                                                            <tr>

                                                                <td align="center"
                                                                    style="padding:16px;">

                                                                    <div style="
                                                                        font-size:27px;
                                                                        font-weight:bold;
                                                                        color:#16a34a;
                                                                    ">
                                                                        ✓
                                                                    </div>

                                                                    <div style="
                                                                        font-size:12px;
                                                                        color:#64748b;
                                                                        margin-top:4px;
                                                                    ">
                                                                        Automation Execution
                                                                    </div>

                                                                </td>

                                                            </tr>

                                                        </table>

                                                    </td>

                                                </tr>

                                            </table>

                                        </td>

                                    </tr>

                                    <!-- COMPANY REPORTS -->

                                    <tr>

                                        <td style="
                                            padding:25px;
                                            background-color:#ffffff;
                                        ">
                """);

        for (String result : results) {

            report.append(result);
        }

        report.append("""
                                        </td>

                                    </tr>

                                    <!-- FOOTER -->
                                    <tr>

                                        <td align="center"
                                            style="
                                                padding:18px;
                                                background-color:#0f172a;
                                                color:#94a3b8;
                                                font-size:11px;
                                            ">

                                            Generated automatically by
                                            Playwright Automation Framework

                                        </td>

                                    </tr>

                                </table>

                            </td>

                        </tr>

                    </table>

                </body>

                </html>
                """);

        return report.toString();
    }

    // ============================================================
    // GET SCREENSHOT PATHS
    // ============================================================

    public static List<String> getScreenshotPaths() {

        return new ArrayList<>(
                screenshotPaths);
    }

    // ============================================================
    // CLEAR REPORT
    // ============================================================

    public static void clear() {

        results.clear();
        screenshotPaths.clear();
    }

    // ============================================================
    // HTML ESCAPE
    // ============================================================

    private static String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}