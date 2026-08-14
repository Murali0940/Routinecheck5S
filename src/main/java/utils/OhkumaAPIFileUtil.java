package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OhkumaAPIFileUtil {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        private static final int ATTRIBUTE_ICON = 1;

        /**
         * Get OHKUMA today's file information.
         *
         * Metrics:
         * 1. Total File Count
         * 2. Attribute Icon File Count
         * 3. Non-Attribute File Count
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

                        if (!files.isArray() || files.isEmpty()) {

                                System.out.println("No files found in API response.");
                                return;
                        }

                        LocalDate today = LocalDate.now();

                        int totalFileCount = 0;
                        int attributeIconFileCount = 0;
                        int nonAttributeFileCount = 0;

                        /*
                         * Process today's API records.
                         */
                        for (JsonNode file : files) {

                                String date = file.path("date").asText();

                                LocalDate fileDate = LocalDateTime
                                                .parse(date, FORMATTER)
                                                .toLocalDate();

                                /*
                                 * Ignore records that are not from today.
                                 */
                                if (!fileDate.equals(today)) {
                                        continue;
                                }

                                totalFileCount++;

                                int attribute = file
                                                .path("attribute")
                                                .asInt();

                                /*
                                 * Separate Attribute and Non-Attribute files.
                                 */
                                if (attribute == ATTRIBUTE_ICON) {

                                        attributeIconFileCount++;

                                } else {

                                        nonAttributeFileCount++;
                                }
                        }

                        /*
                         * Print OHKUMA result.
                         */
                        printOhkumaResult(
                                        totalFileCount,
                                        attributeIconFileCount,
                                        nonAttributeFileCount);

                        /*
                         * Add result to email report.
                         */
                        TestExecutionReport.addOhkumaResult(
                                        "OHKUMA S09 SOCKET",
                                        "Today",
                                        totalFileCount,
                                        attributeIconFileCount,
                                        nonAttributeFileCount,
                                        screenshotPath);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to process OHKUMA Today API response",
                                        e);
                }
        }

        /**
         * Print OHKUMA result to console.
         */
        private void printOhkumaResult(
                        int totalFileCount,
                        int attributeIconFileCount,
                        int nonAttributeFileCount) {

                System.out.println();
                System.out.println("========================================");
                System.out.println("             OHKUMA - TODAY");
                System.out.println("========================================");

                System.out.println(
                                "Total File Count          : "
                                                + totalFileCount);

                System.out.println(
                                "Attribute Icon File Count : "
                                                + attributeIconFileCount);

                System.out.println(
                                "Non-Attribute File Count  : "
                                                + nonAttributeFileCount);

                System.out.println("========================================");
        }
}