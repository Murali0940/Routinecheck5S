package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OhkumaAPIFileUtil {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        /*
         * Based on your API:
         *
         * attribute = 1 → Yellow Icon
         * attribute = 0 → No Yellow Icon
         */
        private static final int YELLOW_ICON = 1;

        /**
         * Get OHKUMA today's file information.
         *
         * Today's file is determined using:
         *
         * modifieddate
         *
         * Metrics:
         *
         * 1. Today's Total File Count
         * 2. Today's Yellow Icon File Count
         * 3. Today's No Yellow Icon File Count
         *
         * Total = Yellow Icon + No Yellow Icon
         *
         * @param jsonResponse   OHKUMA API response
         * @param screenshotPath Screenshot path for email report
         */
        public void ohkumaGetTodayFiles(
                        String jsonResponse,
                        String screenshotPath) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        /*
                         * Check API response.
                         */
                        if (!files.isArray() || files.isEmpty()) {

                                System.out.println("No files found in API response.");

                                TestExecutionReport.addOhkumaResult(
                                                "OHKUMA S09 SOCKET",
                                                "Today",
                                                0,
                                                0,
                                                0,
                                                screenshotPath);

                                return;
                        }

                        /*
                         * Get today's date.
                         *
                         * Example:
                         *
                         * 2026-08-17
                         */
                        LocalDate today = LocalDate.now();

                        int totalFileCount = 0;
                        int yellowIconFileCount = 0;
                        int noYellowIconFileCount = 0;

                        /*
                         * =====================================================
                         * PROCESS EACH API FILE
                         * =====================================================
                         */
                        for (JsonNode file : files) {

                                /*
                                 * Get modifieddate.
                                 *
                                 * Example:
                                 *
                                 * 08/17/2026 04:39:00
                                 */
                                String modifiedDate = file.path("modifieddate").asText("");

                                /*
                                 * Ignore records where modifieddate is missing.
                                 */
                                if (modifiedDate.isBlank()) {
                                        continue;
                                }

                                LocalDate fileDate;

                                try {

                                        fileDate = LocalDateTime
                                                        .parse(modifiedDate, FORMATTER)
                                                        .toLocalDate();

                                } catch (Exception e) {

                                        System.out.println(
                                                        "Invalid modifieddate: "
                                                                        + modifiedDate);

                                        continue;
                                }

                                /*
                                 * =================================================
                                 * ONLY TODAY'S FILES
                                 * =================================================
                                 *
                                 * Example:
                                 *
                                 * Today = 08/17/2026
                                 * modifieddate = 08/17/2026 04:39:00
                                 *
                                 * → COUNT
                                 *
                                 * modifieddate = 07/15/2026 06:18:00
                                 *
                                 * → IGNORE
                                 */
                                if (!fileDate.equals(today)) {
                                        continue;
                                }

                                /*
                                 * =================================================
                                 * TODAY'S TOTAL FILE COUNT
                                 * =================================================
                                 */
                                totalFileCount++;

                                /*
                                 * =================================================
                                 * GET ATTRIBUTE VALUE
                                 * =================================================
                                 *
                                 * attribute = 1 → Yellow Icon
                                 * attribute = 0 → No Yellow Icon
                                 */
                                int attribute = file.path("attribute").asInt(0);

                                /*
                                 * =================================================
                                 * YELLOW / NO YELLOW COUNT
                                 * =================================================
                                 */
                                if (attribute == YELLOW_ICON) {

                                        yellowIconFileCount++;

                                } else {

                                        noYellowIconFileCount++;
                                }
                        }

                        /*
                         * =====================================================
                         * PRINT RESULT
                         * =====================================================
                         */
                        printOhkumaResult(
                                        totalFileCount,
                                        yellowIconFileCount,
                                        noYellowIconFileCount);

                        /*
                         * =====================================================
                         * ADD RESULT TO EMAIL REPORT
                         * =====================================================
                         */
                        TestExecutionReport.addOhkumaResult(
                                        "OHKUMA S09 SOCKET",
                                        "Today",
                                        totalFileCount,
                                        yellowIconFileCount,
                                        noYellowIconFileCount,
                                        screenshotPath);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to process OHKUMA Today API response",
                                        e);
                }
        }

        /**
         * Print OHKUMA today's result.
         */
        private void printOhkumaResult(
                        int totalFileCount,
                        int yellowIconFileCount,
                        int noYellowIconFileCount) {

                System.out.println();
                System.out.println("========================================");
                System.out.println("           OHKUMA - TODAY");
                System.out.println("========================================");

                System.out.println(
                                "Today's Total File Count     : "
                                                + totalFileCount);

                System.out.println(
                                "Today's Yellow Icon Count    : "
                                                + yellowIconFileCount);

                System.out.println(
                                "Today's No Yellow Icon Count : "
                                                + noYellowIconFileCount);

                System.out.println("----------------------------------------");

                System.out.println(
                                "Yellow + No Yellow           : "
                                                + (yellowIconFileCount
                                                                + noYellowIconFileCount));

                System.out.println("========================================");
        }

        // ============================================================
        // GET TODAY'S FOLDER NAMES (items with NO guid, modified today)
        // ============================================================

        /**
         * Returns the list of folder names (filename) from the API response
         * where:
         * - guid is null / blank / missing → it is a FOLDER (not a file)
         * - modifieddate matches TODAY
         *
         * @param jsonResponse raw JSON string from getSocketFiles API
         * @return list of folder filenames modified today
         */
        public List<String> getTodayFolderNames(String jsonResponse) {

                List<String> todayFolders = new ArrayList<>();

                try {

                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode items = mapper.readTree(jsonResponse);

                        if (!items.isArray()) {
                                System.out.println("[OHKUMA] API response is not an array.");
                                return todayFolders;
                        }

                        LocalDate today = LocalDate.now();

                        for (JsonNode item : items) {

                                /*
                                 * FOLDERS have no guid (null, blank, or missing).
                                 * FILES have a guid value.
                                 */
                                String guid = item.path("guid").asText("").trim();

                                if (!guid.isEmpty() && !guid.equals("null")) {
                                        // This is a file, not a folder — skip
                                        continue;
                                }

                                /*
                                 * Check modifieddate == today.
                                 */
                                String modifiedDate = item.path("modifieddate").asText("");

                                if (modifiedDate.isBlank()) {
                                        continue;
                                }

                                LocalDate itemDate;

                                try {
                                        itemDate = LocalDateTime
                                                        .parse(modifiedDate, FORMATTER)
                                                        .toLocalDate();
                                } catch (Exception e) {
                                        System.out.println("[OHKUMA] Invalid modifieddate for folder: " + modifiedDate);
                                        continue;
                                }

                                if (!itemDate.equals(today)) {
                                        continue;
                                }

                                /*
                                 * It is a folder modified today — collect its filename.
                                 */
                                String folderName = item.path("filename").asText("").trim();

                                if (!folderName.isEmpty()) {
                                        todayFolders.add(folderName);
                                        System.out.println("[OHKUMA] Today's folder found: " + folderName);
                                }
                        }

                        System.out.println("[OHKUMA] Total today's folders found: " + todayFolders.size());

                } catch (Exception e) {
                        throw new RuntimeException(
                                        "[OHKUMA] Failed to extract today's folder names", e);
                }

                return todayFolders;
        }

        // ============================================================
        // COUNT FILES INSIDE A FOLDER
        // (all files returned by API after entering the folder)
        // ============================================================

        /**
         * Counts total files, yellow icon files, and non-yellow icon files
         * from the API response after entering a folder.
         *
         * All records in the response are counted (no date filter needed
         * since the folder context already filters the content).
         *
         * attribute = 1 → Yellow Icon
         * attribute = 0 → No Yellow Icon
         *
         * @param jsonResponse   raw JSON from getSocketFiles API (inside the folder)
         * @param screenshotPath screenshot taken after entering the folder
         * @param folderName     folder display name for logging
         * @return FileCountResult containing all counts and screenshot path
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

                        if (!items.isArray() || items.isEmpty()) {
                                System.out.println("[OHKUMA] No files found inside folder: " + folderName);
                                return new FileCountResult(0, 0, 0, screenshotPath);
                        }

                        /*
                         * TODAY's date — only count files modified today.
                         */
                        LocalDate today = LocalDate.now();

                        for (JsonNode item : items) {

                                /*
                                 * Count only files (items with a guid).
                                 * Items without a guid are sub-folders — skip them.
                                 */
                                String guid = item.path("guid").asText("").trim();

                                if (guid.isEmpty() || guid.equals("null")) {
                                        continue;
                                }

                                /*
                                 * FILTER: only TODAY's files
                                 * Check modifieddate == today.
                                 */
                                String modifiedDate = item.path("modifieddate").asText("").trim();

                                if (modifiedDate.isBlank()) {
                                        continue;
                                }

                                LocalDate fileDate;

                                try {
                                        fileDate = LocalDateTime
                                                        .parse(modifiedDate, FORMATTER)
                                                        .toLocalDate();
                                } catch (Exception e) {
                                        System.out.println("[OHKUMA] Invalid modifieddate: " + modifiedDate);
                                        continue;
                                }

                                if (!fileDate.equals(today)) {
                                        continue;
                                }

                                /*
                                 * TODAY's file — count it.
                                 */
                                totalFileCount++;

                                int attribute = item.path("attribute").asInt(0);

                                if (attribute == YELLOW_ICON) {
                                        yellowIconFileCount++;
                                } else {
                                        noYellowIconFileCount++;
                                }
                        }

                        // ----------------------------------------
                        // PRINT RESULT
                        // ----------------------------------------

                        System.out.println();
                        System.out.println("========================================");
                        System.out.println(" OHKUMA FOLDER : " + folderName);
                        System.out.println("========================================");
                        System.out.println("Today's Total File Count     : " + totalFileCount);
                        System.out.println("Today's Yellow Icon Files    : " + yellowIconFileCount);
                        System.out.println("Today's No Yellow Icon Files : " + noYellowIconFileCount);
                        System.out.println("========================================");

                        return new FileCountResult(
                                        yellowIconFileCount,
                                        noYellowIconFileCount,
                                        totalFileCount,
                                        screenshotPath);

                } catch (Exception e) {
                        throw new RuntimeException(
                                        "[OHKUMA] Failed to count files inside folder: " + folderName, e);
                }
        }
}