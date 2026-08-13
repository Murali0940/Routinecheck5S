package utils;

import java.io.File;
import java.util.List;

import org.apache.commons.mail2.jakarta.EmailAttachment;
import org.apache.commons.mail2.jakarta.MultiPartEmail;

public class EmailReportService {

    public static void sendEmail(String report) {

        try {

            String senderEmail = ConfigReader.get("SENDER_EMAIL_ADDRESS");
            String appPassword = ConfigReader.get("APP_PASSWORD");

            String receiver1 = ConfigReader.get("RECEIVER_EMAIL_ADDRESS1");
            String receiver2 = ConfigReader.get("RECEIVER_EMAIL_ADDRESS2");

            String[] receiverEmails = {
                    receiver1,
                    receiver2
            };

            MultiPartEmail email = new MultiPartEmail();

            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);

            email.setAuthentication(
                    senderEmail,
                    appPassword);

            email.setStartTLSEnabled(true);

            email.setFrom(senderEmail);

            for (String receiverEmail : receiverEmails) {
                email.addTo(receiverEmail);
            }

            email.setSubject(
                    "Routine Company Check");

            email.setMsg(report);

            /*
             * Attach all company screenshots.
             */
            List<String> screenshotPaths = TestExecutionReport.getScreenshotPaths();

            for (String screenshotPath : screenshotPaths) {

                File screenshotFile = new File(screenshotPath);

                if (screenshotFile.exists()) {

                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setPath(screenshotPath);
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setName(screenshotFile.getName());

                    email.attach(attachment);

                    System.out.println("Attaching screenshot: " + screenshotPath);

                } else {

                    System.out.println("Screenshot not found, skipping: " + screenshotPath);
                }
            }

            email.send();

            System.out.println("Email report sent successfully.");

        } catch (Exception e) {

            System.err.println("Failed to send email.");
            e.printStackTrace();
        }
    }
}