package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class APIFileUtil {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        /**
         * Company-specific attribute values.
         *
         * OAKSYSTEM -> 7
         * NISINOSEIKISS -> 4
         * OHKUMA -> 1
         * SANMATSU -> 1
         */
        private int getCompanyAttribute(String company) {

                if (company.equalsIgnoreCase("OAKSYSTEM")) {
                        return 7;
                }

                if (company.equalsIgnoreCase("NISINOSEIKISS")) {
                        return 4;
                }

                if (company.equalsIgnoreCase("OHKUMA")) {
                        return 1;
                }

                if (company.equalsIgnoreCase("SANMATSU")) {
                        return 1;
                }

                throw new IllegalArgumentException(
                                "Unsupported company: " + company);
        }

        /**
         * Existing method.
         *
         * Gets Attribute and Non-Attribute file counts
         * for Today or Yesterday.
         *
         * This method is kept for your existing functionality.
         */
        public FileCountResult getFilesByDay(
                        String jsonResponse,
                        String dayFilter,
                        String company) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        if (!files.isArray()) {

                                throw new IllegalArgumentException(
                                                "Invalid API response. Expected JSON array.");
                        }

                        LocalDate today = LocalDate.now();
                        LocalDate yesterday = today.minusDays(1);

                        int companyAttribute = getCompanyAttribute(company);

                        int attributeFileCount = 0;
                        int nonAttributeFileCount = 0;

                        System.out.println();
                        System.out.println("======================================");
                        System.out.println("COMPANY : " + company);
                        System.out.println("DAY     : " + dayFilter);
                        System.out.println("======================================");

                        for (JsonNode file : files) {

                                String fileName = file
                                                .path("filename")
                                                .asText();

                                String date = file.has("date") && !file.path("date").asText().isBlank()
                                                ? file.path("date").asText().trim()
                                                : file.path("modifieddate").asText("").trim();

                                if (date.isBlank()) {
                                        continue;
                                }

                                int attribute = file
                                                .path("attribute")
                                                .asInt();

                                LocalDate fileDate;
                                try {
                                        fileDate = LocalDateTime
                                                        .parse(date, FORMATTER)
                                                        .toLocalDate();
                                } catch (Exception e) {
                                        System.out.println("Invalid date format: " + date);
                                        continue;
                                }

                                boolean matchedDate;

                                if (dayFilter.equalsIgnoreCase("Today")) {

                                        matchedDate = fileDate.equals(today);

                                } else if (dayFilter.equalsIgnoreCase("Yesterday")) {

                                        matchedDate = fileDate.equals(yesterday);

                                } else {

                                        throw new IllegalArgumentException(
                                                        "Unsupported day filter: " + dayFilter);
                                }

                                /*
                                 * Process only requested day.
                                 */
                                if (!matchedDate) {
                                        continue;
                                }

                                /*
                                 * Attribute Icon File.
                                 */
                                if (attribute == companyAttribute) {

                                        attributeFileCount++;

                                } else {

                                        /*
                                         * Non-Attribute Icon File.
                                         */
                                        nonAttributeFileCount++;

                                        System.out.println(
                                                        "Non-Attribute File : "
                                                                        + fileName);
                                }
                        }

                        /*
                         * Calculate total.
                         */
                        int totalFileCount = attributeFileCount + nonAttributeFileCount;

                        System.out.println(
                                        "--------------------------------------");

                        System.out.println(
                                        "Attribute Icon Files     : "
                                                        + attributeFileCount);

                        System.out.println(
                                        "Non-Attribute Icon Files : "
                                                        + nonAttributeFileCount);

                        System.out.println(
                                        "Total File Count         : "
                                                        + totalFileCount);

                        System.out.println(
                                        "======================================");

                        /*
                         * Return result.
                         *
                         * Screenshot path is not handled here.
                         */
                        return new FileCountResult(
                                        attributeFileCount,
                                        nonAttributeFileCount,
                                        totalFileCount,
                                        null);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to process getSocketFiles API response",
                                        e);
                }
        }

        // ============================================================
        // OAKSYSTEM / SANMATSU
        // ============================================================

        /**
         * Get today's file count information.
         *
         * 1. Total File Count
         * 2. Attribute Icon Files
         * 3. Non-Attribute Icon Files
         *
         * Supported:
         *
         * OAKSYSTEM -> attribute 7
         * SANMATSU -> attribute 1
         */
        public FileCountResult getTodayFileCount(
                        String jsonResponse,
                        String company) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        if (!files.isArray()) {

                                throw new IllegalArgumentException(
                                                "Invalid API response. Expected JSON array.");
                        }

                        LocalDate today = LocalDate.now();

                        int companyAttribute = getCompanyAttribute(company);

                        int totalFileCount = 0;
                        int attributeIconFileCount = 0;
                        int nonAttributeIconFileCount = 0;

                        /*
                         * Process API records.
                         */
                        for (JsonNode file : files) {

                                String date = file.has("date") && !file.path("date").asText().isBlank()
                                                ? file.path("date").asText().trim()
                                                : file.path("modifieddate").asText("").trim();

                                if (date.isBlank()) {
                                        continue;
                                }

                                int attribute = file
                                                .path("attribute")
                                                .asInt();

                                LocalDate fileDate;
                                try {
                                        fileDate = LocalDateTime
                                                        .parse(date, FORMATTER)
                                                        .toLocalDate();
                                } catch (Exception e) {
                                        System.out.println("Invalid date format in API file record: " + date);
                                        continue;
                                }

                                /*
                                 * Process only today's files.
                                 */
                                if (!fileDate.equals(today)) {
                                        continue;
                                }

                                /*
                                 * 1. Total File Count.
                                 */
                                totalFileCount++;

                                /*
                                 * 2. Attribute Icon Files.
                                 */
                                if (attribute == companyAttribute) {

                                        attributeIconFileCount++;

                                } else {

                                        /*
                                         * 3. Non-Attribute Icon Files.
                                         */
                                        nonAttributeIconFileCount++;
                                }
                        }

                        System.out.println();
                        System.out.println("======================================");
                        System.out.println("COMPANY : " + company);
                        System.out.println("DAY     : Today");
                        System.out.println("======================================");

                        System.out.println(
                                        "Total File Count         : "
                                                        + totalFileCount);

                        System.out.println(
                                        "Attribute Icon Files     : "
                                                        + attributeIconFileCount);

                        System.out.println(
                                        "Non-Attribute Icon Files : "
                                                        + nonAttributeIconFileCount);

                        System.out.println(
                                        "======================================");

                        /*
                         * Return result.
                         *
                         * Screenshot path is handled separately.
                         */
                        return new FileCountResult(
                                        attributeIconFileCount,
                                        nonAttributeIconFileCount,
                                        totalFileCount,
                                        null);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to process today's "
                                                        + company
                                                        + " file information",
                                        e);
                }
        }
}