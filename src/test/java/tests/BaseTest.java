package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tests.clients.ClientFactory;
import tests.clients.JsonPlaceholderClient;
import tests.clients.NotificationApiClient;
import tests.clients.PaymentApiClient;
import tests.clients.UserApiClient;

/**
 * Base test class for API testing
 * 
 * This class provides common setup and teardown for API tests using the client pattern.
 * It initializes API clients and provides convenient access to them for subclasses.
 * 
 * Instead of using global baseUri, tests should use the specific client methods:
 * - jsonPlaceholderClient for JSONPlaceholder API tests
 * - userApiClient for User API tests
 * - paymentApiClient for Payment API tests
 * - notificationApiClient for Notification API tests
 */
public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    // API Clients - accessible to subclasses
    protected static JsonPlaceholderClient jsonPlaceholderClient;
    protected static UserApiClient userApiClient;
    protected static PaymentApiClient paymentApiClient;
    protected static NotificationApiClient notificationApiClient;

    @BeforeAll
    public static void setup() {
        logger.info("Setting up API clients for tests");
        
        // Initialize API clients through the factory
        try {
            // Always initialize JSONPlaceholder client (it's our main test API)
            jsonPlaceholderClient = ClientFactory.getJsonPlaceholderClient();
            logger.info("JsonPlaceholder client initialized");
            
            // Initialize other clients if they are configured
            if (ClientFactory.isServiceAvailable("user")) {
                userApiClient = ClientFactory.getUserApiClient();
                logger.info("User API client initialized");
            } else {
                logger.warn("User API service is not configured - client will be null");
            }
            
            if (ClientFactory.isServiceAvailable("payment")) {
                paymentApiClient = ClientFactory.getPaymentApiClient();
                logger.info("Payment API client initialized");
            } else {
                logger.warn("Payment API service is not configured - client will be null");
            }
            
            if (ClientFactory.isServiceAvailable("notification")) {
                notificationApiClient = ClientFactory.getNotificationApiClient();
                logger.info("Notification API client initialized");
            } else {
                logger.warn("Notification API service is not configured - client will be null");
            }
            
        } catch (Exception e) {
            logger.error("Failed to initialize API clients: {}", e.getMessage());
            throw new RuntimeException("Failed to setup API clients", e);
        }
        
        logger.info("API clients setup completed");
    }

    @AfterAll
    public static void cleanup() {
        logger.info("Cleaning up API clients");
        
        // Clear all cached clients
        ClientFactory.clearClients();
        
        // Reset client references
        jsonPlaceholderClient = null;
        userApiClient = null;
        paymentApiClient = null;
        notificationApiClient = null;
        
        logger.info("API clients cleanup completed");
    }
    
    /**
     * Utility method to check if a service is available for testing
     * 
     * @param serviceName The name of the service to check
     * @return true if the service is configured and available
     */
    protected static boolean isServiceAvailable(String serviceName) {
        return ClientFactory.isServiceAvailable(serviceName);
    }
    
    /**
     * Utility method to get the base URI for a service
     * 
     * @param serviceName The name of the service
     * @return The base URI for the service
     */
    protected static String getServiceBaseUri(String serviceName) {
        return ClientFactory.getServiceBaseUri(serviceName);
    }
} 