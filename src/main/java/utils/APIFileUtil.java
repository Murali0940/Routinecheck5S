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
     */
    private int getCompanyAttribute(String company) {

        if (company.equalsIgnoreCase("OAKSYSTEM")) {
            return 7;
        }

        if (company.equalsIgnoreCase("NISINOSEIKISS")) {
            return 4;
        }

        throw new IllegalArgumentException(
                "Unsupported company: " + company);
    }

    /**
     * Get yellow and non-yellow file information.
     *
     * Yellow files:
     * Count only.
     *
     * Non-yellow files:
     * Count + filenames.
     *
     * @param jsonResponse API response
     * @param dayFilter    Today / Yesterday
     * @param company      OAKSYSTEM / NISINOSEIKISS
     */
    public void getFilesByDay(
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

                String fileName = file.get("filename").asText();

                String date = file.get("date").asText();

                int attribute = file.get("attribute").asInt();

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
                            "Unsupported day filter: " + dayFilter);
                }

                // Process only files for the requested day
                if (!matchedDate) {
                    continue;
                }

                // Yellow icon file
                if (attribute == companyAttribute) {

                    yellowFileCount++;

                }
                // File without yellow icon
                else {

                    nonYellowFileCount++;

                    System.out.println(
                            "Non-Yellow File : " + fileName);
                }
            }

            System.out.println("--------------------------------------");
            System.out.println(
                    "Yellow Icon Files     : " + yellowFileCount);
            System.out.println(
                    "Non-Yellow Files      : " + nonYellowFileCount);
            System.out.println("======================================");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process getSocketFiles API response",
                    e);
        }
    }
}