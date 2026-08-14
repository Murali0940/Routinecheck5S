package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {

        /*
         * Load application.properties.
         *
         * Jenkins:
         * -Dconfig.file=<secret file path>
         *
         * Local:
         * application.properties from resources.
         */
        loadApplicationProperties();

        /*
         * Load remaining properties
         * from src/main/resources.
         */
        loadProperties("company-credentials.properties");
        loadProperties("user-credentials.properties");
        loadProperties("test-report.properties");
    }

    private ConfigReader() {
        // Prevent object creation
    }

    /**
     * Load application.properties.
     *
     * Jenkins -> external secret file
     * Local -> classpath resource
     */
    private static void loadApplicationProperties() {

        String configFile = System.getProperty("config.file");

        if (configFile != null
                && !configFile.isBlank()) {

            loadExternalProperties(configFile);

        } else {

            loadProperties("application.properties");
        }
    }

    /**
     * Load properties from classpath.
     */
    private static void loadProperties(String fileName) {

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

    /**
     * Load properties from external file.
     *
     * Used by Jenkins Secret File.
     */
    private static void loadExternalProperties(
            String filePath) {

        try (InputStream inputStream = new FileInputStream(filePath)) {

            properties.load(inputStream);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load external properties file: "
                            + filePath,
                    e);
        }
    }

    /**
     * Get property value.
     */
    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {

            throw new RuntimeException(
                    "Property not found: "
                            + key);
        }

        return value.trim();
    }
}