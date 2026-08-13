package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class APIFileUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    /**
     * Returns the company-specific attribute value.
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
     * Get yellow and non-yellow file information.
     *
     * This method only calculates and returns the result.
     * It does NOT add anything to TestExecutionReport.
     */
    public FileCountResult getFilesByDay(
            String jsonResponse,
            String dayFilter,
            String company) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode files = mapper.readTree(jsonResponse);

            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            int companyAttribute = getCompanyAttribute(company);

            int yellowFileCount = 0;
            int nonYellowFileCount = 0;

            System.out.println();
            System.out.println("======================================");
            System.out.println("COMPANY : " + company);
            System.out.println("DAY     : " + dayFilter);
            System.out.println("======================================");

            for (JsonNode file : files) {

                String fileName = file.path("filename").asText();

                String date = file.path("date").asText();

                int attribute = file.path("attribute").asInt();

                LocalDate fileDate = LocalDateTime
                        .parse(date, FORMATTER)
                        .toLocalDate();

                boolean matchedDate;

                if (dayFilter.equalsIgnoreCase("Today")) {

                    matchedDate = fileDate.equals(today);

                } else if (dayFilter.equalsIgnoreCase("Yesterday")) {

                    matchedDate = fileDate.equals(yesterday);

                } else {

                    throw new IllegalArgumentException(
                            "Unsupported day filter: "
                                    + dayFilter);
                }

                // Process only requested day
                if (!matchedDate) {
                    continue;
                }

                // Attribute / Yellow icon file
                if (attribute == companyAttribute) {

                    yellowFileCount++;

                } else {

                    // No attribute / Non-yellow file
                    nonYellowFileCount++;

                    System.out.println(
                            "Non-Yellow File : "
                                    + fileName);
                }
            }

            System.out.println(
                    "--------------------------------------");

            System.out.println(
                    "Attribute Icon Files     : "
                            + yellowFileCount);

            System.out.println(
                    "No Attribute Icon Files : "
                            + nonYellowFileCount);

            System.out.println(
                    "======================================");

            return new FileCountResult(
                    yellowFileCount,
                    nonYellowFileCount,
                    null);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process getSocketFiles API response",
                    e);
        }
    }
}