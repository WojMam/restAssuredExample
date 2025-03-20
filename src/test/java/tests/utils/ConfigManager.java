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

    /**
     * Loads the configuration file from the classpath
     */
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

    /**
     * Gets the base URL for the given environment
     * @param environment The environment to get the base URL for
     * @return The base URL for the given environment
     */ 
    public static String getBaseUrl(String environment) {
        return properties.getProperty("base.url." + environment.toLowerCase());
    }

    /**
     * Gets the authentication username
     * @return The authentication username
     */ 
    public static String getAuthUsername() {
        return properties.getProperty("auth.username");
    }

    /**
     * Gets the authentication password
     * @return The authentication password
     */
    public static String getAuthPassword() {
        return properties.getProperty("auth.password");
    }

    /**
     * Gets the authentication token
     * @return The authentication token
     */
    public static String getAuthToken() {
        return properties.getProperty("auth.token");
    }

    /**
     * Gets the connection timeout
     * @return The connection timeout
     */
    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }

    /**
     * Gets the read timeout
     * @return The read timeout
     */
    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("read.timeout"));
    }

    /**
     * Gets the test data path
     * @return The test data path
     */
    public static String getTestDataPath() {
        return properties.getProperty("test.data.path");
    }
} 