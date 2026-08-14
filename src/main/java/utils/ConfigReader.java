package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {

        loadPropertiesFile(
                "config.file",
                "application.properties");

        loadPropertiesFile(
                "company.config",
                "company-credentials.properties");

        loadPropertiesFile(
                "user.config",
                "user-credentials.properties");

        loadPropertiesFile(
                "report.config",
                "test-report.properties");
    }

    private ConfigReader() {
        // Prevent object creation
    }

    private static void loadPropertiesFile(
            String systemProperty,
            String classpathFile) {

        String externalFile = System.getProperty(systemProperty);

        if (externalFile != null
                && !externalFile.isBlank()) {

            loadExternalProperties(externalFile);

        } else {

            loadClasspathProperties(classpathFile);
        }
    }

    private static void loadExternalProperties(
            String filePath) {

        try (InputStream inputStream = new FileInputStream(filePath)) {

            properties.load(inputStream);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load external properties file",
                    e);
        }
    }

    private static void loadClasspathProperties(
            String fileName) {

        try (InputStream inputStream = ConfigReader.class
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

    public static String get(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }
        String envVar = System.getenv(key);
        if (envVar != null && !envVar.isBlank()) {
            return envVar.trim();
        }
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value.trim();
    }
}