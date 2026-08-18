package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OakSystemFileAPIUtil {

        /*
         * API modifieddate format.
         *
         * Example:
         * 08/18/2026 09:30:00
         */
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        /*
         * OakSystem API modifieddate is treated as UTC.
         */
        private static final ZoneOffset API_ZONE = ZoneOffset.UTC;

        /*
         * India Standard Time = UTC +05:30
         */
        private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

        /*
         * OakSystem file type.
         *
         * filetype = 1 -> Valid file
         */
        private static final int FILE_TYPE = 1;

        /**
         * Counts today's OakSystem files.
         *
         * A record is considered a valid today's file when:
         *
         * 1. guid is available
         * 2. filetype = 1
         * 3. modifieddate is available
         * 4. modifieddate is treated as UTC
         * 5. UTC is converted to India time (+05:30)
         * 6. Converted India date is today
         *
         * Icon classification:
         *
         * attribute = 7 -> Yellow
         * attribute = 6 -> Yellow
         * anything else -> Non Yellow
         *
         * @param jsonResponse OakSystem API response
         * @return FileCountResult
         */
        public FileCountResult getTodayFileCount(
                        String jsonResponse) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        /*
                         * ========================================================
                         * VALIDATE API RESPONSE
                         * ========================================================
                         */
                        if (!files.isArray()) {

                                throw new IllegalArgumentException(
                                                "[OAKSYSTEM] Invalid API response. "
                                                                + "Expected JSON array.");
                        }

                        /*
                         * ========================================================
                         * TODAY'S DATE IN INDIA
                         * ========================================================
                         */
                        LocalDate today = LocalDate.now(INDIA_ZONE);

                        int totalFileCount = 0;
                        int yellowIconFileCount = 0;
                        int nonYellowIconFileCount = 0;

                        /*
                         * ========================================================
                         * PROCESS API RECORDS
                         * ========================================================
                         */
                        for (JsonNode file : files) {

                                /*
                                 * ----------------------------------------------------
                                 * 1. GUID CHECK
                                 * ----------------------------------------------------
                                 *
                                 * A valid file must have a GUID.
                                 */
                                String guid = file.path("guid")
                                                .asText("")
                                                .trim();

                                if (guid.isEmpty()
                                                || guid.equalsIgnoreCase("null")) {

                                        continue;
                                }

                                /*
                                 * ----------------------------------------------------
                                 * 2. FILETYPE CHECK
                                 * ----------------------------------------------------
                                 *
                                 * filetype = 1 -> Valid file
                                 */
                                int fileType = file.path("filetype")
                                                .asInt(-1);

                                if (fileType != FILE_TYPE) {

                                        continue;
                                }

                                /*
                                 * ----------------------------------------------------
                                 * 3. MODIFIED DATE CHECK
                                 * ----------------------------------------------------
                                 */
                                String modifiedDate = file.path("modifieddate")
                                                .asText("")
                                                .trim();

                                if (modifiedDate.isEmpty()) {

                                        continue;
                                }

                                /*
                                 * ----------------------------------------------------
                                 * 4. UTC -> INDIA DATE
                                 * ----------------------------------------------------
                                 */
                                LocalDate fileDate = convertUtcToIndiaDate(
                                                modifiedDate);

                                if (fileDate == null) {

                                        continue;
                                }

                                /*
                                 * ----------------------------------------------------
                                 * 5. TODAY CHECK
                                 * ----------------------------------------------------
                                 */
                                if (!fileDate.equals(today)) {

                                        continue;
                                }

                                /*
                                 * ====================================================
                                 * VALID TODAY'S FILE
                                 * ====================================================
                                 */
                                totalFileCount++;

                                /*
                                 * ----------------------------------------------------
                                 * 6. ATTRIBUTE CHECK
                                 * ----------------------------------------------------
                                 *
                                 * attribute = 7 -> Yellow
                                 * attribute = 6 -> Yellow
                                 *
                                 * anything else -> Non Yellow
                                 */
                                int attribute = file.path("attribute")
                                                .asInt(-1);

                                String fileName = file.path("filename")
                                                .asText("")
                                                .trim();

                                /*
                                 * ====================================================
                                 * YELLOW ICON
                                 * ====================================================
                                 */
                                if (attribute == 7
                                                || attribute == 6) {

                                        yellowIconFileCount++;

                                        System.out.println(
                                                        "[OAKSYSTEM] YELLOW : "
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
                                         * ====================================================
                                         * NON YELLOW ICON
                                         * ====================================================
                                         */
                                } else {

                                        nonYellowIconFileCount++;

                                        System.out.println(
                                                        "[OAKSYSTEM] NON YELLOW : "
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
                         * ========================================================
                         * FINAL RESULT
                         * ========================================================
                         */
                        System.out.println();

                        System.out.println(
                                        "========================================");

                        System.out.println(
                                        "       OAKSYSTEM - TODAY");

                        System.out.println(
                                        "========================================");

                        System.out.println(
                                        "Today's India Date       : "
                                                        + today);

                        System.out.println(
                                        "Total File Count         : "
                                                        + totalFileCount);

                        System.out.println(
                                        "Yellow Icon Files        : "
                                                        + yellowIconFileCount);

                        System.out.println(
                                        "Non Yellow Icon Files    : "
                                                        + nonYellowIconFileCount);

                        System.out.println(
                                        "Yellow + Non Yellow      : "
                                                        + (yellowIconFileCount
                                                                        + nonYellowIconFileCount));

                        System.out.println(
                                        "========================================");

                        /*
                         * ========================================================
                         * RETURN RESULT
                         * ========================================================
                         *
                         * FileCountResult constructor:
                         *
                         * 1. Yellow count
                         * 2. Non Yellow count
                         * 3. Total count
                         * 4. Screenshot path
                         */
                        return new FileCountResult(
                                        yellowIconFileCount,
                                        nonYellowIconFileCount,
                                        totalFileCount,
                                        null);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "[OAKSYSTEM] Failed to process today's "
                                                        + "file information",
                                        e);
                }
        }

        /**
         * Converts API modifieddate from UTC
         * to India time and returns the India date.
         *
         * Example:
         *
         * API UTC:
         * 08/17/2026 23:50:00
         *
         * India:
         * 08/18/2026 05:20:00
         */
        private LocalDate convertUtcToIndiaDate(
                        String modifiedDate) {

                try {

                        /*
                         * Parse API date/time.
                         */
                        LocalDateTime apiDateTime = LocalDateTime.parse(
                                        modifiedDate,
                                        FORMATTER);

                        /*
                         * Treat API date/time as UTC.
                         */
                        ZonedDateTime utcDateTime = apiDateTime.atZone(
                                        API_ZONE);

                        /*
                         * Convert UTC -> Asia/Kolkata.
                         *
                         * UTC +05:30
                         */
                        ZonedDateTime indiaDateTime = utcDateTime.withZoneSameInstant(
                                        INDIA_ZONE);

                        /*
                         * Return India date.
                         */
                        return indiaDateTime.toLocalDate();

                } catch (Exception e) {

                        System.out.println(
                                        "[OAKSYSTEM] Invalid modifieddate: "
                                                        + modifiedDate);

                        return null;
                }
        }
}