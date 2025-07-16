package tests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * Gets the base URL for a specific service
     * @param serviceName The name of the service (e.g., "jsonplaceholder", "user", "payment")
     * @return The base URL for the specified service
     */
    public static String getServiceBaseUrl(String serviceName) {
        return properties.getProperty("service." + serviceName.toLowerCase() + ".baseurl");
    }

    /**
     * Gets the authentication token for a specific service
     * @param serviceName The name of the service
     * @return The authentication token for the specified service
     */
    public static String getServiceAuthToken(String serviceName) {
        return properties.getProperty("service." + serviceName.toLowerCase() + ".auth.token");
    }

    /**
     * Gets the authentication username for a specific service
     * @param serviceName The name of the service
     * @return The authentication username for the specified service
     */
    public static String getServiceAuthUsername(String serviceName) {
        return properties.getProperty("service." + serviceName.toLowerCase() + ".auth.username");
    }

    /**
     * Gets the authentication password for a specific service
     * @param serviceName The name of the service
     * @return The authentication password for the specified service
     */
    public static String getServiceAuthPassword(String serviceName) {
        return properties.getProperty("service." + serviceName.toLowerCase() + ".auth.password");
    }

    /**
     * Gets the timeout for a specific service
     * @param serviceName The name of the service
     * @return The timeout for the specified service, or default timeout if not found
     */
    public static int getServiceTimeout(String serviceName) {
        String timeout = properties.getProperty("service." + serviceName.toLowerCase() + ".timeout");
        return timeout != null ? Integer.parseInt(timeout) : getConnectionTimeout();
    }

    /**
     * Checks if a service is configured
     * @param serviceName The name of the service to check
     * @return true if the service has a base URL configured, false otherwise
     */
    public static boolean isServiceConfigured(String serviceName) {
        return getServiceBaseUrl(serviceName) != null;
    }
} 