package pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.util.stream.Stream;

import base.CreateDummyPdf;

public class AttributeProcess {

        private Page page;
        public CreateDummyPdf createDummyPdf;

        public AttributeProcess(Page page) {
                this.page = page;
                this.createDummyPdf = new CreateDummyPdf(page);
        }

        public void clickHyperlinkIcon() {
                page.locator("//h4[text()='HyperLink']/preceding::input[@src='assets/icons/hyperLink.png' and @type='image']")
                                .click();
        }

        public void clickScanIcon() {
                page.locator("//h4[text()='ソケット名']/preceding::input[@src='assets/icons/Scan.png' and @type='image']")
                                .click();
        }

        public void setAttribute() {
                createDummyPdf.createDummypdfFile();
        }

        public void dragLatestPdfToScanSocket() throws IOException {

                // Local folder
                Path folderPath = Paths.get(
                                "D:\\PlaywrightJava\\registrationservice\\SampleFile");

                // Find latest PDF
                Path latestFile;

                try (Stream<Path> files = Files.list(folderPath)) {

                        latestFile = files
                                        .filter(Files::isRegularFile)
                                        .filter(path -> path.toString()
                                                        .toLowerCase()
                                                        .endsWith(".pdf"))
                                        .max(
                                                        Comparator.comparingLong(
                                                                        path -> path.toFile().lastModified()))
                                        .orElseThrow(() -> new RuntimeException(
                                                        "No PDF file found in: "
                                                                        + folderPath));
                }

                System.out.println(
                                "[TESTPRO SCAN] Latest PDF: "
                                                + latestFile.getFileName());

                /*
                 * Website's hidden file input.
                 *
                 * This produces the same file-upload result
                 * as dropping the local file onto the upload area.
                 */
                Locator fileInput = page.locator(
                                "input[type='file']");

                fileInput.setInputFiles(latestFile);

                Allure.step(
                                "[TESTPRO SCAN] Uploaded latest PDF: "
                                                + latestFile.getFileName());

                System.out.println(
                                "[TESTPRO SCAN] Successfully uploaded: "
                                                + latestFile);
        }

        public void drawingManagementicon() {
                page.locator("//h4[normalize-space()='Drawing Management']/preceding-sibling::div//img[contains(@src,'Digital 5S.png')]")
                                .click();
        }

        public void uploadFile(){
                
        }
        

}
