package tests.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Unable to find {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found");
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading configuration: {}", e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getBaseUrl(String environment) {
        return properties.getProperty("base.url." + environment.toLowerCase());
    }

    public static String getAuthUsername() {
        return properties.getProperty("auth.username");
    }

    public static String getAuthPassword() {
        return properties.getProperty("auth.password");
    }

    public static String getAuthToken() {
        return properties.getProperty("auth.token");
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }

    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("read.timeout"));
    }

    public static String getTestDataPath() {
        return properties.getProperty("test.data.path");
    }
} 