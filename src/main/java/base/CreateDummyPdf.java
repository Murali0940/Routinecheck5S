package base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import com.microsoft.playwright.Page;

public class CreateDummyPdf {

    private Page page;

    public CreateDummyPdf(Page page) {
        this.page = page;
    }

    public void createDummypdf() {

        page.waitForTimeout(2000);

        try {

            // ============================================================
            // PROJECT DIRECTORY
            // ============================================================

            String projectDirectory = System.getProperty("user.dir");

            // ============================================================
            // CREATE SampleFile FOLDER
            // ============================================================

            Path sampleFolder = Paths.get(projectDirectory, "SampleFile");

            Files.createDirectories(sampleFolder);

            // ============================================================
            // CREATE TIMESTAMP
            // ============================================================

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // ============================================================
            // PDF FILE NAME
            // ============================================================

            String fileName = "DummyFile_"
                    + timestamp
                    + ".pdf";

            Path pdfPath = sampleFolder.resolve(fileName);

            // ============================================================
            // CREATE PDF
            // ============================================================

            try (PDDocument document = new PDDocument()) {

                // Create page
                PDPage page = new PDPage();

                document.addPage(page);

                // ========================================================
                // ADD CONTENT
                // ========================================================

                try (PDPageContentStream contentStream = new PDPageContentStream(
                        document,
                        page)) {

                    contentStream.beginText();

                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);

                    contentStream.newLineAtOffset(100, 700);

                    contentStream.showText("This is for testing purpose");

                    contentStream.endText();
                }

                // ========================================================
                // SAVE PDF
                // ========================================================

                document.save(pdfPath.toFile());
            }

            // ============================================================
            // RESULT
            // ============================================================

            System.out.println("========================================");

            System.out.println("PDF created successfully.");

            System.out.println("Project Directory : " + projectDirectory);

            System.out.println("PDF File          : " + fileName);

            System.out.println("PDF Path          : " + pdfPath.toAbsolutePath());

            System.out.println("========================================");

        } catch (IOException e) {

            System.err.println("Failed to create PDF.");

            e.printStackTrace();
        }
    }
}
