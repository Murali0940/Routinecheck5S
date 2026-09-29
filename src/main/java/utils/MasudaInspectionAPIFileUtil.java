package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MasudaInspectionAPIFileUtil {

        /*
         * API modifieddate format
         *
         * Example:
         * 08/18/2026 09:30:00
         */
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        /*
         * Masuda API modifieddate is treated as UTC.
         */
        private static final ZoneOffset API_ZONE = ZoneOffset.UTC;

        /*
         * India Standard Time
         */
        private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

        /*
         * filetype = 1 -> Valid file
         */
        private static final int FILE_TYPE = 1;

        /**
         * Process Masuda Inspection files.
         *
         * Rules:
         *
         * 1. GUID must be available.
         * 2. filetype must be 1.
         * 3. modifieddate must be available.
         * 4. modifieddate is treated as UTC.
         * 5. UTC is converted to India time.
         * 6. Only today's files are counted.
         *
         * ICON RULE:
         *
         * attribute = 1
         * -> YELLOW ICON
         *
         * attribute != 1
         * -> NON YELLOW ICON
         *
         * This method processes ALL files supplied in the
         * API response. Callers are responsible for passing in
         * a JSON array that already contains ALL pages of results
         * (see MasudaInspectionTest.getAllSocketFilesJson()) --
         * this class has no knowledge of pagination.
         */
        public FileCountResult getTodayFileCount(String jsonResponse) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        /*
                         * Validate API response
                         */
                        if (!files.isArray()) {

                                throw new IllegalArgumentException(
                                                "[MASUDA] Invalid API response. "
                                                                + "Expected JSON array.");
                        }

                        /*
                         * Today's date in India
                         */
                        LocalDate today = LocalDate.now(INDIA_ZONE);

                        int totalFileCount = 0;
                        int yellowIconFileCount = 0;
                        int nonYellowIconFileCount = 0;

                        /*
                         * Process every file
                         */
                        for (JsonNode file : files) {

                                /*
                                 * ================================================
                                 * 1. GUID CHECK
                                 * ================================================
                                 */

                                String guid = file.path("guid")
                                                .asText("")
                                                .trim();

                                if (guid.isEmpty()
                                                || guid.equalsIgnoreCase("null")) {

                                        continue;
                                }

                                /*
                                 * ================================================
                                 * 2. FILE TYPE CHECK
                                 * ================================================
                                 */

                                int fileType = file.path("filetype")
                                                .asInt(-1);

                                if (fileType != FILE_TYPE) {

                                        continue;
                                }

                                /*
                                 * ================================================
                                 * 3. MODIFIED DATE CHECK
                                 * ================================================
                                 */

                                String modifiedDate = file.path("modifieddate")
                                                .asText("")
                                                .trim();

                                if (modifiedDate.isEmpty()) {

                                        continue;
                                }

                                /*
                                 * ================================================
                                 * 4. CONVERT UTC -> INDIA DATE
                                 * ================================================
                                 */

                                LocalDate fileDate = convertUtcToIndiaDate(modifiedDate);

                                if (fileDate == null) {

                                        continue;
                                }

                                /*
                                 * ================================================
                                 * 5. TODAY CHECK
                                 * ================================================
                                 */

                                if (!fileDate.equals(today)) {

                                        /*
                                         * This file is not today's file.
                                         *
                                         * Do not count it.
                                         */
                                        continue;
                                }

                                /*
                                 * ================================================
                                 * TODAY'S VALID FILE
                                 * ================================================
                                 */

                                totalFileCount++;

                                /*
                                 * ================================================
                                 * 6. ATTRIBUTE CHECK
                                 * ================================================
                                 */

                                int attribute = file.path("attribute")
                                                .asInt(-1);

                                String fileName = file.path("filename")
                                                .asText("")
                                                .trim();

                                /*
                                 * ================================================
                                 * ATTRIBUTE = 1
                                 * YELLOW ICON
                                 * ================================================
                                 */

                                if (attribute == 1) {

                                        yellowIconFileCount++;

                                        System.out.println(
                                                        "[MASUDA] YELLOW : "
                                                                        + fileName
                                                                        + " | GUID = "
                                                                        + guid
                                                                        + " | filetype = "
                                                                        + fileType
                                                                        + " | modifieddate = "
                                                                        + modifiedDate
                                                                        + " | attribute = "
                                                                        + attribute);

                                        /*
                                         * ================================================
                                         * ATTRIBUTE != 1
                                         * NON YELLOW ICON
                                         * ================================================
                                         */

                                } else {

                                        nonYellowIconFileCount++;

                                        System.out.println(
                                                        "[MASUDA] NON YELLOW : "
                                                                        + fileName
                                                                        + " | GUID = "
                                                                        + guid
                                                                        + " | filetype = "
                                                                        + fileType
                                                                        + " | modifieddate = "
                                                                        + modifiedDate
                                                                        + " | attribute = "
                                                                        + attribute);
                                }
                        }

                        /*
                         * ================================================
                         * FINAL RESULT
                         * ================================================
                         */

                        System.out.println();
                        System.out.println(
                                        "========================================");

                        System.out.println(
                                        "        MASUDA INSPECTION - TODAY");

                        System.out.println(
                                        "========================================");

                        System.out.println(
                                        "Today's India Date       : "
                                                        + today);

                        System.out.println(
                                        "Total Today Files       : "
                                                        + totalFileCount);

                        System.out.println(
                                        "Yellow Icon Files       : "
                                                        + yellowIconFileCount);

                        System.out.println(
                                        "Non Yellow Icon Files   : "
                                                        + nonYellowIconFileCount);

                        System.out.println(
                                        "Yellow + Non Yellow     : "
                                                        + (yellowIconFileCount
                                                                        + nonYellowIconFileCount));

                        System.out.println(
                                        "========================================");

                        /*
                         * ================================================
                         * RETURN RESULT
                         * ================================================
                         */

                        return new FileCountResult(
                                        yellowIconFileCount,
                                        nonYellowIconFileCount,
                                        totalFileCount,
                                        null);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "[MASUDA] Failed to process today's "
                                                        + "file information",
                                        e);
                }
        }

        /**
         * Convert API UTC modifieddate
         * to India date.
         */
        private LocalDate convertUtcToIndiaDate(
                        String modifiedDate) {

                try {

                        /*
                         * Parse API date/time
                         */
                        LocalDateTime apiDateTime = LocalDateTime.parse(
                                        modifiedDate,
                                        FORMATTER);

                        /*
                         * Treat API date/time as UTC
                         */
                        ZonedDateTime utcDateTime = apiDateTime.atZone(API_ZONE);

                        /*
                         * Convert UTC -> India
                         */
                        ZonedDateTime indiaDateTime = utcDateTime.withZoneSameInstant(
                                        INDIA_ZONE);

                        /*
                         * Return India date
                         */
                        return indiaDateTime.toLocalDate();

                } catch (Exception e) {

                        System.out.println(
                                        "[MASUDA] Invalid modifieddate: "
                                                        + modifiedDate);

                        return null;
                }
        }

        /*
         * =========================================================
         * RESULT CLASS
         * =========================================================
         */

        public static class FileCountResult {

                private final int yellowIconFileCount;

                private final int nonYellowIconFileCount;

                private final int totalFileCount;

                private final String screenshotPath;

                public FileCountResult(
                                int yellowIconFileCount,
                                int nonYellowIconFileCount,
                                int totalFileCount,
                                String screenshotPath) {

                        this.yellowIconFileCount = yellowIconFileCount;

                        this.nonYellowIconFileCount = nonYellowIconFileCount;

                        this.totalFileCount = totalFileCount;

                        this.screenshotPath = screenshotPath;
                }

                public int getYellowIconFileCount() {

                        return yellowIconFileCount;
                }

                public int getNonYellowIconFileCount() {

                        return nonYellowIconFileCount;
                }

                public int getTotalFileCount() {

                        return totalFileCount;
                }

                public String getScreenshotPath() {

                        return screenshotPath;
                }
        }
}