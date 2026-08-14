package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {

        /*
         * Load all four configuration files.
         *
         * Jenkins:
         * Load files from Jenkins Secret File paths.
         *
         * Local:
         * Load files from src/main/resources.
         */
        loadApplicationProperties();
        loadCompanyCredentials();
        loadUserCredentials();
        loadTestReportProperties();
    }

    private ConfigReader() {
        // Prevent object creation
    }

    // ============================================================
    // APPLICATION PROPERTIES
    // ============================================================

    private static void loadApplicationProperties() {

        loadExternalOrClasspath(
                "config.file",
                "application.properties");
    }

    // ============================================================
    // COMPANY CREDENTIALS
    // ============================================================

    private static void loadCompanyCredentials() {

        loadExternalOrClasspath(
                "company.config",
                "company-credentials.properties");
    }

    // ============================================================
    // USER CREDENTIALS
    // ============================================================

    private static void loadUserCredentials() {

        loadExternalOrClasspath(
                "user.config",
                "user-credentials.properties");
    }

    // ============================================================
    // TEST REPORT
    // ============================================================

    private static void loadTestReportProperties() {

        loadExternalOrClasspath(
                "report.config",
                "test-report.properties");
    }

    // ============================================================
    // LOAD PROPERTY FILE
    // ============================================================

    private static void loadExternalOrClasspath(
            String systemProperty,
            String classpathFile) {

        String externalFile =
                System.getProperty(systemProperty);

        if (externalFile != null
                && !externalFile.isBlank()) {

            loadExternalProperties(externalFile);

        } else {

            loadClasspathProperties(classpathFile);
        }
    }

    // ============================================================
    // LOAD FROM JENKINS / EXTERNAL FILE
    // ============================================================

    private static void loadExternalProperties(
            String filePath) {

        try (InputStream inputStream =
                     new FileInputStream(filePath)) {

            properties.load(inputStream);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load external properties file: "
                            + filePath,
                    e);
        }
    }

    // ============================================================
    // LOAD FROM RESOURCES / LOCAL
    // ============================================================

    private static void loadClasspathProperties(
            String fileName) {

        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (inputStream == null) {

                throw new RuntimeException(
                        "Properties file not found: "
                                + fileName);
            }

            properties.load(inputStream);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load properties file: "
                            + fileName,
                    e);
        }
    }

    // ============================================================
    // GET PROPERTY
    // ============================================================

    public static String get(String key) {

        String value =
                properties.getProperty(key);

        if (value == null
                || value.isBlank()) {

            throw new RuntimeException(
                    "Property not found: "
                            + key);
        }

        return value.trim();
    }
}