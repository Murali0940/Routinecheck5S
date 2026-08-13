package utils;

import java.io.File;
import java.util.List;

import org.apache.commons.mail2.jakarta.EmailAttachment;
import org.apache.commons.mail2.jakarta.HtmlEmail;

public class EmailReportService {

        public static void sendEmail(String report) {

                try {

                        // =====================================================
                        // EMAIL CONFIGURATION
                        // =====================================================

                        String senderEmail = ConfigReader.get("SENDER_EMAIL_ADDRESS");

                        String appPassword = ConfigReader.get("APP_PASSWORD");

                        String receiver1 = ConfigReader.get("RECEIVER_EMAIL_ADDRESS1");

                        String receiver2 = ConfigReader.get("RECEIVER_EMAIL_ADDRESS2");

                        String[] receiverEmails = {
                                        receiver1,
                                        receiver2
                        };

                        // =====================================================
                        // CREATE HTML EMAIL
                        // =====================================================

                        HtmlEmail email = new HtmlEmail();

                        email.setHostName("smtp.gmail.com");

                        email.setSmtpPort(587);

                        email.setAuthentication(
                                        senderEmail,
                                        appPassword);

                        email.setStartTLSEnabled(true);

                        // =====================================================
                        // SENDER
                        // =====================================================

                        email.setFrom(
                                        senderEmail,
                                        "Routine Company Check");

                        // =====================================================
                        // RECEIVERS
                        // =====================================================

                        for (String receiverEmail : receiverEmails) {

                                if (receiverEmail != null
                                                && !receiverEmail.trim().isEmpty()) {

                                        email.addTo(
                                                        receiverEmail.trim());
                                }
                        }

                        // =====================================================
                        // SUBJECT
                        // =====================================================

                        email.setSubject("Routine Company Check - Automation Report");

                        // IMPORTANT: UTF-8
                        email.setCharset("UTF-8");

                        // =====================================================
                        // HTML REPORT
                        // =====================================================

                        email.setHtmlMsg(report);

                        // =====================================================
                        // PLAIN TEXT FALLBACK
                        // =====================================================

                        email.setTextMsg("Please open this email in an HTML-compatible email client.");

                        // =====================================================
                        // ATTACH COMPANY SCREENSHOTS
                        // =====================================================

                        List<String> screenshotPaths = TestExecutionReport.getScreenshotPaths();

                        for (String screenshotPath : screenshotPaths) {

                                if (screenshotPath == null
                                                || screenshotPath.trim().isEmpty()) {

                                        continue;
                                }

                                File screenshotFile = new File(screenshotPath);

                                if (screenshotFile.exists()
                                                && screenshotFile.isFile()) {

                                        EmailAttachment attachment = new EmailAttachment();

                                        attachment.setPath(
                                                        screenshotFile.getAbsolutePath());

                                        attachment.setDisposition(
                                                        EmailAttachment.ATTACHMENT);

                                        attachment.setDescription(
                                                        "Automation execution screenshot");

                                        attachment.setName(
                                                        screenshotFile.getName());

                                        email.attach(attachment);

                                        System.out.println(
                                                        "Attaching screenshot: "
                                                                        + screenshotFile.getAbsolutePath());

                                } else {

                                        System.out.println(
                                                        "Screenshot not found, skipping: "
                                                                        + screenshotPath);
                                }
                        }

                        // =====================================================
                        // SEND EMAIL
                        // =====================================================

                        email.send();

                        System.out.println("======================================");

                        System.out.println("Email report sent successfully.");

                        System.out.println("======================================");

                } catch (Exception e) {

                        System.err.println("======================================");

                        System.err.println("Failed to send email.");

                        System.err.println("======================================");

                        e.printStackTrace();
                }
        }
}