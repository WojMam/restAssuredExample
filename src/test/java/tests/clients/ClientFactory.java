package tests.clients;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tests.utils.ConfigManager;

/**
 * Factory class for creating and managing API clients
 * 
 * This class provides a centralized way to create and manage API clients.
 * It uses the singleton pattern to ensure only one instance of each client exists,
 * and provides methods to retrieve clients for different services.
 * 
 * Usage:
 * - ClientFactory.getJsonPlaceholderClient()
 * - ClientFactory.getUserApiClient()
 * - ClientFactory.getPaymentApiClient()
 * - ClientFactory.getNotificationApiClient()
 */
public class ClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);
    
    // Singleton instances of clients
    private static final Map<String, BaseApiClient> clients = new HashMap<>();
    
    // Private constructor to prevent instantiation
    private ClientFactory() {
        // Private constructor
    }
    
    /**
     * Gets the JsonPlaceholder API client
     * 
     * @return JsonPlaceholderClient instance
     */
    public static JsonPlaceholderClient getJsonPlaceholderClient() {
        return (JsonPlaceholderClient) getOrCreateClient("jsonplaceholder", JsonPlaceholderClient.class);
    }
    
    /**
     * Gets the User API client
     * 
     * @return UserApiClient instance
     */
    public static UserApiClient getUserApiClient() {
        return (UserApiClient) getOrCreateClient("user", UserApiClient.class);
    }
    
    /**
     * Gets the Payment API client
     * 
     * @return PaymentApiClient instance
     */
    public static PaymentApiClient getPaymentApiClient() {
        return (PaymentApiClient) getOrCreateClient("payment", PaymentApiClient.class);
    }
    
    /**
     * Gets the Notification API client
     * 
     * @return NotificationApiClient instance
     */
    public static NotificationApiClient getNotificationApiClient() {
        return (NotificationApiClient) getOrCreateClient("notification", NotificationApiClient.class);
    }
    
    /**
     * Generic method to get or create a client
     * 
     * @param serviceName The name of the service
     * @param clientClass The class of the client to create
     * @return The client instance
     */
    private static BaseApiClient getOrCreateClient(String serviceName, Class<? extends BaseApiClient> clientClass) {
        if (!clients.containsKey(serviceName)) {
            synchronized (ClientFactory.class) {
                if (!clients.containsKey(serviceName)) {
                    if (!ConfigManager.isServiceConfigured(serviceName)) {
                        logger.warn("Service '{}' is not configured in properties file", serviceName);
                        throw new IllegalStateException("Service '" + serviceName + "' is not configured");
                    }
                    
                    try {
                        BaseApiClient client = clientClass.getDeclaredConstructor().newInstance();
                        clients.put(serviceName, client);
                        logger.info("Created new {} client for service '{}'", clientClass.getSimpleName(), serviceName);
                    } catch (Exception e) {
                        logger.error("Failed to create client for service '{}': {}", serviceName, e.getMessage());
                        throw new RuntimeException("Failed to create client for service: " + serviceName, e);
                    }
                }
            }
        }
        return clients.get(serviceName);
    }
    
    /**
     * Checks if a service is available and configured
     * 
     * @param serviceName The name of the service to check
     * @return true if the service is configured, false otherwise
     */
    public static boolean isServiceAvailable(String serviceName) {
        return ConfigManager.isServiceConfigured(serviceName);
    }
    
    /**
     * Gets all available service names
     * 
     * @return Array of available service names
     */
    public static String[] getAvailableServices() {
        return new String[]{"jsonplaceholder", "user", "payment", "notification"};
    }
    
    /**
     * Clears all cached clients (useful for testing)
     */
    public static void clearClients() {
        synchronized (ClientFactory.class) {
            clients.clear();
            logger.info("Cleared all cached API clients");
        }
    }
    
    /**
     * Gets the base URI for a specific service
     * 
     * @param serviceName The name of the service
     * @return The base URI for the service
     */
    public static String getServiceBaseUri(String serviceName) {
        return ConfigManager.getServiceBaseUrl(serviceName);
    }
    
    /**
     * Creates a custom client for a service not predefined in the factory
     * This method allows for dynamic client creation for services that might be
     * added later or for testing purposes
     * 
     * @param serviceName The name of the service
     * @param baseUri The base URI for the service
     * @return A generic BaseApiClient instance
     */
    public static BaseApiClient createCustomClient(String serviceName, String baseUri) {
        logger.info("Creating custom client for service '{}' with base URI: {}", serviceName, baseUri);
        return new BaseApiClient(serviceName, baseUri) {
            // Anonymous implementation of abstract BaseApiClient
        };
    }
} 