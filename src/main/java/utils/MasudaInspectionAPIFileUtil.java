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

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        /** Masuda API modifieddate is treated as UTC. */
        private static final ZoneOffset API_ZONE = ZoneOffset.UTC;

        /** India Standard Time. */
        private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

        /** filetype = 1 means a valid file. */
        private static final int VALID_FILE_TYPE = 1;

        /** attribute = 1 means Yellow icon. */
        private static final int YELLOW_ATTRIBUTE = 1;

        /**
         * Processes all files in the supplied JSON array and returns today's counts.
         *
         * @param jsonResponse JSON array returned by the Masuda Inspection API
         * @return today's Yellow, Non-Yellow and total file counts
         * @throws IllegalArgumentException if the response is null, blank, invalid,
         *                                  or is not a JSON array
         */
        public FileCountResult getTodayFileCount(String jsonResponse) {

                if (jsonResponse == null || jsonResponse.isBlank()) {
                        throw new IllegalArgumentException(
                                        "[MASUDA] API response must not be null or blank.");
                }

                try {
                        JsonNode files = OBJECT_MAPPER.readTree(jsonResponse);

                        if (files == null || !files.isArray()) {
                                throw new IllegalArgumentException(
                                                "[MASUDA] Invalid API response. Expected JSON array.");
                        }

                        LocalDate today = LocalDate.now(INDIA_ZONE);

                        int totalFileCount = 0;
                        int yellowIconFileCount = 0;
                        int nonYellowIconFileCount = 0;

                        for (JsonNode file : files) {

                                if (file == null || !file.isObject()) {
                                        continue;
                                }

                                // 1. GUID check
                                String guid = getText(file, "guid");
                                if (guid.isEmpty() || "null".equalsIgnoreCase(guid)) {
                                        continue;
                                }

                                // 2. File type check
                                int fileType = file.path("filetype").asInt(-1);
                                if (fileType != VALID_FILE_TYPE) {
                                        continue;
                                }

                                // 3. Modified date check
                                String modifiedDate = getText(file, "modifieddate");
                                if (modifiedDate.isEmpty() || "null".equalsIgnoreCase(modifiedDate)) {
                                        continue;
                                }

                                // 4. UTC -> India date
                                LocalDate fileDate = convertUtcToIndiaDate(modifiedDate);
                                if (fileDate == null) {
                                        continue;
                                }

                                // 5. Today's file check
                                if (!today.equals(fileDate)) {
                                        continue;
                                }

                                // Valid file for today
                                totalFileCount++;

                                // 6. Attribute / icon check
                                int attribute = file.path("attribute").asInt(-1);
                                String fileName = getText(file, "filename");

                                if (attribute == YELLOW_ATTRIBUTE) {
                                        yellowIconFileCount++;
                                        logFile("YELLOW", fileName, guid, fileType, modifiedDate, attribute);
                                } else {
                                        nonYellowIconFileCount++;
                                        logFile("NON YELLOW", fileName, guid, fileType, modifiedDate, attribute);
                                }
                        }

                        printSummary(today, totalFileCount, yellowIconFileCount, nonYellowIconFileCount);

                        return new FileCountResult(
                                        yellowIconFileCount,
                                        nonYellowIconFileCount,
                                        totalFileCount,
                                        null);

                } catch (IllegalArgumentException e) {
                        throw e;
                } catch (Exception e) {
                        throw new RuntimeException(
                                        "[MASUDA] Failed to process today's file information", e);
                }
        }

        /**
         * Converts the API modifieddate from UTC to India time and returns the
         * resulting India date.
         */
        private LocalDate convertUtcToIndiaDate(String modifiedDate) {
                try {
                        LocalDateTime apiDateTime = LocalDateTime.parse(modifiedDate, FORMATTER);
                        ZonedDateTime utcDateTime = apiDateTime.atZone(API_ZONE);
                        ZonedDateTime indiaDateTime = utcDateTime.withZoneSameInstant(INDIA_ZONE);
                        return indiaDateTime.toLocalDate();
                } catch (Exception e) {
                        System.out.println(
                                        "[MASUDA] Invalid modifieddate: " + modifiedDate);
                        return null;
                }
        }

        private String getText(JsonNode node, String fieldName) {
                JsonNode value = node.path(fieldName);
                if (value.isMissingNode() || value.isNull()) {
                        return "";
                }
                return value.asText("").trim();
        }

        private void logFile(
                        String iconType,
                        String fileName,
                        String guid,
                        int fileType,
                        String modifiedDate,
                        int attribute) {

                System.out.println(
                                "[MASUDA] " + iconType
                                                + " : " + fileName
                                                + " | GUID = " + guid
                                                + " | filetype = " + fileType
                                                + " | modifieddate = " + modifiedDate
                                                + " | attribute = " + attribute);
        }

        private void printSummary(
                        LocalDate today,
                        int totalFileCount,
                        int yellowIconFileCount,
                        int nonYellowIconFileCount) {

                System.out.println();
                System.out.println("========================================");
                System.out.println("        MASUDA INSPECTION - TODAY");
                System.out.println("========================================");
                System.out.println("Today's India Date       : " + today);
                System.out.println("Total Today Files       : " + totalFileCount);
                System.out.println("Yellow Icon Files       : " + yellowIconFileCount);
                System.out.println("Non Yellow Icon Files   : " + nonYellowIconFileCount);
                System.out.println("Yellow + Non Yellow     : "
                                + (yellowIconFileCount + nonYellowIconFileCount));
                System.out.println("========================================");
        }

        /** Result object containing today's file counts. */
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

                        if (yellowIconFileCount < 0
                                        || nonYellowIconFileCount < 0
                                        || totalFileCount < 0) {
                                throw new IllegalArgumentException("File counts cannot be negative.");
                        }

                        if (yellowIconFileCount + nonYellowIconFileCount != totalFileCount) {
                                throw new IllegalArgumentException(
                                                "Yellow + Non-Yellow count must equal total file count.");
                        }

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