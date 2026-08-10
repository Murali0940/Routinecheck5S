package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class APIFileUtil {

    private Page page;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    /**
     * @param jsonResponse API response from getSocketFiles
     * @param dayFilter    Today / Yesterday
     */
    public void getFilesByDay(String jsonResponse, String dayFilter) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode files = mapper.readTree(jsonResponse);

            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            int totalFiles = 0;
            int yellowFiles = 0;

            System.out.println("\n====================================");
            System.out.println("SECTION : " + dayFilter.toUpperCase());
            System.out.println("====================================");

            for (JsonNode file : files) {

                String fileName = file.get("filename").asText();

                String date = file.get("date").asText();

                int attribute = file.get("attribute").asInt();

                LocalDate fileDate = LocalDateTime.parse(date, FORMATTER).toLocalDate();

                boolean matched = false;

                if (dayFilter.equalsIgnoreCase("Today")) {

                    matched = fileDate.equals(today);

                } else if (dayFilter.equalsIgnoreCase("Yesterday")) {

                    matched = fileDate.equals(yesterday);

                }

                if (matched) {

                    totalFiles++;

                    if (attribute == 7) {
                        yellowFiles++;
                    }

                    System.out.println(fileName);

                }

            }

            System.out.println("------------------------------------");
            System.out.println("Total Files       : " + totalFiles);
            System.out.println("Yellow Icon Files : " + yellowFiles);
            System.out.println("====================================");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
