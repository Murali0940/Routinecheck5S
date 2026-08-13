package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OhkumaAPIFileUtil {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        private static final int OHKUMA_ATTRIBUTE = 1;

        /*
         * Add your folder names here.
         */
        private static final Set<String> FOLDER_NAMES = new HashSet<>(Arrays.asList(
                        "栃木溶接",
                        "栃木ベンダー",
                        "本社ベンダー",
                        "栃木艤装",
                        "本社溶接",
                        "本社スポット",
                        "栃木スポット",
                        "本社ロボット",
                        "栃木ロボット",
                        "本社機械加工",
                        "図面",
                        "栃木機械加工",
                        "本社出荷検査",
                        "艤装図面",
                        "組立図",
                        "カチオン吊りかけ",
                        "カチオン降ろし",
                        "本社プレス",
                        "栃木出荷検査",
                        "栃木スポット - コピー"));

        /**
         * Get OHKUMA Today's file information.
         */
        public void ohkumaGetTodayFiles(String jsonResponse, String screenshotPath) {

                try {

                        ObjectMapper mapper = new ObjectMapper();

                        JsonNode files = mapper.readTree(jsonResponse);

                        if (!files.isArray() || files.isEmpty()) {

                                System.out.println("No files found in API response.");
                                return;
                        }

                        LocalDate today = LocalDate.now();

                        int todayFileCount = 0;
                        int folderCount = 0;
                        int filesInsideFolderCount = 0;
                        int yellowFileCount = 0;
                        int nonYellowFileCount = 0;

                        System.out.println();
                        System.out.println("========================================");
                        System.out.println("             OHKUMA - TODAY");
                        System.out.println("========================================");

                        /*
                         * Process API records.
                         */
                        for (JsonNode file : files) {

                                String fileName = file
                                                .path("filename")
                                                .asText();

                                String date = file
                                                .path("date")
                                                .asText();

                                int attribute = file
                                                .path("attribute")
                                                .asInt();

                                LocalDate fileDate = LocalDateTime
                                                .parse(date, FORMATTER)
                                                .toLocalDate();

                                /*
                                 * Process only today's records.
                                 */
                                if (!fileDate.equals(today)) {
                                        continue;
                                }

                                todayFileCount++;

                                /*
                                 * Check whether the filename is one of
                                 * the folder names provided above.
                                 */
                                if (FOLDER_NAMES.contains(fileName)) {

                                        folderCount++;

                                        System.out.println(
                                                        "Folder Name : " + fileName);

                                        continue;
                                }

                                /*
                                 * File inside a folder.
                                 */
                                filesInsideFolderCount++;

                                /*
                                 * Yellow icon file.
                                 */
                                if (attribute == OHKUMA_ATTRIBUTE) {

                                        yellowFileCount++;

                                } else {

                                        /*
                                         * Non-yellow icon file.
                                         */
                                        nonYellowFileCount++;
                                }
                        }

                        System.out.println("----------------------------------------");

                        System.out.println(
                                        "Today File Count          : "
                                                        + todayFileCount);

                        System.out.println(
                                        "Folder Count              : "
                                                        + folderCount);

                        System.out.println(
                                        "Files Inside Folder Count : "
                                                        + filesInsideFolderCount);

                        System.out.println(
                                        "Yellow Icon Files         : "
                                                        + yellowFileCount);

                        System.out.println(
                                        "Non-Yellow Icon Files     : "
                                                        + nonYellowFileCount);

                        System.out.println("========================================");

                        /*
                         * Add OHKUMA result to email report.
                         */
                        TestExecutionReport.addOhkumaResult(
                                        "OHKUMA",
                                        "Today",
                                        todayFileCount,
                                        folderCount,
                                        filesInsideFolderCount,
                                        yellowFileCount,
                                        nonYellowFileCount,
                                        screenshotPath);

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Failed to process OHKUMA Today API response",
                                        e);
                }
        }
}