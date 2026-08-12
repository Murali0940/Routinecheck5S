package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties("application.properties");
        loadProperties("company-credentials.properties");
        loadProperties("user-credentials.properties");
        loadProperties("test-report.properties");
    }

    private ConfigReader() {
        // Prevent object creation
    }

    private static void loadProperties(String fileName) {

        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "Properties file not found: " + fileName);
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load properties file: " + fileName, e);
        }
    }

    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Property not found: " + key);
        }

        return value.trim();
    }
}