package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MasudaAPIFileUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    private static final ZoneOffset API_ZONE = ZoneOffset.UTC;

    private static final ZoneId APPLICATION_ZONE = ZoneId.of("Asia/Kolkata");

    private static final int YELLOW_ICON = 1;

    // ============================================================
    // GET TODAY'S FILE INFORMATION
    // ============================================================

    public void masudaGetTodayFiles(
            String jsonResponse,
            String screenshotPath) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode files = mapper.readTree(jsonResponse);

            /*
             * ========================================================
             * VALIDATE API RESPONSE
             * ========================================================
             */

            if (!files.isArray() || files.isEmpty()) {

                System.out.println(
                        "[MASUDA] No files found in API response.");

                TestExecutionReport.addResult(
                        "MASUDA S09 SOCKET",
                        "Today",
                        0,
                        0,
                        0,
                        screenshotPath);

                return;
            }

            LocalDate today = LocalDate.now(APPLICATION_ZONE);

            int totalFileCount = 0;
            int yellowIconFileCount = 0;
            int noYellowIconFileCount = 0;

            /*
             * ========================================================
             * PROCESS EACH API RECORD
             * ========================================================
             */

            for (JsonNode file : files) {

                /*
                 * Get modifieddate.
                 *
                 * Example:
                 *
                 * 08/17/2026 23:50:00
                 */

                String modifiedDate = file.path("modifieddate")
                        .asText("")
                        .trim();

                /*
                 * Ignore records without modifieddate.
                 */

                if (modifiedDate.isBlank()) {
                    continue;
                }

                /*
                 * Convert API UTC date to India date.
                 */

                LocalDate fileDate = convertApiDateToIndiaDate(modifiedDate);

                /*
                 * Ignore invalid date.
                 */

                if (fileDate == null) {
                    continue;
                }

                /*
                 * ====================================================
                 * ONLY TODAY'S FILES
                 * ====================================================
                 */

                if (!fileDate.equals(today)) {
                    continue;
                }

                /*
                 * Today's file.
                 */

                totalFileCount++;

                /*
                 * Get attribute.
                 *
                 * attribute = 1 -> Yellow
                 * attribute = 0 -> No Yellow
                 */

                int attribute = file.path("attribute").asInt(0);

                if (attribute == YELLOW_ICON) {

                    yellowIconFileCount++;

                } else {

                    noYellowIconFileCount++;
                }
            }

            /*
             * ========================================================
             * PRINT RESULT
             * ========================================================
             */

            printMasudaResult(
                    totalFileCount,
                    yellowIconFileCount,
                    noYellowIconFileCount);

            /*
             * ========================================================
             * ADD RESULT TO EMAIL REPORT
             * ========================================================
             */

            TestExecutionReport.addResult(
                    "MASUDA",
                    "Today",
                    totalFileCount,
                    yellowIconFileCount,
                    noYellowIconFileCount,
                    screenshotPath);

        } catch (Exception e) {

            throw new RuntimeException(
                    "[MASUDA] Failed to process today's API response",
                    e);
        }
    }

    // ============================================================
    // GET TODAY'S FOLDER NAMES
    // ============================================================

    /**
     * Returns folder names from MASUDA API.
     *
     * Folder identification:
     *
     * guid is blank / null / missing
     *
     * Today's folder is determined using:
     *
     * modifieddate
     *
     * modifieddate is converted:
     *
     * UTC -> Asia/Kolkata
     *
     * Example:
     *
     * API:
     * 08/17/2026 23:50:00 UTC
     *
     * India:
     * 08/18/2026 05:20:00 IST
     *
     * Therefore this folder belongs to TODAY.
     *
     * @param jsonResponse Raw JSON from getSocketFiles API
     * @return List of today's folder names
     */

    public List<String> getTodayFolderNames(
            String jsonResponse) {

        List<String> todayFolders = new ArrayList<>();

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode items = mapper.readTree(jsonResponse);

            /*
             * ========================================================
             * VALIDATE API RESPONSE
             * ========================================================
             */

            if (!items.isArray()) {

                System.out.println(
                        "[MASUDA] API response is not an array.");

                return todayFolders;
            }

            /*
             * Today's date in India.
             */

            LocalDate today = LocalDate.now(APPLICATION_ZONE);

            System.out.println();

            System.out.println(
                    "[MASUDA] Today's India date: "
                            + today);

            /*
             * ========================================================
             * PROCESS EACH API RECORD
             * ========================================================
             */

            for (JsonNode item : items) {

                /*
                 * ====================================================
                 * CHECK WHETHER RECORD IS A FOLDER
                 * ====================================================
                 *
                 * Folder:
                 *
                 * guid = ""
                 *
                 * File:
                 *
                 * guid has value
                 */

                String guid = item.path("guid")
                        .asText("")
                        .trim();

                /*
                 * File -> skip.
                 */

                if (!guid.isEmpty()
                        && !guid.equalsIgnoreCase("null")) {

                    continue;
                }

                /*
                 * ====================================================
                 * GET FOLDER NAME
                 * ====================================================
                 */

                String folderName = item.path("filename")
                        .asText("")
                        .trim();

                if (folderName.isEmpty()) {
                    continue;
                }

                /*
                 * ====================================================
                 * GET MODIFIED DATE
                 * ====================================================
                 */

                String modifiedDate = item.path("modifieddate")
                        .asText("")
                        .trim();

                /*
                 * Folder without modifieddate.
                 *
                 * We cannot determine whether it is today's folder.
                 */

                if (modifiedDate.isBlank()) {

                    System.out.println(
                            "[MASUDA] Folder skipped because "
                                    + "modifieddate is missing: "
                                    + folderName);

                    continue;
                }

                /*
                 * Convert UTC -> India.
                 */

                LocalDate folderDate = convertApiDateToIndiaDate(
                        modifiedDate);

                /*
                 * Invalid date.
                 */

                if (folderDate == null) {
                    continue;
                }

                /*
                 * ====================================================
                 * DEBUG LOG
                 * ====================================================
                 */

                System.out.println(
                        "[MASUDA] Folder: "
                                + folderName
                                + " | API modifieddate: "
                                + modifiedDate
                                + " | India date: "
                                + folderDate);

                /*
                 * ====================================================
                 * CHECK TODAY
                 * ====================================================
                 */

                if (!folderDate.equals(today)) {
                    continue;
                }

                /*
                 * ====================================================
                 * TODAY'S FOLDER FOUND
                 * ====================================================
                 */

                todayFolders.add(folderName);

                System.out.println(
                        "[MASUDA] Today's folder found: "
                                + folderName);
            }

            /*
             * ========================================================
             * FINAL FOLDER COUNT
             * ========================================================
             */

            System.out.println(
                    "[MASUDA] Total today's folders found: "
                            + todayFolders.size());

        } catch (Exception e) {

            throw new RuntimeException(
                    "[MASUDA] Failed to extract today's folder names",
                    e);
        }

        return todayFolders;
    }

    // ============================================================
    // COUNT FILES INSIDE A FOLDER
    // ============================================================

    /**
     * Counts today's files inside a folder.
     *
     * Folder records:
     *
     * guid blank -> skipped
     *
     * File records:
     *
     * guid exists -> processed
     *
     * Today's file is determined using modifieddate.
     *
     * modifieddate:
     *
     * UTC -> Asia/Kolkata
     *
     * attribute:
     *
     * 1 -> Yellow Icon
     * 0 -> No Yellow Icon
     *
     * @param jsonResponse   Raw JSON from getSocketFiles API
     * @param screenshotPath Screenshot taken after entering folder
     * @param folderName     Folder name
     *
     * @return FileCountResult
     */

    public FileCountResult countFilesInFolder(
            String jsonResponse,
            String screenshotPath,
            String folderName) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode items = mapper.readTree(jsonResponse);

            int totalFileCount = 0;
            int yellowIconFileCount = 0;
            int noYellowIconFileCount = 0;

            /*
             * ========================================================
             * VALIDATE API RESPONSE
             * ========================================================
             */

            if (!items.isArray() || items.isEmpty()) {

                System.out.println(
                        "[MASUDA] No files found inside folder: "
                                + folderName);

                return new FileCountResult(
                        0,
                        0,
                        0,
                        screenshotPath);
            }

            /*
             * Today's date in India.
             */

            LocalDate today = LocalDate.now(APPLICATION_ZONE);

            /*
             * ========================================================
             * PROCESS EACH RECORD
             * ========================================================
             */

            for (JsonNode item : items) {

                /*
                 * ====================================================
                 * IDENTIFY FILE
                 * ====================================================
                 *
                 * guid exists -> File
                 *
                 * guid missing -> Folder
                 */

                String guid = item.path("guid")
                        .asText("")
                        .trim();

                /*
                 * Skip sub-folders.
                 */

                if (guid.isEmpty()
                        || guid.equalsIgnoreCase("null")) {

                    continue;
                }

                /*
                 * ====================================================
                 * GET FILE NAME
                 * ====================================================
                 */

                String fileName = item.path("filename")
                        .asText("")
                        .trim();

                /*
                 * ====================================================
                 * GET MODIFIED DATE
                 * ====================================================
                 */

                String modifiedDate = item.path("modifieddate")
                        .asText("")
                        .trim();

                /*
                 * Ignore files without modifieddate.
                 */

                if (modifiedDate.isBlank()) {
                    continue;
                }

                /*
                 * Convert UTC -> India.
                 */

                LocalDate fileDate = convertApiDateToIndiaDate(
                        modifiedDate);

                /*
                 * Invalid date.
                 */

                if (fileDate == null) {
                    continue;
                }

                /*
                 * ====================================================
                 * DEBUG LOG
                 * ====================================================
                 */

                System.out.println(
                        "[MASUDA] File: "
                                + fileName
                                + " | API modifieddate: "
                                + modifiedDate
                                + " | India date: "
                                + fileDate);

                /*
                 * ====================================================
                 * ONLY TODAY'S FILES
                 * ====================================================
                 */

                if (!fileDate.equals(today)) {
                    continue;
                }

                /*
                 * ====================================================
                 * TOTAL FILE
                 * ====================================================
                 */

                totalFileCount++;

                /*
                 * ====================================================
                 * GET ATTRIBUTE
                 * ====================================================
                 */

                int attribute = item.path("attribute")
                        .asInt(0);

                /*
                 * ====================================================
                 * YELLOW / NO YELLOW
                 * ====================================================
                 */

                if (attribute == YELLOW_ICON) {

                    yellowIconFileCount++;

                    System.out.println(
                            "[MASUDA] YELLOW file found: "
                                    + fileName);

                } else {

                    noYellowIconFileCount++;

                    System.out.println(
                            "[MASUDA] NO YELLOW file found: "
                                    + fileName);
                }
            }

            /*
             * ========================================================
             * PRINT RESULT
             * ========================================================
             */

            System.out.println();

            System.out.println(
                    "========================================");

            System.out.println(
                    " MASUDA FOLDER : "
                            + folderName);

            System.out.println(
                    "========================================");

            System.out.println(
                    "Today's Total File Count     : "
                            + totalFileCount);

            System.out.println(
                    "Today's Yellow Icon Files    : "
                            + yellowIconFileCount);

            System.out.println(
                    "Today's No Yellow Icon Files : "
                            + noYellowIconFileCount);

            System.out.println(
                    "Yellow + No Yellow           : "
                            + (yellowIconFileCount
                                    + noYellowIconFileCount));

            System.out.println(
                    "========================================");

            /*
             * ========================================================
             * FILE COUNT RESULT
             * ========================================================
             *
             * FileCountResult constructor order:
             *
             * 1. attributeFileCount
             * 2. nonAttributeFileCount
             * 3. totalFileCount
             * 4. screenshotPath
             *
             * Therefore:
             *
             * yellow
             * noYellow
             * total
             * screenshot
             */

            return new FileCountResult(
                    yellowIconFileCount,
                    noYellowIconFileCount,
                    totalFileCount,
                    screenshotPath);

        } catch (Exception e) {

            throw new RuntimeException(
                    "[MASUDA] Failed to count files inside folder: "
                            + folderName,
                    e);
        }
    }

    // ============================================================
    // CONVERT MASUDA API DATE
    // ============================================================

    /**
     * Converts MASUDA API date from UTC to India date.
     *
     * Example:
     *
     * API:
     * 08/17/2026 23:50:00
     *
     * UTC:
     * 08/17/2026 23:50:00
     *
     * India:
     * 08/18/2026 05:20:00
     *
     * Returned date:
     * 08/18/2026
     *
     * @param apiDate API date string
     * @return converted India LocalDate, or null if invalid
     */

    private LocalDate convertApiDateToIndiaDate(
            String apiDate) {

        try {

            LocalDateTime localDateTime = LocalDateTime.parse(
                    apiDate,
                    FORMATTER);

            /*
             * Treat API timestamp as UTC.
             */

            ZonedDateTime utcDateTime = localDateTime.atZone(API_ZONE);

            /*
             * Convert UTC -> Asia/Kolkata.
             */

            ZonedDateTime indiaDateTime = utcDateTime.withZoneSameInstant(
                    APPLICATION_ZONE);

            /*
             * Return India date.
             */

            return indiaDateTime.toLocalDate();

        } catch (Exception e) {

            System.out.println(
                    "[MASUDA] Invalid API date: "
                            + apiDate);

            return null;
        }
    }

    // ============================================================
    // PRINT MASUDA RESULT
    // ============================================================

    /**
     * Prints overall MASUDA today's result.
     */

    private void printMasudaResult(
            int totalFileCount,
            int yellowIconFileCount,
            int noYellowIconFileCount) {

        System.out.println();

        System.out.println(
                "========================================");

        System.out.println(
                "           MASUDA - TODAY");

        System.out.println(
                "========================================");

        System.out.println(
                "Today's Total File Count     : "
                        + totalFileCount);

        System.out.println(
                "Today's Yellow Icon Count    : "
                        + yellowIconFileCount);

        System.out.println(
                "Today's No Yellow Icon Count : "
                        + noYellowIconFileCount);

        System.out.println(
                "----------------------------------------");

        System.out.println(
                "Yellow + No Yellow           : "
                        + (yellowIconFileCount
                                + noYellowIconFileCount));

        System.out.println(
                "========================================");
    }
}